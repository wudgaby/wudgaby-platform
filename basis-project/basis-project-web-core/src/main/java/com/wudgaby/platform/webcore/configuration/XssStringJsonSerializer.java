package com.wudgaby.platform.webcore.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.io.IOException;

/**
 * 针对jackson
 * 未启用
 *   @Bean
 *   @Primary
 *   public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
 *     //解析器
 *     ObjectMapper objectMapper = builder.createXmlMapper(false).build();
 *     //注册xss解析器
 *     SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
 *     xssModule.addSerializer(new XssStringJsonSerializer());
 *     objectMapper.registerModule(xssModule);
 *     //返回
 *     return objectMapper;
 *   }
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {
    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            String encodedValue = HtmlUtils.htmlEscape(value);
            encodedValue = JavaScriptUtils.javaScriptEscape(encodedValue);
            jsonGenerator.writeString(encodedValue);
        }
    }
}