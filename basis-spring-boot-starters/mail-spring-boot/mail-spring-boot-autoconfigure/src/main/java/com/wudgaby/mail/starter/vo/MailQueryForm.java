package com.wudgaby.mail.starter.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/3/31 21:00
 * @Desc :
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class MailQueryForm{
    @NonNull
    private MailAccount mailAccount;

    private String messageId;
    private String from;
    //private Flags.Flag mailFlag;
    private Integer receiveNum;
    private Boolean flagSeen;
    private Boolean acceptContent;
    /**
     * 已读
     */
    private Boolean seen;
    private Boolean acceptTextContent = true;
    private Boolean acceptHtmlContent;
    private List<String> folders;
    private Boolean eachProtocol;
}
