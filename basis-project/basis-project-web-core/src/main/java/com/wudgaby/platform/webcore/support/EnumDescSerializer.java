package com.wudgaby.platform.webcore.support;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @ClassName : EnumDescSerializer
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/10/14 15:29
 * @Desc :
 */
@Slf4j
public class EnumDescSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = jsonSerializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        //原来的值code
        if(object.getClass().isEnum()){
            try {
                Method targetDescMethod = object.getClass().getMethod("getDesc");
                if(targetDescMethod != null){
                    out.writeString(String.valueOf(targetDescMethod.invoke(object)));
                    return;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
            }
        }
        out.writeString(object.toString());
    }
}
