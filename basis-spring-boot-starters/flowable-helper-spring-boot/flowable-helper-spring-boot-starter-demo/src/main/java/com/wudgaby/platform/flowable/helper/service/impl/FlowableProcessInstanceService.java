package com.wudgaby.platform.flowable.helper.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.flowable.helper.cmd.DeleteFlowableProcessInstanceCmd;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.enums.CommentTypeEnum;
import com.wudgaby.platform.flowable.helper.mapper.FlowableProcessInstanceDao;
import com.wudgaby.platform.flowable.helper.service.FlowableBpmnModelService;
import com.wudgaby.platform.flowable.helper.service.FlowableTaskService;
import com.wudgaby.platform.flowable.helper.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.User;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName : FlowableProcessInstanceService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/19 9:14
 * @Desc :   TODO
 */
@Slf4j
@Service
public class FlowableProcessInstanceService extends BaseProcessService implements com.wudgaby.platform.flowable.helper.service.FlowableProcessInstanceService {
    @Autowired
    private FlowableBpmnModelService flowableBpmnModelService;
    @Autowired
    private FlowableProcessInstanceDao flowableProcessInstanceDao;
    @Autowired
    private FlowProcessDiagramGenerator flowProcessDiagramGenerator;
    @Autowired
    private FlowableTaskService flowableTaskService;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected IdentityService identityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startProcessInstanceByKey(StartProcessInstanceVo params) {
        if (StringUtils.isNotBlank(params.getProcessDefinitionKey())
                && StringUtils.isNotBlank(params.getBusinessKey())
                //&& StringUtils.isNotBlank(params.getSystemSn())
        ) {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                                                        .processDefinitionKey(params.getProcessDefinitionKey())
                                                        .latestVersion().singleResult();
            if(processDefinition == null){
                throw new BusinessException("该流程未部署,请部署后重试.");
            }
            if (processDefinition != null && processDefinition.isSuspended()) {
                throw new BusinessException("此流程已经挂起,请联系系统管理员!");
            }
            if(params.getVariables() == null){
                params.setVariables(Maps.newHashMap());
            }
            //1.1、设置提交人字段为空字符串让其自动跳过
            params.getVariables().put(FlowConstant.FLOW_SUBMITTER_VAR, "");
            //1.2、设置可以自动跳过
            params.getVariables().put(FlowConstant.FLOWABLE_SKIP_EXPRESSION_ENABLED, true);

            // TODO 1.3、汇报线的参数设置
            //2、当我们流程创建人和发起人
            String creator = params.getCreator();
            if (StringUtils.isBlank(creator)) {
                creator = params.getCurrentUserCode();
                params.setCreator(creator);
            }
            Authentication.setAuthenticatedUserId(creator);

            //不行. 流程实例中的startUserId调用的是Authentication.getAuthenticatedUserId();
            //params.getVariables().put(FlowConstant.FLOW_INITIATOR_VAR, creator);
            params.getVariables().put(FlowConstant.PROCESS_DEF_VAR, params.getProcessDefinitionKey().trim());

            //3.启动流程
            ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                    .processDefinitionKey(params.getProcessDefinitionKey().trim())
                    .name(Optional.ofNullable(params.getFormName()).map(name -> name.trim()).orElse(""))
                    .businessKey(params.getBusinessKey().trim())
                    .variables(params.getVariables())
                    .tenantId(params.getSystemSn().trim())
                    .start();

            //这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
            Authentication.setAuthenticatedUserId(null);

            log.info("启动流程, 流程实例ID : {}", processInstance.getId());
            List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstance.getProcessInstanceId())
                    .finished()
                    .orderByTaskCreateTime()
                    .asc()
                    .list();

            String taskId = "";
            if(CollectionUtils.isNotEmpty(historicTaskInstanceList)){
                taskId = historicTaskInstanceList.get(0).getId();
            }

            //4.添加审批记录
            log.info("添加审批记录");
            this.addComment(taskId, params.getCurrentUserCode(), processInstance.getProcessInstanceId(),
                    Optional.ofNullable(params.getCommentTypeEnum()).orElse(CommentTypeEnum.TJ).toString(), "");
            //5.TODO 推送消息数据

            return processInstance;
        }
        throw new BusinessException("参数不正确, 请填写ProcessDefinitionKey,BusinessKey.");
    }

    @Override
    public IPage<ProcessInstanceVo> getProcessInstances(ProcessInstanceQueryVo queryForm) {
        IPage<ProcessInstanceVo> page = flowableProcessInstanceDao.listPage(new Page(queryForm.getPageNum(), queryForm.getPageCount()), queryForm);
        page.getRecords().forEach(processInstanceVo -> {
            this.setStateApprover(processInstanceVo);
        });
        return page;
    }

    @Override
    public ProcessInstanceVo getProcessInstance(String processInstanceId) {
        ProcessInstanceVo processInstanceVo = flowableProcessInstanceDao.getByProcessInstanceId(processInstanceId);
        this.setStateApprover(processInstanceVo);
        return processInstanceVo;
    }

    /**
     * 设置状态和审批人
     *
     * @param processInstanceVo 参数
     */
    private void setStateApprover(ProcessInstanceVo processInstanceVo) {
        if(processInstanceVo == null){
            return;
        }

        if (processInstanceVo.getEndTime() == null) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceVo.getProcessInstanceId())
                    .singleResult();
            if (processInstance.isSuspended()) {
                processInstanceVo.setSuspensionState(FlowConstant.SUSPENSION_STATE);
            } else {
                processInstanceVo.setSuspensionState(FlowConstant.ACTIVATE_STATE);
            }
        }
        List<User> approvers = flowableTaskService.getApprovers(processInstanceVo.getProcessInstanceId());
        String userNames = this.createApprovers(approvers);
        processInstanceVo.setApprover(userNames);
    }

    /**
     * 组合审批人显示名称
     *
     * @param approvers 审批人列表
     * @return
     */
    private String createApprovers(List<User> approvers) {
        if (CollectionUtils.isNotEmpty(approvers)) {
            StringBuffer approverstr = new StringBuffer();
            StringBuffer finalApproverstr = approverstr;
            approvers.forEach(user -> {
                finalApproverstr.append(user.getFirstName()).append(";");
            });
            if (approverstr.length() > 0) {
                approverstr = approverstr.deleteCharAt(approverstr.length() - 1);
            }
            return approverstr.toString();
        }
        return null;
    }

    @Override
    public byte[] createImage(String processInstanceId) {
        //1.获取当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = null;
        List<String> activeActivityIds = new ArrayList<>();
        List<String> highLightedFlows = new ArrayList<>();
        //2.获取所有的历史轨迹线对象
        List<HistoricActivityInstance> historicSquenceFlows = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).activityType(BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW).list();
        historicSquenceFlows.forEach(historicActivityInstance -> highLightedFlows.add(historicActivityInstance.getActivityId()));
        //3. 获取流程定义id和高亮的节点id
        if (processInstance != null) {
            //3.1. 正在运行的流程实例
            processDefinitionId = processInstance.getProcessDefinitionId();
            activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        } else {
            //3.2. 已经结束的流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if(historicProcessInstance == null){
                throw new BusinessException("该流程实例未找到").setPrintLog(false);
            }

            processDefinitionId = historicProcessInstance.getProcessDefinitionId();

            //3.3. 获取结束节点列表
            List<HistoricActivityInstance> historicEnds = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId).activityType(BpmnXMLConstants.ELEMENT_EVENT_END).list();
            List<String> finalActiveActivityIds = activeActivityIds;
            historicEnds.forEach(historicActivityInstance -> finalActiveActivityIds.add(historicActivityInstance.getActivityId()));
        }
        //4. 获取bpmnModel对象
        BpmnModel bpmnModel = flowableBpmnModelService.getBpmnModelByProcessDefId(processDefinitionId);
        //5. 生成图片流
        InputStream inputStream = flowProcessDiagramGenerator.generateDiagram(bpmnModel, activeActivityIds, highLightedFlows);
        //6. 转化成byte便于网络传输
        return IoUtil.readInputStream(inputStream, "image inputStream name");
    }

    @Override
    public void deleteProcessInstanceById(String processInstanceId) {
        long count = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).count();
        if (count > 0) {
            DeleteFlowableProcessInstanceCmd cmd = new DeleteFlowableProcessInstanceCmd(processInstanceId, "删除流程实例", true);
            managementService.executeCommand(cmd);
        } else {
            historyService.deleteHistoricProcessInstance(processInstanceId);
        }
    }

    @Override
    public void stopProcessInstanceById(EndProcessVo endVo) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(endVo.getProcessInstanceId()).singleResult();
        if (processInstance == null) {
            throw new BusinessException("不存在运行的流程实例,请确认!");
        }
        //1、添加审批记录
        this.addComment(endVo.getUserCode(), endVo.getProcessInstanceId(), CommentTypeEnum.ZZ.toString(), endVo.getMessage());
        List<EndEvent> endNodes = flowableBpmnModelService.findEndFlowElement(processInstance.getProcessDefinitionId());
        String endId = endNodes.get(0).getId();
        String processInstanceId = endVo.getProcessInstanceId();
        //2、执行终止
        List<Execution> executions = runtimeService.createExecutionQuery().parentId(processInstanceId).list();
        List<String> executionIds = new ArrayList<>();
        executions.forEach(execution -> executionIds.add(execution.getId()));
        this.moveExecutionsToSingleActivityId(executionIds, endId);
    }

    @Override
    public void revokeProcess(RevokeProcessVo revokeVo) {
        if (StringUtils.isNotBlank(revokeVo.getProcessInstanceId())) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(revokeVo.getProcessInstanceId()).singleResult();
            if (processInstance != null) {
                //1.添加撤回意见
                this.addComment(revokeVo.getUserCode(), revokeVo.getProcessInstanceId(), CommentTypeEnum.CH.toString(), revokeVo.getMessage());
                //2.设置提交人
                runtimeService.setVariable(revokeVo.getProcessInstanceId(), FlowConstant.FLOW_SUBMITTER_VAR, processInstance.getStartUserId());
                //3.执行撤回
                Activity disActivity = flowableBpmnModelService.findActivityByName(processInstance.getProcessDefinitionId(), FlowConstant.FLOW_SUBMITTER);
                //4.删除运行和历史的节点信息
                this.deleteActivity(disActivity.getId(), revokeVo.getProcessInstanceId());
                //5.执行跳转
                List<Execution> executions = runtimeService.createExecutionQuery().parentId(revokeVo.getProcessInstanceId()).list();
                List<String> executionIds = new ArrayList<>();
                executions.forEach(execution -> executionIds.add(execution.getId()));
                this.moveExecutionsToSingleActivityId(executionIds, disActivity.getId());
            }
            return;
        }
        throw new BusinessException("流程实例id不能为空!");
    }
}
