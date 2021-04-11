package com.wudgaby.platform.flowable.helper.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.wudgaby.platform.flowable.helper.config.FlowableProperites;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.consts.SysConstant;
import com.wudgaby.platform.flowable.helper.enums.AuditType;
import com.wudgaby.platform.flowable.helper.enums.VoteResultType;
import com.wudgaby.platform.flowable.helper.service.EmployeeService;
import com.wudgaby.platform.flowable.helper.service.FlowableProcessInstanceService;
import com.wudgaby.platform.flowable.helper.service.VoteService;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName : VoteServiceImpl
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/19 14:47
 * @Desc :   TODO
 */
@Service("voteService")
public class VoteServiceImpl implements VoteService {
    @Autowired
    private FlowableProperites flowableProperites;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FlowableProcessInstanceService flowableProcessInstanceService;

    @Override
    public List<String> getVoters(DelegateExecution execution) {
        FlowElement flowElement = execution.getCurrentFlowElement();
        UserTask userTask = (UserTask) flowElement;

        //从配置中获取fromByProcDefId(execution.getProcessDefinitionId())
        List<String> assigneeList = Lists.newArrayList();
        assigneeList.addAll(employeeService.getEmployeesLikeUsername("hkbird"));

        return assigneeList;
    }


    @Override
    public boolean isComplete(DelegateExecution execution) {
        //已完成的实例数量。
        Integer nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");
        //实例总数
        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances");
        //是否全部投票完成
        boolean isAllCompleted = nrOfCompletedInstances.equals(nrOfInstances);
        //同意
        int agreeCount = 0;
        //拒绝
        int rejectCount = 0;
        //弃权
        int abstainCount = 0;

        String nodeId = execution.getCurrentActivityId();

        Map<String, Object> variables = execution.getVariables();
        for (String key : variables.keySet()) {
            //会签投票以nodeId + VOTE + taskId标识
            //获得会签投票的统计结果
            if (key.startsWith(nodeId + SysConstant.SYMBOL_UNDERLINE + FlowConstant.VOTE)) {
                String value = Objects.toString(variables.get(key));
                //统计同意、驳回、弃权票数
                switch (AuditType.valueOf(value)){
                    case pass:
                        ++agreeCount;
                        break;
                    case reject:
                        ++rejectCount;
                        break;
                    case abstain:
                        ++abstainCount;
                        break;
                    default:
                        //nothing
                        break;
                }
            }
        }

        UserTask userTask = (UserTask) execution.getCurrentFlowElement();
        VoteResultType voteResultType = flowableProperites.getMultiCase().getVoteResultType();
        if(userTask.getName().contains("或签")){
            voteResultType = VoteResultType.OR_SIGN;
        }else{
            voteResultType = VoteResultType.ALL_PASS;
        }

        boolean result;
        switch (voteResultType){
            case ONE_VOTE_PASS:
                result = onePass(execution, isAllCompleted, nrOfInstances, nrOfCompletedInstances, agreeCount, rejectCount, abstainCount);
                break;
            case ONE_VOTE_VETO:
                result = oneVeto(execution, isAllCompleted, nrOfInstances, nrOfCompletedInstances, agreeCount, rejectCount, abstainCount);
                break;
            case PERCENT:
                result = percentPass(execution, isAllCompleted, nrOfInstances, nrOfCompletedInstances, agreeCount, rejectCount, abstainCount);
                break;
            case OR_SIGN:
                result = orSign(execution, isAllCompleted, nrOfInstances, nrOfCompletedInstances, agreeCount, rejectCount, abstainCount);
                break;
            case ALL_PASS:
            default:
                result = allPass(execution, isAllCompleted, nrOfInstances, nrOfCompletedInstances, agreeCount, rejectCount, abstainCount);
                break;
        }

        //设置流程结果
        if(result){
            Boolean voteResult = execution.getVariable(FlowConstant.VOTE_RESULT, Boolean.class);
            if(voteResult.equals(Boolean.TRUE)){
                execution.setVariable(FlowConstant.OUTCOME, AuditType.pass.name());
            }else{
                execution.setVariable(FlowConstant.OUTCOME, AuditType.reject.name());
            }
        }
        return result;

        /*if (!nrOfCompletedInstances.equals(nrOfInstances)) {
            //必须等所有的办理人都投票
            return false;
        } else {
            //会签全部完成时,使用默认规则结束
            if (rejectCount > 0) {
                //有反对票，则最终的会签结果为不通过
                //移除SIGN_VOTE+TaskId为标识的参数
                //removeSignVars(execution);
                //增加会签结果参数，以便之后流转使用
                execution.setVariable(VOTE_RESULT, false);
                //会签结束
                return true;
            } else {
                //没有反对票时，则最终的会签结果为通过
                //removeSignVars(execution);
                execution.setVariable(VOTE_RESULT, true);
                return true;
            }
        }*/
    }

