package com.wudgaby.platform.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : FastJsonUtil
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/18 15:55
 * @Desc :   TODO
 */
@UtilityClass
public class FastJsonUtil {
    private static final SerializeConfig config = new SerializeConfig();

    static {
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    public static final SerializerFeature[] FEATURES = {SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullStringAsEmpty
    };


    public static String collectToString(Object object) {
        return JSON.toJSONString(object, config, FEATURES);
    }

    public static String toJsonNoFeatures(Object object) {
        return JSON.toJSONString(object, config);
    }

    public static Object toBean(String text) {
        return JSON.parse(text);
    }

    public static <T> T toBean(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    /*public static <T> T toBean(Object object, TypeReference<T> type) {
        return JSON.parseObject(object, type);
    }*/

    public static Map toMap(String json) {
        return JSONObject.parseObject(json, Map.class);
    }
    /**
     *  转换为数组
     * @param text
     * @return
     */
    public static <T> Object[] toArray(String text) {
        return JSON.parseArray(text).toArray();
    }

    /**
     * 转换为List
     * @param text
     * @param clazz
     * @return
     */
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }


    /**
     * 将map转化为string
     * @param m
     * @return
     */
    public static <K, V> String collectToString(Map<K, V> m) {
       return JSONObject.toJSONString(m);
    }

    /**
     * 将collection转化为string
     * @param
     * @return
     */
    public static <T> String collectToString(Collection<T> collection) {
        return JSONObject.toJSONString(collection);
    }
}
