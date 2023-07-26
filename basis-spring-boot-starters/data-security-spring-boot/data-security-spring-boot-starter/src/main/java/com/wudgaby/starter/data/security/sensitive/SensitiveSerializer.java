package com.wudgaby.starter.data.security.sensitive;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveFieldSerializer;
import com.wudgaby.starter.data.security.sensitive.desensitize.DesensitizeHandler;
import com.wudgaby.starter.data.security.sensitive.desensitize.DesensitizeHandlerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/26 0026 15:39
 * @desc :
 */
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private static DesensitizeHandler desensitizeHandler;
    private String type;

    public SensitiveSerializer() {}

    public SensitiveSerializer(String type) {
        this.type = type;
    }

    public static void setDesensitizeHandler(DesensitizeHandler desensitizeHandler) {
        SensitiveSerializer.desensitizeHandler = desensitizeHandler;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            if (Objects.equals(property.getType().getRawClass(), String.class)) {
                SensitiveFieldSerializer sensitive = property.getAnnotation(SensitiveFieldSerializer.class);
                if (sensitive == null) {
                    sensitive = property.getContextAnnotation(SensitiveFieldSerializer.class);
                }
                if (sensitive != null) {
                    return new SensitiveSerializer(sensitive.value());
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(null == desensitizeHandler){
            desensitizeHandler = DesensitizeHandlerFactory.get(type);
            if (null == desensitizeHandler){
                throw new RuntimeException(StrUtil.format("无效类型 {} 序列化处理", type));
            }
        }

        gen.writeObject(desensitizeHandler.handle(value));
    }
}
