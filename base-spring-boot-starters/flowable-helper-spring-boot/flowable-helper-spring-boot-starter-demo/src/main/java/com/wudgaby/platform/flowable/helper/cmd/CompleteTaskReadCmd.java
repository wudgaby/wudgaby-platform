package com.wudgaby.platform.flowable.helper.cmd;

import com.wudgaby.platform.flowable.helper.consts.FlowableConst;
import com.wudgaby.platform.flowable.helper.enums.CommentTypeEnum;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cmd.AddCommentCmd;
import org.flowable.engine.impl.cmd.NeedsActiveTaskCmd;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.TaskHelper;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

/**
 * @author 庄金明
 */
public class CompleteTaskReadCmd extends NeedsActiveTaskCmd<Void> {

    private static final long serialVersionUID = 1L;

    private String userId;

    public CompleteTaskReadCmd(String taskId, String userId) {
        super(taskId);
        this.userId = userId;
    }

    @Override
    protected Void execute(CommandContext commandContext, TaskEntity task) {
        if (userId == null || userId.length() == 0) {
            throw new FlowableIllegalArgumentException("userId is null or empty");
        }
        if (!FlowableConst.CATEGORY_TO_READ.equals(task.getCategory())) {
            throw new FlowableException("task category must be 'toRead'");
        }
        if (!userId.equals(task.getOwner()) && !userId.equals(task.getAssignee())) {
            throw new FlowableException("User does not have permission");
        }
        CommandContextUtil.getProcessEngineConfiguration().getCommandExecutor().execute(
                new AddCommentCmd(taskId, task.getProcessInstanceId(), CommentTypeEnum.YY.toString(), "已阅！"));
        //TaskHelper.completeTask(task, null, null, false, commandContext);
        TaskHelper.completeTask(task, null, null, null, null, commandContext);
        return null;
    }

    @Override
    protected String getSuspendedTaskException() {
        return "Cannot complete a suspended task";
    }

}
