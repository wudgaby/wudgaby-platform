package com.wudgaby.platform.twofactorauth.sample.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.ImmutableMap;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.springext.RequestContextHolderSupport;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/1 0001 15:30
 * @desc :
 */
@RestController
@RequestMapping("/mfa")
public class MfaSetupController {

    @Autowired
    private SecretGenerator secretGenerator;

    @Autowired
    private QrDataFactory qrDataFactory;

    @Autowired
    private QrGenerator qrGenerator;

    @Autowired
    private CodeVerifier verifier;

    private String secret;
    private byte[] imageData;

    @GetMapping(value = "/setup", produces = MediaType.IMAGE_PNG_VALUE)
    public void setupDevice() throws QrGenerationException, IOException {
        // Generate and store the secret
        secret = secretGenerator.generate();

        QrData data = qrDataFactory.newBuilder()
                .label("example@example.com")
                .secret(secret)
                .issuer("AppName")
                .build();

        // Generate the QR code image data as a base64 string which
        // can be used in an <img> tag:
        /*String qrCodeImage = Utils.getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType()
        );*/
        OutputStream stream = RequestContextHolderSupport.getResponse().getOutputStream();
        stream.write(qrGenerator.generate(data));
        stream.flush();
    }

    @PostMapping("/verify")
    @ResponseBody
    public String verify(@RequestParam String code) {
        if (verifier.isValidCode(secret, code)) {
            return "验证成功";
        }

        return "验证失败";
    }

    @ApiOperation("8Lib生成")
    @GetMapping(value = "/generator")
    @ApiOperationSupport(order = 8, author = "bird")
    @SneakyThrows
    public ApiResult generator(){
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        QrData qrData = new QrData.Builder()
                .label("test")
                .secret(secret)
                .issuer("95至尊网")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        imageData = generator.generate(qrData);
        String mimeType = generator.getImageMimeType();
        String dataUri = Utils.getDataUriForImage(imageData, mimeType);
        return ApiResult.success().data(ImmutableMap.of("secret",secret, "secretQrCode", qrData.getUri(), "dataUri", dataUri));
    }

    @ApiOperation("9Lib生成二维码")
    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperationSupport(order = 8, author = "bird")
    @SneakyThrows
    public void qrcode(){
        OutputStream stream = RequestContextHolderSupport.getResponse().getOutputStream();
        stream.write(imageData);
        stream.flush();
    }

    @ApiOperation("10Lib校验")
    @GetMapping("/totpLibVerify")
    @ApiOperationSupport(order = 10, author = "bird")
    @SneakyThrows
    public ApiResult totpLibVerify(@RequestParam String code){
        //系统时间
        TimeProvider timeProvider = new SystemTimeProvider();
        //服务器时间
        //TimeProvider timeProvider = new NtpTimeProvider("pool.ntp.org");
        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        //verifier.setTimePeriod(60);
        //verifier.setAllowedTimePeriodDiscrepancy(2);

        boolean successful = verifier.isValidCode(secret, code);
        return ApiResult.success(successful ? "验证成功" : "验证失败");
    }

    @ApiOperation(value = "11Lib恢复", notes = "允许用户在不提供TOTP的情况下访问受MFA保护的帐户，从而绕过MFA进程")
    @GetMapping("/totpLibRecovery")
    @ApiOperationSupport(order = 11, author = "bird")
    @SneakyThrows
    public ApiResult totpLibRecovery(){
        RecoveryCodeGenerator recoveryCodes = new RecoveryCodeGenerator();
        String[] codes = recoveryCodes.generateCodes(5);
        return ApiResult.success().data(codes);
    }
}