package com.wudgaby.sign.starter.aspect;

import com.wudgaby.redis.api.RedisSupport;
import com.wudgaby.sign.api.SignConst;
import com.wudgaby.sign.api.SignatureException;
import com.wudgaby.sign.api.SignatureHeaders;
import com.wudgaby.sign.api.SignatureUtils;
import com.wudgaby.sign.api.SignatureVo;
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
            throw new RuntimeException("缺少Header[" +SignatureHeaders.HEADER_APP_KEY+ "]信息");
        }
        if (StringUtils.isBlank(reqSign)) {
            throw new RuntimeException("缺少Header[" +SignatureHeaders.HEADER_SIGNATURE+ "]信息");
        }

        String appSecret = signatureService.getAppSecret(appKey);

        if(StringUtils.isBlank(appSecret)){
            throw new SignatureException("无效的appSecret");
        }

        SignatureVo signatureVo = SignatureUtils.buildSignatureVo(request, appKey, appSecret);

        checkSignatureHeader(signatureVo.getSignatureHeaders());

        String serverSign;
        try {
            serverSign = SignatureUtils.sign(signatureVo);
        } catch (Exception e) {
            throw new SignatureException("验签出错...", e);
        }
        if (!serverSign.equals(reqSign)) {
            log.error("签名不一致. user: {} - server: {}", reqSign, serverSign);
            throw new SignatureException("签名不一致...");
        }
    }

    private void checkSignatureHeader(SignatureHeaders signatureHeaders){
        if (StringUtils.isBlank(signatureHeaders.getAppSecret())) {
            log.error("未找到appKey对应的appSecret, appKey= {}", signatureHeaders.getAppKey());
            throw new SignatureException("无效的appKey");
        }

        //其他合法性校验
        long now = System.currentTimeMillis();
        long requestTimestamp = Long.parseLong(signatureHeaders.getTimestamp());
        if ((now - requestTimestamp) > SignConst.EXPIRE_TIME) {
            String errMsg = "无效请求,请求时间超过规定范围.";
            log.error(errMsg);
            throw new SignatureException(errMsg);
        }

        String nonceKey = SignConst.RESUBMIT_KEY + signatureHeaders.getNonce();
        String nonce = signatureHeaders.getNonce();
        if (!redisSupport.hasKey(nonceKey)) {
            redisSupport.set(nonceKey, nonce, SignConst.RESUBMIT_DURATION);
        } else {
            String errMsg = "重放请求, nonce=" + nonce;
            log.error(errMsg);
            throw new SignatureException(errMsg);
        }
    }
}