package com.wudgaby.platform.socketio.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/2/22 0022 9:34
 * @desc :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessage {
    private String userName;
    private String message;

    /*private String type;
    private String content;
    private String messageId;
    private Long userId;
    private Long fromUserId;
    private Long toUserId;*/
}
