package com.wudgaby.platform.core.exception;

import com.wudgaby.platform.core.result.enums.ApiResultCode;
import com.wudgaby.platform.core.result.enums.SystemResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wudgaby
 * @version V1.0
 * @ClassName: BusinessException
 * @Description: 基础业务异常类
 * @date 2018/9/30 11:42
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    private static final ApiResultCode BIZ_FAILURE = SystemResultCode.BIZ_FAILURE;
    protected int errorCode = BIZ_FAILURE.getCode();
    protected Object data;
    protected boolean printLog = true;
    protected boolean sendAlarm = false;

    public BusinessException(){
        this(BIZ_FAILURE, null, true, null);
    }

    public BusinessException(String message){
        this(BIZ_FAILURE.getCode(), message);
    }

    public BusinessException(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable e){
        super(message, e);
    }

    public BusinessException(int errorCode, String message, Throwable e){
        this(message, e);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message, Object data, Throwable e){
        this(errorCode, message, e);
        this.data = data;
    }

    public BusinessException(int errorCode, String message, Object data, boolean printLog, Throwable e){
        this(errorCode, message, data, e);
        this.printLog = printLog;
    }

    public BusinessException(ApiResultCode apiResultCode, Object data, boolean printLog, Throwable e){
        this(apiResultCode.getCode(), apiResultCode.getReason(), data, printLog, e);
    }

    public BusinessException(ApiResultCode apiResultCode, Object data, boolean printLog){
        this(apiResultCode, data, printLog, null);
    }

    public BusinessException(ApiResultCode apiResultCode, Object data){
        this(apiResultCode, data, true, null);
    }

    public BusinessException(ApiResultCode apiResultCode){
        this(apiResultCode, null, true, null);
    }

    public BusinessException(ApiResultCode apiResultCode, boolean printLog){
        this(apiResultCode, null, printLog, null);
    }
}
