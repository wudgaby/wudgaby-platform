package com.wudgaby.platform.flowable.helper.listener;

import com.wudgaby.platform.flowable.helper.entity.FlowConfig;
import com.wudgaby.platform.flowable.helper.enums.CommentTypeEnum;
import com.wudgaby.platform.flowable.helper.service.FlowableCommentService;
import com.wudgaby.platform.flowable.helper.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName : SkipRoleTaskListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/22 19:19
 * @Desc :   TODO
 */
@Slf4j(topic = "FLOW_LOG")
@Component("taskNobodySkipListener")
public class TaskNobodySkipListener implements TaskListener {

    @Autowired
    private TaskService taskService;
    @Autowired
    private FlowableCommentService flowableCommentService;

    @Override
    public void notify(DelegateTask delegateTask) {
        FlowConfig flowConfig = delegateTask.getVariable("flowConfig", FlowConfig.class);

        Set<IdentityLink> identityLinkSet = new HashSet<>(((TaskEntityImpl)delegateTask).getIdentityLinks());
        if(CollectionUtils.isEmpty(identityLinkSet)){
            if(flowConfig.getNobodySkip()){
                taskService.complete(delegateTask.getId());
                flowableCommentService.addComment(new CommentVo(delegateTask.getId(), "", delegateTask.getProcessInstanceId(), CommentTypeEnum.NOBODY_SKIP.name(), "无人处理跳过审批"));
            }else{
                delegateTask.setAssignee(flowConfig.getDefaultApprover());
            }
        }
    }
}
