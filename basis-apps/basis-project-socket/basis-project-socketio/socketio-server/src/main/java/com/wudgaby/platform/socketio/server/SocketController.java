package com.wudgaby.platform.socketio.server;

import com.wudgaby.platform.core.result.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/2/22 0022 9:41
 * @desc :
 */
@RestController
public class SocketController {

    @GetMapping("/push")
    public ApiResult socketPushTest(@RequestParam String id, @RequestParam String message){
        NettySocketUtil.send(id, message);
        return ApiResult.success();
    }
}
