package com.wudgaby.platform.flowable.helper.vo;

import com.wudgaby.platform.flowable.helper.enums.CommentTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author: Bruce.liu
 * @Since:19:24 2019/11/24
 * 爱拼才会赢 2019 ~ 2030 版权所有
 */
@Data
public class CommentVo implements Serializable {
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 任务id
     */
    protected String taskId;
    /**
     * 添加人
     */
    protected String userId;
    /**
     * 用户的名称
     */
    protected String userName;
    /**
     * 用户的头像链接
     */
    protected String userUrl;
    /**
     * 流程实例id
     */
    protected String processInstanceId;
    /**
     * 意见信息
     */
    protected String message;
    /**
     * 时间
     */
    protected Date time;
    /**
     *
      */
    private String type;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务key
     */
    private String taskDefkey;

    /**
     * 评论全信息
     */
    private String fullMsg;
    public CommentVo(){}
    public CommentVo(String userId, String processInstanceId, String type, String message) {
        this.userId = userId;
        this.processInstanceId = processInstanceId;
        this.message = message;
        this.type = type;
    }
    public CommentVo(String taskId, String userId, String processInstanceId, String type, String message) {
        this.taskId = taskId;
        this.userId = userId;
        this.processInstanceId = processInstanceId;
        this.message = message;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeName = CommentTypeEnum.getEnumMsgByType(type);
    }

}
