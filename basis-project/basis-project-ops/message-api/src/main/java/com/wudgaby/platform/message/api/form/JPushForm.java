package com.wudgaby.platform.message.api.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : JPushForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/18 10:57
 * @Desc :   TODO
 */
@Data
@Accessors(chain = true)
@ApiModel("极光推送表单")
public class JPushForm {
    /**
     * android专用. 可选, 通知标题,如果指定了，则通知里原来展示 App名称的地方，将展示成这个字段。
     */
    @ApiModelProperty(value = "标题", required = false)
    private String title;

    /**
     * 通知内容, 内容可以为空字符串，则表示不展示到通知栏
     */
    @ApiModelProperty(value = "消息内容(为了单行显示全，尽量保持在22个汉字以下)", required = true)
    @NotBlank(message = "消息内容不能为空")
    private String content;

    @ApiModelProperty(value = "需接收的用户别名数组（为空则所有用户都推送）", required = false)
    private List<String> aliasList;

    @ApiModelProperty(value = "附加信息", required = false)
    private Map<String, String> extrasMap;
}
