package com.wudgaby.platform.message.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.push.model.v20160801.CancelPushRequest;
import com.aliyuncs.push.model.v20160801.CancelPushResponse;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidResponse;
import com.aliyuncs.push.model.v20160801.PushMessageToiOSRequest;
import com.aliyuncs.push.model.v20160801.PushMessageToiOSResponse;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidResponse;
import com.aliyuncs.push.model.v20160801.PushNoticeToiOSRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToiOSResponse;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.push.model.v20160801.QueryAliasesRequest;
import com.aliyuncs.push.model.v20160801.QueryAliasesResponse;
import com.aliyuncs.push.model.v20160801.QueryDeviceInfoRequest;
import com.aliyuncs.push.model.v20160801.QueryDeviceInfoResponse;
import com.aliyuncs.push.model.v20160801.QueryDevicesByAccountRequest;
import com.aliyuncs.push.model.v20160801.QueryDevicesByAccountResponse;
import com.aliyuncs.utils.ParameterHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.wudgaby.platform.core.constant.SymbolConstant;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.message.api.enmus.AliTargetType;
import com.wudgaby.platform.message.api.form.AliPushForm;
import com.wudgaby.platform.message.config.properties.AliPushProperties;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName : AliPushService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/18 10:52
 * @Desc :
 */
@Slf4j
@Service
public class AliPushService {
    /**
     * 离线消息保存12小时
     */
    private static final long MESSAGE_EXPIRE = 12 * 3600 * 1000;
    @Autowired
    private AliPushProperties aliPushProperties;
    @Autowired
    private DefaultAcsClient defaultAcsClient;

    /**
     * 消息推送
     * @param aliPushForm
     * @return
     */
    public void pushMessageToAndroid(AliPushForm aliPushForm) throws Exception{
        PushMessageToAndroidRequest androidRequest = new PushMessageToAndroidRequest();
        //安全性比较高的内容建议使用HTTPS
        androidRequest.setSysProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        androidRequest.setSysMethod(MethodType.POST);
        androidRequest.setAppKey(aliPushProperties.getAppKey());
        androidRequest.setTarget(aliPushForm.getTargetType().name());
        androidRequest.setTargetValue(Joiner.on(SymbolConstant.COMMA).skipNulls().join(aliPushForm.getTargetList()));
        androidRequest.setTitle(aliPushForm.getTitle());
        androidRequest.setBody(aliPushForm.getContent());
        PushMessageToAndroidResponse pushMessageToAndroidResponse = defaultAcsClient.getAcsResponse(androidRequest);
        log.info("发送成功. requestId: {}, messageId: {}", pushMessageToAndroidResponse.getRequestId(), pushMessageToAndroidResponse.getMessageId());
    }

    /**
     * 通知推送
     */
    public void pushNoticeToAndroid(AliPushForm aliPushForm) throws Exception{
        PushNoticeToAndroidRequest androidRequest = new PushNoticeToAndroidRequest();
        //安全性比较高的内容建议使用HTTPS
        androidRequest.setSysProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        androidRequest.setSysMethod(MethodType.POST);
        androidRequest.setAppKey(aliPushProperties.getAppKey());
        androidRequest.setTarget(aliPushForm.getTargetType().name());
        androidRequest.setTargetValue(Joiner.on(SymbolConstant.COMMA).skipNulls().join(aliPushForm.getTargetList()));
        androidRequest.setTitle(aliPushForm.getTitle());
        androidRequest.setBody(aliPushForm.getContent());
        androidRequest.setExtParameters(FastJsonUtil.collectToString(aliPushForm.getExtrasMap()));

        PushNoticeToAndroidResponse pushNoticeToAndroidResponse = defaultAcsClient.getAcsResponse(androidRequest);
        log.info("发送成功. requestId: {}, messageId: {}", pushNoticeToAndroidResponse.getRequestId(), pushNoticeToAndroidResponse.getMessageId());
    }

