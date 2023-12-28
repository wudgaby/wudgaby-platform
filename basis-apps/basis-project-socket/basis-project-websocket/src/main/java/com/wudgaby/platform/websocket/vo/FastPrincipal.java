package com.wudgaby.platform.websocket.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.Principal;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/26 16:20
 * @Desc :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FastPrincipal implements Principal, Serializable {
    private String name;
}
