package com.wudgaby.platform.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import serialize.CustomJavaTimeModule;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * {@link JacksonUtil}
 * <p>
 *
 * @author zhaoyb1990
 */
public class JacksonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setLocale(Locale.getDefault())
                .setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                .setDateFormat(sdf);
        mapper.registerModule(new CustomJavaTimeModule());
    }

    public static String serialize(Object object){
        return serialize(object,true);
    }

    public static String serialize(Object object, boolean pretty){
        try {
            return pretty ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object) : mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("jackson-serialize-error", e);
        }
    }

    public static byte[] serialize2Bytes(Object object){
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("jackson-serialize-error", e);
        }
    }

    public static <T> T deserialize(byte[] bytes, Class<T> type){
        try {
            return mapper.readValue(bytes, type);
        } catch (Exception e) {
            throw new RuntimeException("jackson-serialize-error", e);
        }
    }

    public static <T> T deserialize(String sequence, Class<T> type){
        try {
            return mapper.readValue(sequence, type);
        } catch (Exception e) {
            throw new RuntimeException("jackson-serialize-error", e);
        }
    }

    public static <T> List<T> deserializeArray(byte[] bytes, Class<T> type){
        try {
            return mapper.readValue(bytes, getCollectionType(ArrayList.class, type));
        } catch (Exception e) {
            throw new RuntimeException("jackson-serialize-error", e);
        }
    }

    public static <T> List<T> deserializeArray(String sequence, Class<T> type){
        try {
            return mapper.readValue(sequence, getCollectionType(ArrayList.class, type));
        } catch (Exception e) {
            throw new RuntimeException("jackson-serialize-error", e);
        }
    }

    public static Object deserialize(byte[] bytes){
        try {
            return mapper.readTree(bytes);
        } catch (Exception e) {
            throw new RuntimeException("jackson-serialize-error", e);
        }
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametrizedType(collectionClass, collectionClass, elementClasses);
    }
}