    /**
     * 推送消息给iOS
     * <p>
     * 参见文档 https://help.aliyun.com/document_detail/48086.html
     */
    public void pushMessageToIOS(AliPushForm aliPushForm) throws Exception {
        PushMessageToiOSRequest iOSRequest = new PushMessageToiOSRequest();
        //安全性比较高的内容建议使用HTTPS
        iOSRequest.setSysProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        iOSRequest.setSysMethod(MethodType.POST);
        iOSRequest.setAppKey(aliPushProperties.getAppKey());
        iOSRequest.setTarget(aliPushForm.getTargetType().name());
        iOSRequest.setTargetValue(Joiner.on(SymbolConstant.COMMA).skipNulls().join(aliPushForm.getTargetList()));
        iOSRequest.setTitle(aliPushForm.getTitle());
        iOSRequest.setBody(aliPushForm.getContent());

        PushMessageToiOSResponse pushMessageToiOSResponse = defaultAcsClient.getAcsResponse(iOSRequest);
        log.info("发送成功. requestId: {}, messageId: {}", pushMessageToiOSResponse.getRequestId(), pushMessageToiOSResponse.getMessageId());
    }

    /**
     * 推送通知给iOS
     * <p>
     * 参见文档 https://help.aliyun.com/document_detail/48088.html
     */
    public void testPushNoticeToIOS(AliPushForm aliPushForm) throws Exception{
        PushNoticeToiOSRequest iOSRequest = new PushNoticeToiOSRequest();
        //安全性比较高的内容建议使用HTTPS
        iOSRequest.setSysProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        iOSRequest.setSysMethod(MethodType.POST);
        iOSRequest.setAppKey(aliPushProperties.getAppKey());
        iOSRequest.setTarget(aliPushForm.getTargetType().name());
        iOSRequest.setTargetValue(Joiner.on(SymbolConstant.COMMA).skipNulls().join(aliPushForm.getTargetList()));
        iOSRequest.setTitle(aliPushForm.getTitle());
        iOSRequest.setBody(aliPushForm.getContent());
        iOSRequest.setExtParameters(FastJsonUtil.collectToString(aliPushForm.getExtrasMap()));

        PushNoticeToiOSResponse pushNoticeToiOSResponse = defaultAcsClient.getAcsResponse(iOSRequest);
        log.info("发送成功. requestId: {}, messageId: {}", pushNoticeToiOSResponse.getRequestId(), pushNoticeToiOSResponse.getMessageId());
    }

    public List<String> queryDeviceByAccount(String account) throws ClientException {
        QueryDevicesByAccountRequest request = new QueryDevicesByAccountRequest();
        request.setAppKey(aliPushProperties.getAppKey());
        request.setAccount(account);

        QueryDevicesByAccountResponse response = defaultAcsClient.getAcsResponse(request);
        return response.getDeviceIds();
    }

    public String queryDeviceInfo(String deviceId) throws ClientException {
        QueryDeviceInfoRequest request = new QueryDeviceInfoRequest();
        request.setAppKey(aliPushProperties.getAppKey());
        request.setDeviceId(deviceId);

        QueryDeviceInfoResponse response = defaultAcsClient.getAcsResponse(request);
        QueryDeviceInfoResponse.DeviceInfo deviceInfo = response.getDeviceInfo();
        return JSON.toJSONString(deviceInfo);
    }

    public List<String> queryAliaseList(String deviceId) throws ClientException {
        QueryAliasesRequest request = new QueryAliasesRequest();
        request.setAppKey(aliPushProperties.getAppKey());
        request.setDeviceId(deviceId);

        List<String> aliasNames = Lists.newArrayList();
        QueryAliasesResponse response = defaultAcsClient.getAcsResponse(request);
        for(QueryAliasesResponse.AliasInfo aliasInfo : response.getAliasInfos()){
            aliasNames.add(aliasInfo.getAliasName());
        }
        return aliasNames;
    }