    /**
     * 或签
     * @param execution
     * @param isAllCompleted
     * @param total
     * @param completeCount
     * @param agreeCount
     * @param rejectCount
     * @param abstainCount
     * @return
     */
    private boolean orSign(DelegateExecution execution, boolean isAllCompleted,
                            int total, int completeCount, int agreeCount, int rejectCount, int abstainCount){
        if (agreeCount >= 1) {
            execution.setVariable(FlowConstant.VOTE_RESULT, true);
            return true;
        }
        if (rejectCount >= 1) {
            execution.setVariable(FlowConstant.VOTE_RESULT, false);
            return true;
        }

        if(isAllCompleted){
            if (agreeCount >= 1) {
                execution.setVariable(FlowConstant.VOTE_RESULT, true);
            }else{
                execution.setVariable(FlowConstant.VOTE_RESULT, false);
            }
            return true;
        }

        return false;
    }

    /**
     * 一票通过
     * @return
     */
    private boolean onePass(DelegateExecution execution, boolean isAllCompleted,
                            int total, int completeCount, int agreeCount, int rejectCount, int abstainCount){
        if (agreeCount >= 1) {
            execution.setVariable(FlowConstant.VOTE_RESULT, true);
            return true;
        }

        if(isAllCompleted){
            if (agreeCount >= 1) {
                execution.setVariable(FlowConstant.VOTE_RESULT, true);
            }else{
                execution.setVariable(FlowConstant.VOTE_RESULT, false);
            }
            return true;
        }

        return false;
    }

    /**
     * 一票否决
     * @return
     */
    private boolean oneVeto(DelegateExecution execution, boolean isAllCompleted,
                            int total, int completeCount, int agreeCount, int rejectCount, int abstainCount){

        if (rejectCount >= 1) {
            execution.setVariable(FlowConstant.VOTE_RESULT, false);
            return true;
        }

        if(isAllCompleted){
            if (rejectCount >= 1) {
                execution.setVariable(FlowConstant.VOTE_RESULT, false);
            }else{
                execution.setVariable(FlowConstant.VOTE_RESULT, true);
            }
            return true;
        }

        return false;
    }

    /**
     * 比例通过 默认0.5
     * @return
     */
    private boolean percentPass(DelegateExecution execution, boolean isAllCompleted,
                                int total, int completeCount, int agreeCount, int rejectCount, int abstainCount){

        double percent = NumberUtil.div(agreeCount, total);

        if (percent >= flowableProperites.getMultiCase().getPassPercent()) {
            execution.setVariable(FlowConstant.VOTE_RESULT, true);
            return true;
        }

        if(isAllCompleted){
            if (percent >= flowableProperites.getMultiCase().getPassPercent()) {
                execution.setVariable(FlowConstant.VOTE_RESULT, true);
            }else{
                execution.setVariable(FlowConstant.VOTE_RESULT, false);
            }
            return true;
        }
        return false;
    }

    /**
     * 所有通过
     * @return
     */
    private boolean allPass(DelegateExecution execution, boolean isAllCompleted,
                            int total, int completeCount, int agreeCount, int rejectCount, int abstainCount){
        if(rejectCount >= 1){
            execution.setVariable(FlowConstant.VOTE_RESULT, false);
            return true;
        }

        if(isAllCompleted){
            if (total == agreeCount) {
                execution.setVariable(FlowConstant.VOTE_RESULT, true);
            }else{
                execution.setVariable(FlowConstant.VOTE_RESULT, false);
            }
            return true;
        }
        return false;
    }

    /**
     * copy from ParallelMultiInstanceBehavior
     * @param execution
     * @param variableName
     * @return
     * 一层层往上找变量
     * 该实现方式为累加法
     */
    public Object getLoopVariable(DelegateExecution execution, String variableName) {
        Object value = execution.getVariableLocal(variableName);
        DelegateExecution parent = execution.getParent();
        while (value == null && parent != null) {
            value = parent.getVariableLocal(variableName);
            parent = parent.getParent();
        }
        return value;
    }
}
