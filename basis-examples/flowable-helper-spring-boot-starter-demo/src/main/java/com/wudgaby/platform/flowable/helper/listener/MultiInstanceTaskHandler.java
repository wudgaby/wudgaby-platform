package com.wudgaby.platform.flowable.helper.listener;

import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "FLOW_LOG")
@Component("multiInstanceTaskHandler")
public class MultiInstanceTaskHandler implements ExecutionListener {

    @Autowired
    private TaskService taskService;
    @Autowired
    private IdmIdentityService idmIdentityService;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        log.info("多实例事件监听类开始执行");
        FlowElement currentFlowElement = delegateExecution.getCurrentFlowElement();

        //当进入多实例活动时，在任何内部活动执行前，抛出一个启动事件。loopCounter变量还未设置（为null）。
        //进入每个实际执行的活动时，抛出一个启动事件。loopCounter变量已经设置。

        //离开每个实际执行的活动后，抛出一个结束事件。loopCounter变量已经设置。
        //多实例活动整体完成后，抛出一个结束事件。loopCounter变量未设置。
        if(delegateExecution.getVariable("loopCounter") != null) {
            return;
        }

        if(currentFlowElement instanceof UserTask){
            UserTask userTask = (UserTask) currentFlowElement;
            List<String> candidateUsers = userTask.getCandidateUsers();
            List<String> candidateGroups = userTask.getCandidateGroups();

            //加载角色用户
            if(CollectionUtils.isNotEmpty(candidateGroups)){
                List<User> userList = idmIdentityService.createUserQuery().memberOfGroups(candidateGroups).list();
                if(CollectionUtils.isNotEmpty(userList)){
                    candidateUsers = userList.stream().map(User::getId).collect(Collectors.toList());
                }
            }

            if(CollectionUtils.isEmpty(candidateUsers)){
                //跳过该节点
                userTask.setSkipExpression("${1==1}");
            }

            //设为本地变量(节点所有) usertask在数据库中还未实例化
            //taskService.setVariableLocal(userTask.getId(), "assigneeList", CollectionUtils.isEmpty(candidateUsers) ? usersForRole : candidateUsers);
            delegateExecution.setVariable(userTask.getId() + FlowConstant.VAR_ASSIGNEE_LIST_KEY, candidateUsers);
        }
    }
}