    /**
     * 推送高级接口
     * <p>
     * 参见文档 https://help.aliyun.com/document_detail/48089.html
     */
    public void advancedPush(AliPushForm aliPushForm){
        log.info("阿里推送参数: {}", aliPushForm);

        PushRequest pushRequest = new PushRequest();
        //安全性比较高的内容建议使用HTTPS
        pushRequest.setSysProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        pushRequest.setSysMethod(MethodType.POST);
        //推送目标
        pushRequest.setAppKey(aliPushProperties.getAppKey());
        //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
        pushRequest.setTarget(aliPushForm.getTargetType().name());
        //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setTargetValue(aliPushForm.getTargetType() == AliTargetType.ALL ?
                                                            aliPushForm.getTargetType().name() :
                                                            Joiner.on(SymbolConstant.COMMA).skipNulls().join(aliPushForm.getTargetList() == null ? Lists.newArrayList() : aliPushForm.getTargetList() ));
        // 消息类型 MESSAGE NOTICE
        pushRequest.setPushType(aliPushForm.getPushType().name());
        // 设备类型 ANDROID iOS ALL.
        pushRequest.setDeviceType(aliPushForm.getDeviceType().name());
        // 消息的标题
        pushRequest.setTitle(aliPushForm.getTitle());
        // 消息的内容
        pushRequest.setBody(aliPushForm.getContent());

        // 推送配置: iOS
        pushRequest.setIOSBadge(5); // iOS应用图标右上角角标
        pushRequest.setIOSSilentNotification(false);//开启静默通知
        pushRequest.setIOSMusic("default"); // iOS通知声音
        pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
        pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
        pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
        pushRequest.setIOSApnsEnv(aliPushProperties.getApnsEnv());//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
        pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
        pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
        pushRequest.setIOSExtParameters(FastJsonUtil.collectToString(aliPushForm.getExtrasMap())); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)

        // 推送配置: Android
        //通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
        //pushRequest.setAndroidNotifyType("BOTH");
        //通知栏自定义样式0-100
        //pushRequest.setAndroidNotificationBarType(50);
        //Android通知在通知栏展示时排列位置的优先级 -2 -1 0 1 2
        //pushRequest.setAndroidNotificationBarPriority(1);
        //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
        pushRequest.setAndroidOpenType("APPLICATION");
        //pushRequest.setAndroidOpenUrl("http://www.aliyun.com"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
        //pushRequest.setAndroidActivity("com.alibaba.push2.demo.XiaoMiPushActivity"); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
        //pushRequest.setAndroidMusic("default"); // Android通知音乐
        //推送类型为消息时设备不在线，则这条推送会使用辅助弹窗功能。默认值为False，仅当PushType=MESSAGE时生效。
        pushRequest.setAndroidRemind(true);
        //设置该参数后启动小米托管弹窗功能, 此处指定通知点击后跳转的Activity（托管弹窗的前提条件：1. 集成小米辅助通道；2. StoreOffline参数设为true）
        pushRequest.setAndroidPopupActivity("x.xx.push.AliPushActivity");
        pushRequest.setAndroidPopupTitle(aliPushForm.getTitle());
        pushRequest.setAndroidPopupBody(aliPushForm.getContent());
        //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
        pushRequest.setAndroidExtParameters(FastJsonUtil.collectToString(aliPushForm.getExtrasMap()));
        pushRequest.setAndroidNotificationChannel("1");
        //推送控制
        //Date pushDate = new Date(System.currentTimeMillis()); // 30秒之间的时间点, 也可以设置成你指定固定时间
        //String pushTime = ParameterHelper.getISO8601Time(pushDate);
        //pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送

        // 12小时后消息失效, 不会再发送
        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + MESSAGE_EXPIRE));
        pushRequest.setExpireTime(expireTime);
        // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
        pushRequest.setStoreOffline(true);

        try {
            PushResponse pushResponse = defaultAcsClient.getAcsResponse(pushRequest);
            log.info("阿里推送成功. requestId: {}, messageId: {}", pushResponse.getRequestId(), pushResponse.getMessageId());
        } catch (ClientException e) {
            log.error("阿里推送失败.", e);
            throw new BusinessException("阿里推送失败.原因: " + e.getErrMsg());
        }
    }

    /**
     * 取消定时推送
     */
    public void cancelPush(Long messageId){
        CancelPushRequest request = new CancelPushRequest();
        request.setAppKey(aliPushProperties.getAppKey());
        request.setMessageId(messageId);
        try {
            CancelPushResponse response = defaultAcsClient.getAcsResponse(request);
            log.info("取消成功. {}" + response);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
        }
    }
}
