package com.wudgaby.apiautomatic.dto;

import com.wudgaby.apiautomatic.enums.ApiStatus;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName : ApiDTO
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/30 15:28
 * @Desc :
 */
@Data
@Accessors(chain = true)
public class ApiDTO {
    /**
     * 接口编号 md5(method + uri)
     */
    private String code;
    /**
     * 接口名称 @ApiOperation中的value()
     */
    private String name;
    /**
     * 接口描述 @ApiOperation中的note()
     */
    private String desc;
    /**
     * 接口方法 RequestMapping GET/POST/PUT/DELETE
     */
    private String method;
    /**
     * 接口方法 className 类名
     */
    private String className;
    /**
     * 接口方法 methodName 方法名
     */
    private String methodName;
    /**
     * 接口地址 @RequestMapping中的value()
     */
    private String uri;
    /**
     * 接口类型 url/button
     */
    private String type;
    /**
     * 是否开放 缺省false
     */
    private Boolean open;
    /**
     * 是否需要验证 缺省false
     */
    private Boolean auth;
    /**
     * status api状态
     */
    private ApiStatus status;
    /**
     * 所属服务 spring.application.name
     */
    private String serviceName;
}
