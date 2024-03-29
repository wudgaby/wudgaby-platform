package com.wudgaby.platform.websocket.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/26 21:22
 * @Desc :
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Integer id;
    private String account;
    private String password;
    private String name;
}
