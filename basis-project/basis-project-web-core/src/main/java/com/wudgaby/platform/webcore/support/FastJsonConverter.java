package com.wudgaby.platform.webcore.support;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.util.TypeUtils;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.LocalDateTimeUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @ClassName : FastJsonConverter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/26/026 23:30
 * @Desc :
 */
@Configuration
@ConditionalOnClass(FastJsonHttpMessageConverter.class)
@ComponentScan(value = {"com.alibaba.fastjson.support.spring"})
public class FastJsonConverter {
    private static final SerializerFeature[] FEATURES = new SerializerFeature[]{
            //SerializerFeature.WriteNullBooleanAsFalse,
            //SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteBigDecimalAsPlain,
            SerializerFeature.DisableCircularReferenceDetect,
            //SerializerFeature.WriteNonStringValueAsString,
            //SerializerFeature.WriteClassName,
            //SerializerFeature.BrowserCompatible,
            //SerializerFeature.PrettyFormat,
    };

    /**
     * 处理日期类型为空的情况
     *
     * @return
     */
    public ValueFilter dateFilter() {
        ValueFilter dateFilter = (Object obj, String name, Object val) -> {
            try {
                Class clazz = obj.getClass().getDeclaredField(name).getType();
                boolean isDateType = Date.class.isAssignableFrom(clazz) ||
                        LocalDate.class.isAssignableFrom(clazz) ||
                        LocalTime.class.isAssignableFrom(clazz) ||
                        LocalDateTime.class.isAssignableFrom(clazz);
                if (val == null && obj != null && isDateType) {
                    return "";
                }
            } catch (Exception e) {
            }
            return val;
        };
        return dateFilter;
    }


    private static final List<Class<?>> WRAPPER = Arrays.asList(Byte.class, Short.class,
                                                    Integer.class, Long.class, Float.class, Double.class, Character.class,
                                                    Boolean.class, String.class);

    private static final List<Class<?>> BASE_DATA_TYPE = Arrays.asList(byte.class, short.class,
                                                        int.class, long.class, float.class, double.class, char.class,
                                                        boolean.class);

    private static final List<Class<?>> CollectionAndMap = Arrays.asList(Collection.class, List.class, Set.class, Array.class);

    /**
     * 处理object类型为空的情况
     *
     * @return
     */
    public ValueFilter objectFilter() {
        return (Object obj, String name, Object val) -> {
            try {
                Class clazz = obj.getClass().getDeclaredField(name).getType();
                boolean isWrapper = WRAPPER.contains(clazz);
                boolean isBaseType = BASE_DATA_TYPE.contains(clazz);
                boolean isCollectionAndMap = CollectionAndMap.contains(clazz);
                boolean isEnum = clazz.isEnum();

                if(val != null){
                    return val;
                }

                //包装类/基础数据类型/集合map
                if(clazz.isPrimitive() || isWrapper || isBaseType || isCollectionAndMap || isEnum){
                    return val;
                }

                return ApiResult.EMPTY;
            } catch (Exception e) {
            }
            return val;
        };
    }

    @Bean
    public HttpMessageConverters customConverters() {
        // 定义一个转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(FEATURES);
        fastJsonConfig.setDateFormat(LocalDateTimeUtil.COMMON_DATETIME);
        // 处理中文乱码问题
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);

        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        //添加序列化过滤器, objectFilter()
        fastJsonConfig.setSerializeFilters(dateFilter(), objectFilter(), new MySensitiveContextValueFilter());

        // 处理首字母大小写问题
        TypeUtils.compatibleWithJavaBean = true;
        // 在转换器中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        // 将转换器添加到converters中
        return new HttpMessageConverters(fastConverter);
    }
}