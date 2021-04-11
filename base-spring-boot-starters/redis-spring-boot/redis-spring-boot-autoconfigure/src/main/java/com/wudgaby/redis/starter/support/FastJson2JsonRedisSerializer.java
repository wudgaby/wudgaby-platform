package com.wudgaby.redis.starter.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import com.google.common.base.Joiner;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Collection;

/**
 * @ClassName : FastJson2JsonRedisSerializer
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/24 15:18
 * @Desc :   TODO
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    private static ParserConfig defaultRedisConfig = new ParserConfig();
    static { defaultRedisConfig.setAutoTypeSupport(true);}

    private Class<T> clazz;

    //public FastJson2JsonRedisSerializer(){}

    public FastJson2JsonRedisSerializer(Class<T> clazz){
        super();
        this.clazz = clazz;
    }

    public FastJson2JsonRedisSerializer(Class<T> clazz, Collection accepts){
        this(clazz);
        defaultRedisConfig.addAccept(Joiner.on(",").skipNulls().join(accepts));
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(t, SerializerFeature.WriteClassName);
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return (T)JSON.parseObject(new String(bytes, IOUtils.UTF8), clazz, defaultRedisConfig);
        } catch (Exception ex) {
            throw new SerializationException("Could not deserialize: " + ex.getMessage(), ex);
        }
    }
}
