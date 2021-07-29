package com.wudgaby.sign.starter.aspect;

import com.wudgaby.redis.api.RedisSupport;
import com.wudgaby.sign.api.*;
import com.wudgaby.sign.starter.service.SignatureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName : SignatureAspect
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/17 0:36
 * @Desc :
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class SignatureAspect {
    public final SignatureService signatureService;
    public final RedisSupport redisSupport;

    @Around("@annotation(com.wudgaby.sign.api.Signature) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping))"
    )
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        this.checkSign();
        return pjp.proceed();
    }

    private void checkSign(){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String appKey = request.getHeader(SignatureHeaders.HEADER_APP_KEY);
        String reqSign = request.getHeader(SignatureHeaders.HEADER_SIGNATURE);

        if (StringUtils.isBlank(appKey)) {
            throw new SignatureException("缺少Header[" +SignatureHeaders.HEADER_APP_KEY+ "]信息");
        }
        if (StringUtils.isBlank(reqSign)) {
            throw new SignatureException("缺少Header[" +SignatureHeaders.HEADER_SIGNATURE+ "]信息");
        }

        /**
         * 获取秘钥
         */
        String appSecret = signatureService.getAppSecret(appKey);

        if(StringUtils.isBlank(appSecret)){
            throw new SignatureException("无效的appSecret");
        }

        SignatureVo signatureVo = SignatureUtils.buildSignatureVo(request, appKey, appSecret);

        //校验参数
        SignatureUtils.checkSignatureHeader(signatureVo.getSignatureHeaders());
        checkNonce(signatureVo.getSignatureHeaders().getNonce());

        String serverSign;
        try {
            serverSign = SignatureUtils.sign(signatureVo);
        } catch (Exception e) {
            throw new SignatureException("验签失败...", e);
        }
        if (!serverSign.equals(reqSign)) {
            log.error("签名不一致. user: {} - server: {}", reqSign, serverSign);
            throw new SignatureException("签名不一致! 服务签名结果:" + serverSign);
        }
    }

    private void checkNonce(String nonce){
        String nonceKey = SignConst.RESUBMIT_KEY + nonce;
        if (!redisSupport.hasKey(nonceKey)) {
            redisSupport.set(nonceKey, nonce, SignConst.RESUBMIT_DURATION);
        } else {
            String errMsg = "重放请求, nonce=" + nonce;
            log.error(errMsg);
            throw new SignatureException(errMsg);
        }
    }
}