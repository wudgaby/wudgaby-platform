package com.wudgaby.platform.message.service;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.NettyHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wudgaby.platform.message.api.form.JPushForm;
import com.wudgaby.platform.message.config.properties.JPushProperties;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName : JpushService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/18 10:52
 * @Desc :
 */
@Slf4j
@Service
public class JPushService {
    @Autowired
    private JPushClient jPushClient;
    @Autowired
    private JPushProperties jPushProperties;

    /**
     * 同步原生消息推送
     * @param jPushForm
     * @return
     */
    public boolean sendPush(JPushForm jPushForm){
        PushPayload pushPayload = buildPushPayload(jPushForm);
        return pushMessage(pushPayload);
    }

    /**
     * 异步原生消息推送
     * @param jPushForm
     */
    public void asyncSendPush(JPushForm jPushForm){
        PushPayload pushPayload = buildPushPayload(jPushForm);
        asyncPushMessage(pushPayload);
    }

    /**
     * 同步自定义消息推送
     * @param jPushForm
     * @return
     */
    public boolean sendCustomPush(JPushForm jPushForm){
        PushPayload pushPayload = buildCustomPushPayload(jPushForm);
        return pushMessage(pushPayload);
    }

    /**
     * 异步自定义消息推送
     * @param jPushForm
     */
    public void asyncSendCustomPush(JPushForm jPushForm){
        PushPayload pushPayload = buildCustomPushPayload(jPushForm);
        asyncPushMessage(pushPayload);
    }

    private PushPayload buildCustomPushPayload(JPushForm jPushForm) {
        return buildCustomPushPayload(jPushForm.getTitle(), jPushForm.getContent(), jPushForm.getExtrasMap(), jPushForm.getAliasList());
    }

    /**
     * jpush的自定义消息
     * @param title
     * @param content
     * @param extrasMap
     * @param aliasList
     * @return
     */
    private PushPayload buildCustomPushPayload(String title, String content, Map<String, String> extrasMap, List<String> aliasList) {
        //过滤空字符串
        List<String> newAliasList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(aliasList)){
            newAliasList = aliasList.stream().filter(alias -> StringUtils.isNotBlank(alias)).collect(Collectors.toList());
        }
        return PushPayload.newBuilder().setPlatform(Platform.android_ios())
                .setAudience(CollectionUtils.isEmpty(newAliasList) ? Audience.all() : Audience.alias(newAliasList))
                .setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(extrasMap).build())
                .build();
    }

    private PushPayload buildPushPayload(JPushForm jPushForm) {
       return buildPushPayload(jPushForm.getTitle(), jPushForm.getContent(), jPushForm.getExtrasMap(), jPushForm.getAliasList());
    }

    /**
     * 原生消息
     * @param title
     * @param content
     * @param extrasMap
     * @param aliasList
     * @return
     */
    private PushPayload buildPushPayload(String title, String content, Map<String, String> extrasMap, List<String> aliasList) {
        if (extrasMap == null || extrasMap.isEmpty()) {
            extrasMap = Maps.newHashMap();
        }

        //过滤空字符串
        List<String> newAliasList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(aliasList)){
            newAliasList = aliasList.stream().filter(alias -> StringUtils.isNotBlank(alias)).collect(Collectors.toList());
        }

        return PushPayload.newBuilder().setPlatform(Platform.android_ios())
                // 别名为空，全员推送；别名不为空，按别名推送
                .setAudience(CollectionUtils.isEmpty(newAliasList) ? Audience.all() : Audience.alias(newAliasList))
                .setNotification(Notification.newBuilder().setAlert(content)
                        .addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).addExtras(extrasMap).build())
                        .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtras(extrasMap).build())
                        .build())
                .build();
    }

    private boolean pushMessage(PushPayload pushPayload){
        try {
            PushResult result = jPushClient.sendPush(pushPayload);
            log.info("Got result - " + result);
            return result.isResultOK();
        } catch (APIConnectionException e) {
            log.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
        return false;
    }

    private void asyncPushMessage(PushPayload pushPayload){
        ClientConfig clientConfig = ClientConfig.getInstance();
        String host = (String) clientConfig.get(ClientConfig.PUSH_HOST_NAME);
        final NettyHttpClient nettyHttpClient = new NettyHttpClient(ServiceHelper.getBasicAuthorization(jPushProperties.getAppKey(), jPushProperties.getAppSecure()), null, clientConfig);

        try{
            URI uri = new URI(host + clientConfig.get(ClientConfig.PUSH_PATH));
            nettyHttpClient.sendRequest(HttpMethod.POST, pushPayload.toString(), uri, new NettyHttpClient.BaseCallback() {
                @Override
                public void onSucceed(ResponseWrapper responseWrapper) {
                    if (200 == responseWrapper.responseCode) {
                        log.info("异步极光推送成功");
                    } else {
                        log.info("异步极光推送失败，返回结果: " + responseWrapper.responseContent);
                    }
                }
            });
        }catch(URISyntaxException e){
            log.error(e.getMessage(), e);
        }
    }
}
