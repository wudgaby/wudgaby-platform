package com.wudgaby.platform.flowable.helper.cmd;

/**
 * @ClassName : ActJumpTaskCmd
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/29 15:44
 * @Desc :   
 */

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.impl.cmd.NeedsActiveTaskCmd;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.List;
import java.util.Map;

/**
 * @description: 自由跳转流程
 * @author: starmark
 * @create: 2018-10-13 09:22
 * @desc
 * 清除相关任务(act_ru_task)
 * 清除局部变量(act_ru_variable), 注意是局部变量
 * 清除轨迹(act_ru_execution)
 * 保留act_ru_execution到只剩下两条记录再往下驱动流程.
 * 注意:activti自由跳转也是同样的道理.
 *
 * 但并行分支的驳回有两种。
 * 分支内的驳回，即驳回前有多少条分支，驳回后还是有多少条分支,研究一下act_ru_execution，看清哪些数据再处理
 * 分支外的驳回，即原来有5条分支，可能驳回后只有一条。这样的话，还是删除掉act_ru_execution到只剩下两条记录即可.
 **/
public class ActJumpTaskCmd extends NeedsActiveTaskCmd<Boolean> {
    //执行实例id
    protected String processId;
    //目标节点
    protected String targetNodeId;
    //变量
    protected Map<String, Object> formData;
    protected String operationCode;

    public ActJumpTaskCmd(String processId, String taskId, String targetNodeId, Map<String, Object> formData,String operationCode) {
        super(taskId);
        this.processId = processId;
        this.targetNodeId = targetNodeId;
        this.formData = formData;
        this.operationCode=operationCode;
    }

    @Override
    protected Boolean execute(CommandContext commandContext, TaskEntity task) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        ExecutionEntity rootExecution = executionEntityManager.findChildExecutionsByParentExecutionId(processId).get(0);

        CommandContextUtil.getTaskService().deleteTask(task, true);

        List<ExecutionEntity> executionEntityList = executionEntityManager.findChildExecutionsByParentExecutionId(rootExecution.getId());
        for(ExecutionEntity executionEntity : executionEntityList){
            List<ExecutionEntity> executionEntityList2 = executionEntityManager.findChildExecutionsByParentExecutionId(executionEntity.getId());

            for(ExecutionEntity executionEntity2:executionEntityList2){
                CommandContextUtil.getTaskService().deleteTasksByExecutionId(executionEntity2.getId());
                executionEntityManager.deleteChildExecutions(executionEntity2,"delete",true);
                executionEntityManager.delete(executionEntity2);
                CommandContextUtil.getVariableService().deleteVariablesByExecutionId(executionEntity2.getId());
            }
            CommandContextUtil.getTaskService().deleteTasksByExecutionId(executionEntity.getId());
            executionEntityManager.deleteChildExecutions(executionEntity,"delete",true);
            executionEntityManager.delete(executionEntity);
            CommandContextUtil.getVariableService().deleteVariablesByExecutionId(executionEntity.getId());
        }

        Process process = ProcessDefinitionUtil.getProcess(rootExecution.getProcessDefinitionId());
        FlowElement targetFlowElement = process.getFlowElement(targetNodeId);
        rootExecution.setCurrentFlowElement(targetFlowElement);
        FlowableEngineAgenda agenda = CommandContextUtil.getAgenda();
        agenda.planContinueProcessInCompensation(rootExecution);
        return true;
    }
}
