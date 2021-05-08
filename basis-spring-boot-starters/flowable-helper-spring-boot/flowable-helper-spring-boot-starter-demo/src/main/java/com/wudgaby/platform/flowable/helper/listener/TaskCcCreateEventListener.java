package com.wudgaby.platform.flowable.helper.listener;

import com.google.common.collect.Sets;
import com.wudgaby.platform.flowable.helper.cmd.AddCcIdentityLinkCmd;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.service.FlowableTaskService;
import com.wudgaby.platform.flowable.helper.vo.CcToVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ManagementService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j(topic = "FLOW_LOG")
@Component("taskCcCreateEventListener")
public class TaskCcCreateEventListener implements TaskListener {

    @Autowired
    private ManagementService managementService;
    @Autowired
    private FlowableTaskService flowableTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IdmIdentityService idmIdentityService;

    @Override
    public void notify(DelegateTask delegateTask) {
        //Set<IdentityLink> identityLinkSet = delegateTask.getCandidates();
        //delegateTask.setAssignee("");

        Set<IdentityLink> identityLinkSet = new HashSet<>(((TaskEntityImpl)delegateTask).getIdentityLinks());

        List<String> addCcList = delegateTask.getVariable(delegateTask.getTaskDefinitionKey() + FlowConstant.VAR_ASSIGNEE_LIST_KEY, List.class);

        Set<CcToVo> ccToVoSet = Sets.newHashSet();
        identityLinkSet.forEach(id -> ccToVoSet.add(new CcToVo(id.getUserId(), "")));
        addCcList.forEach(id -> ccToVoSet.add(new CcToVo(id, "")));

        /*List<User> userList = idmIdentityService.createUserQuery().userIds(Lists.newArrayList(identityLinkSet)).list();
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.stream().forEach(id -> ccToVoSet.add(new CcToVo(id.getId(), id.getDisplayName())));
        }*/

        managementService.executeCommand(new AddCcIdentityLinkCmd(delegateTask.getProcessInstanceId(), delegateTask.getId(), "", ccToVoSet));

        taskService.complete(delegateTask.getId());
        /*if(FlowableConst.CC.equals(delegateTask.getCategory())){
            taskService.complete(delegateTask.getId());
        }*/
    }

}
