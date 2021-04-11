package com.wudgaby.platform.flowable.helper.service.impl;

import com.wudgaby.platform.flowable.helper.cmd.AddHisCommentCmd;
import com.wudgaby.platform.flowable.helper.enums.CommentTypeEnum;
import com.wudgaby.platform.flowable.helper.mapper.FlowableCommentDao;
import com.wudgaby.platform.flowable.helper.service.FlowableCommentService;
import com.wudgaby.platform.flowable.helper.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : bruce.liu
 * @title: : FlowCommentServiceImpl
 * @projectName : flowable
 * @description: 流程备注service
 * @date : 2019/11/2412:58
 */
@Service
public class FlowableCommentServiceImpl extends BaseProcessService implements FlowableCommentService {

    @Autowired
    private FlowableCommentDao flowableCommentDao;

    @Override
    public void addComment(CommentVo comment) {
        managementService.executeCommand(new AddHisCommentCmd(comment.getTaskId(), comment.getUserId(), comment.getProcessInstanceId(),
                comment.getType(), comment.getMessage()));
    }

    @Override
    public List<CommentVo> getFlowCommentVosByProcessInstanceId(String processInstanceId) {
        List<CommentVo> datas = flowableCommentDao.getFlowCommentVosByProcessInstanceId(processInstanceId);
        datas.forEach(commentVo -> {
            commentVo.setTypeName(CommentTypeEnum.getEnumMsgByType(commentVo.getType()));
        });
        return datas;

        /*List<Comment> commentList = taskService.getProcessInstanceComments(processInstanceId);
        List<CommentVo> datas = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(commentList)){
            commentList.stream().forEach(comment ->
                datas.add(new CommentVo(comment.getTaskId(), comment.getUserId(), comment.getProcessInstanceId(), comment.getType(), comment.getFullMessage()))
            );
        }
        return datas;*/

    }
}
