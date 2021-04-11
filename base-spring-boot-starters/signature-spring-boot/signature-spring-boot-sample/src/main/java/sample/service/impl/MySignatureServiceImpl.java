package sample.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.sign.starter.service.SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import sample.model.entity.ClientAppInfo;
import sample.service.ClientAppInfoService;

/**
 * @ClassName : MySignatureServiceImpl
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/1 16:28
 * @Desc :   TODO
 */
public class MySignatureServiceImpl implements SignatureService {
    @Autowired
    private ClientAppInfoService clientAppInfoService;

    @Override
    public String getAppSecret(String appId) {
        return clientAppInfoService.getOne(Wrappers.<ClientAppInfo>lambdaQuery().eq(ClientAppInfo::getAppId, appId)).getAppSecret();
    }
}
