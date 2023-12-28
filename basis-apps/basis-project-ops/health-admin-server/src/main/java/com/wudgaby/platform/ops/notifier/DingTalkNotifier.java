package com.wudgaby.platform.ops.notifier;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/8/8 9:25
 * @Desc :   TODO
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class DingTalkNotifier extends AbstractStatusChangeNotifier{
    private static final String DEFAULT_MESSAGE = "*#{instance.registration.name}* (#{instance.id}) is *#{event.statusInfo.status}*";

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private RestTemplate restTemplate = new RestTemplate();

    private String webhookUrl;
    private String[] atMobiles;
    private String msgtype = "markdown";
    private String title = "服务健康告警";
    private Expression message;

    public DingTalkNotifier(InstanceRepository repository) {
        super(repository);
        this.message = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> restTemplate.postForEntity(webhookUrl, createMessage(event, instance),Void.class));
    }

    private HttpEntity<Map<String, Object>> createMessage(InstanceEvent event, Instance instance) {
        Map<String, Object> messageJson = new HashMap<>(8);
        HashMap<String, String> params = new HashMap<>(8);

        String statusMessage  = getDownStatusMessage(instance);
        String serviceUrl = "*" + instance.getRegistration().getServiceUrl() + "*";
        String message = "### 【" +title+ "】 \n" +  this.getMessage(event, instance) + "<br/>\n" + serviceUrl + "<br/>\n" + statusMessage;

        params.put("text", message);
        params.put("title", this.title);
        messageJson.put("msgtype", this.msgtype);
        messageJson.put(this.msgtype, params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(messageJson, headers);
    }

    private String getDownStatusMessage(Instance instance){
        Map<String, Object> statusMap = instance.getStatusInfo().getDetails();
        if(MapUtils.isEmpty(statusMap)){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Object> entry : statusMap.entrySet()){
            if(entry.getValue() instanceof Map){
                Map<String, Object> valMap = (Map<String, Object>) entry.getValue();
                if(StringUtils.equalsIgnoreCase(Objects.toString(valMap.get("status")), "DOWN")){
                    sb.append("- ").append(entry.getKey()).append(" is DOWN").append("<br/>\n");
                }
            }
        }
        return sb.toString();
    }

    private String getMessage(InstanceEvent event, Instance instance) {
        Map<String, Object> root = new HashMap<>(8);
        root.put("event", event);
        root.put("instance", instance);
        root.put("lastStatus", getLastStatus(event.getInstance()));
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        return message.getValue(context, String.class);
    }
}
