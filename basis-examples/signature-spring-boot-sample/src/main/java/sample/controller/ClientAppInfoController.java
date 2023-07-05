package sample.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.openapi.annotations.AccessTokenVerifier;
import com.wudgaby.platform.openapi.entity.ClientAppInfo;
import com.wudgaby.platform.openapi.utils.OpenAppUtils;
import com.wudgaby.redis.api.RedisSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sample.service.ClientAppInfoService;

import java.time.Duration;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-12-01
 */
@Api(tags = "开发者账号管理")
@RestController
@RequestMapping("/clientAppInfos")
public class ClientAppInfoController {
    @Autowired
    private ClientAppInfoService clientAppInfoService;
    @Autowired
    private RedisSupport redisSupport;

    @GetMapping
    @ApiOperation("列表")
    @AccessTokenVerifier
    public ApiResult list(){
        return ApiResult.success(clientAppInfoService.list());
    }

    @PostMapping
    @ApiOperation("开账号")
    public ApiResult add(@RequestParam String appName){
        ClientAppInfo appInfo = new ClientAppInfo();
        appInfo.setAppName(appName);
        appInfo.setAppId(OpenAppUtils.buildAppId(appName));
        appInfo.setAppSecret(OpenAppUtils.buildAppSecret(appInfo.getAppId(), "demoPriKey"));
        appInfo.setStatus(true);

        clientAppInfoService.save(appInfo);
        return ApiResult.success(appInfo);
    }

    @GetMapping("/token")
    @ApiOperation("appSecret换token")
    public ApiResult token(@RequestParam String appId, @RequestParam String appSecret){
        ClientAppInfo clientAppInfo = clientAppInfoService.getOne(Wrappers.<ClientAppInfo>lambdaQuery()
                .eq(ClientAppInfo::getAppId, appId)
                .eq(ClientAppInfo::getAppSecret, appSecret)
            );

        if(clientAppInfo == null){
            return ApiResult.failure("请开发者确认AppSecret的正确性");
        }

        if (clientAppInfo.getStatus() == false){
            return ApiResult.failure("已被禁用");
        }

        //参考微信 可以加入ip白名单过滤


        //生成accessToken 2天有效期
        Map<String, Object> tokenMap = Maps.newHashMap();
        String accessToken = DigestUtils.md5Hex(appId + System.nanoTime());
        long expiresIn = Duration.ofDays(2).getSeconds();
        tokenMap.put("access_token", accessToken);
        tokenMap.put("expires_in", expiresIn);

        redisSupport.set(accessToken, accessToken, expiresIn);
        return ApiResult.success(tokenMap);
    }
}
