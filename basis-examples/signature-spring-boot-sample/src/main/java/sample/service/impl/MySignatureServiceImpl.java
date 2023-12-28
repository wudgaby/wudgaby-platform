package sample.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.openapi.entity.ClientAppInfo;
import com.wudgaby.sign.starter.service.SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import sample.service.ClientAppInfoService;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/12/1 16:28
 * @Desc :   
 */
public class MySignatureServiceImpl implements SignatureService {
    @Autowired
    private ClientAppInfoService clientAppInfoService;

    @Override
    public String getAppSecret(String appId) {
        return clientAppInfoService.getOne(Wrappers.<ClientAppInfo>lambdaQuery().eq(ClientAppInfo::getAppId, appId)).getAppSecret();
    }
}
