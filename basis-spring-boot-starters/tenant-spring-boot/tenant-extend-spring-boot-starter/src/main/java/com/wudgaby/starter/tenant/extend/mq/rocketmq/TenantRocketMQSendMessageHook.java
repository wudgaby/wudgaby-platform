package com.wudgaby.starter.tenant.extend.mq.rocketmq;

import com.wudgaby.starter.tenant.context.TenantContextHolder;
import com.wudgaby.starter.tenant.util.WebTenantUtils;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

/**
 * RocketMQ 消息队列的多租户 {@link SendMessageHook} 实现类
 *
 * Producer 发送消息时，将 {@link TenantContextHolder} 租户编号，添加到消息的 Header 中
 *
 * @author 芋道源码
 */
public class TenantRocketMQSendMessageHook implements SendMessageHook {

    @Override
    public String hookName() {
        return getClass().getSimpleName();
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return;
        }
        sendMessageContext.getMessage().putUserProperty(WebTenantUtils.HEADER_TENANT_ID, tenantId.toString());
    }

    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
    }

}
