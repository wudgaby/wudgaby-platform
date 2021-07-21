package com.wudgaby.sign.api;

import cn.hutool.http.body.RequestBody;
import com.google.common.collect.Lists;
import com.wudgaby.platform.utils.ValidatorUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @ClassName : SignatureHelper
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/19 14:47
 * @Desc :
 */
@Slf4j
@UtilityClass
public class SignatureHelper {
    private static String DELIMETER = ",";
    private static String NOT_FOUND = "NOT_FOUND";

    /** * 生成所有注有 SignatureField属性 key=value的 拼接 */
    public static String toSplice(Object object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }
        if (AnnotatedElementUtils.isAnnotated(object.getClass(), Signature.class)) {
            Signature sg = AnnotationUtils.findAnnotation(object.getClass(), Signature.class);
            switch (sg.sort()) {
                case Signature.ALPHA_SORT:
                    return alphaSignature(object);
                case Signature.ORDER_SORT:
                    //return orderSignature(object);
                    return "";
                default:
                    return alphaSignature(object);
            }
        }
        return toString(object);
    }

    private static String alphaSignature(Object object) {
        StringBuilder result = new StringBuilder();
        Map<String, String> map = new TreeMap<>();
        for (Field field : FieldUtils.getAllFields(object.getClass())) {
            if (field.isAnnotationPresent(SignatureField.class)) {
                field.setAccessible(true);
                try {
                    if (AnnotatedElementUtils.isAnnotated(field.getType(), Signature.class)) {
                        if (!Objects.isNull(field.get(object))) {
                            map.put(field.getName(), toSplice(field.get(object)));
                        }
                    } else {
                        SignatureField sgf = field.getAnnotation(SignatureField.class);
                        if (StringUtils.isNotEmpty(sgf.customValue()) || !Objects.isNull(field.get(object))) {
                            map.put(StringUtils.isNotBlank(sgf.customName()) ? sgf.customName() : field.getName()
                                    , StringUtils.isNotEmpty(sgf.customValue()) ? sgf.customValue() : toString(field.get(object)));
                        }
                    }
                } catch (Exception e) {
                    log.error("签名拼接(alphaSignature)异常", e);
                }
            }
        }

        for (Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = iterator.next();
            result.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                result.append(DELIMETER);
            }
        }
        return result.toString();
    }

    /**
     * 针对array, collection, simple property, map做处理
     */
    private static String toString(Object object) {
        Class<?> type = object.getClass();
        if (BeanUtils.isSimpleProperty(type)) {
            return object.toString();
        }
        if (type.isArray()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Array.getLength(object); ++i) {
                sb.append(toSplice(Array.get(object, i)));
            }
            return sb.toString();
        }
        if (ClassUtils.isAssignable(Collection.class, type)) {
            StringBuilder sb = new StringBuilder();
            for (Iterator<?> iterator = ((Collection<?>) object).iterator(); iterator.hasNext(); ) {
                sb.append(toSplice(iterator.next()));
                if (iterator.hasNext()) {
                    sb.append(DELIMETER);
                }
            }
            return sb.toString();
        }
        if (ClassUtils.isAssignable(Map.class, type)) {
            StringBuilder sb = new StringBuilder();
            for (Iterator<? extends Map.Entry<String, ?>> iterator = ((Map<String, ?>) object).entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, ?> entry = iterator.next();
                if (Objects.isNull(entry.getValue())) {
                    continue;
                }
                sb.append(entry.getKey()).append("=").append(toSplice(entry.getValue()));
                if (iterator.hasNext()) {
                    sb.append(DELIMETER);
                }
            }
            return sb.toString();
        }
        return NOT_FOUND;
    }

    private List<String> generateAllSplice(Method method, Object[] args, String headersToSplice) {
        List<String> pathVariables = Lists.newArrayList(), requestParams = Lists.newArrayList();
        String beanParams = StringUtils.EMPTY;
        for (int i = 0; i < method.getParameterCount(); ++i) {
            MethodParameter mp = new MethodParameter(method, i);
            boolean findSignature = false;
            for (Annotation anno : mp.getParameterAnnotations()) {
                if (anno instanceof PathVariable) {
                    if (!Objects.isNull(args[i])) {
                        pathVariables.add(args[i].toString());
                    }
                    findSignature = true;
                } else if (anno instanceof RequestParam) {
                    RequestParam rp = (RequestParam) anno;
                    String name = mp.getParameterName();
                    if (StringUtils.isNotBlank(rp.name())) {
                        name = rp.name();
                    }
                    if (!Objects.isNull(args[i])) {
                        List<String> values = Lists.newArrayList();
                        if (args[i].getClass().isArray()) {
                            //数组
                            for (int j = 0; j < Array.getLength(args[i]); ++j) {
                                values.add(Array.get(args[i], j).toString());
                            }
                        } else if (ClassUtils.isAssignable(Collection.class, args[i].getClass())) {
                            //集合
                            for (Object o : (Collection<?>) args[i]) {
                                values.add(o.toString());
                            }
                        } else {
                            //单个值
                            values.add(args[i].toString());
                        }
                        values.sort(Comparator.naturalOrder());
                        requestParams.add(name + "=" + StringUtils.join(values));
                    }
                    findSignature = true;
                } else if (anno instanceof RequestBody || anno instanceof ModelAttribute) {
                    beanParams = toSplice(args[i]);
                    findSignature = true;
                }

                if (findSignature) {
                    break;
                }
            }
            if (!findSignature) {
                log.info(String.format("签名未识别的注解, method=%s, parameter=%s, annotations=%s", method.getName(), mp.getParameterName(), StringUtils.join(mp.getMethodAnnotations())));
            }
        }
        List<String> toSplices = Lists.newArrayList();
        toSplices.add(headersToSplice);
        toSplices.addAll(pathVariables);
        requestParams.sort(Comparator.naturalOrder());
        toSplices.addAll(requestParams);
        toSplices.add(beanParams);
        return toSplices;
    }

    public static SignatureHeaders generateSignatureHeaders(HttpServletRequest request){
        Map<String, Object> headerMap = Collections.list(request.getHeaderNames())
                .stream()
                .filter(headerName -> SignatureHeaders.HEADER_NAME_SET.contains(headerName))
                .collect(Collectors.toMap(headerName -> headerName.replaceAll("-", "."), headerName -> request.getHeader(headerName)));
        PropertySource propertySource = new MapPropertySource("signatureHeaders", headerMap);
        // 自定义ConfigurationProperty源信息
        ConfigurationPropertySource sources = new MapConfigurationPropertySource(headerMap);
        // 创建Binder绑定类
        Binder binder = new Binder(sources);
        // 绑定属性
        SignatureHeaders signatureHeaders = binder.bind(SignatureHeaders.SIGNATURE_HEADERS_PREFIX.replaceAll("-", "."), Bindable.of(SignatureHeaders.class)).get();

        Optional<String> result = ValidatorUtils.validateEntity(signatureHeaders);
        if (result.isPresent()) {
            throw new SignatureException(result.get());
        }

        return signatureHeaders;
    }

    /*private SignatureHeaders generateSignatureHeaders(Signature signature, HttpServletRequest request) throws Exception {
        Map<String, Object> headerMap = Collections.list(request.getHeaderNames())
                .stream()
                .filter(headerName -> SignatureHeaders.HEADER_NAME_SET.contains(headerName))
                .collect(Collectors.toMap(headerName -> headerName.replaceAll("-", "."), headerName -> request.getHeader(headerName)));
        PropertySource propertySource = new MapPropertySource("signatureHeaders", headerMap);
        // 自定义ConfigurationProperty源信息
        ConfigurationPropertySource sources = new MapConfigurationPropertySource(headerMap);
        // 创建Binder绑定类
        Binder binder = new Binder(sources);
        // 绑定属性
        SignatureHeaders signatureHeaders = binder.bind(SignatureHeaders.SIGNATURE_HEADERS_PREFIX, Bindable.of(SignatureHeaders.class)).get();

        Optional<String> result = ValidatorUtils.validateEntity(signatureHeaders);
        if (result.isPresent()) {
            throw new SignatureException(result.get());
        }

        //从配置中拿到appid对应的appsecret
        String appSecret = limitConstants.getSignatureLimit().get(signatureHeaders.getAppId());
        if (StringUtils.isBlank(appSecret)) {
            log.error("未找到appId对应的appSecret, appId=" + signatureHeaders.getAppId());
            throw new SignatureException("无效appId");
        }

        //其他合法性校验
        Long now = System.currentTimeMillis();
        Long requestTimestamp = Long.parseLong(signatureHeaders.getTimestamp());
        if ((now - requestTimestamp) > SignConst.EXPIRE_TIME) {
            String errMsg = "请求时间超过规定范围时间10分钟, signature=" + signatureHeaders.getSignature();
            log.error(errMsg);
            throw new SignatureException(errMsg);
        }
        String nonce = signatureHeaders.getNonce();
        if (nonce.length() < 10) {
            String errMsg = "随机串nonce长度最少为10位, nonce=" + nonce;
            log.error(errMsg);
            throw new SignatureException(errMsg);
        }
        if (!signature.resubmit()) {
            String existNonce = redisCacheService.getString(nonce);
            if (StringUtils.isBlank(existNonce)) {
                redisCacheService.setString(nonce, nonce);
                redisCacheService.expire(nonce, (int) TimeUnit.MILLISECONDS.toSeconds(RESUBMIT_DURATION));
            } else {
                String errMsg = "不允许重复请求, nonce=" + nonce;
                log.error(errMsg);
                throw new SignatureException("WMH5000", errMsg);
            }
        }
        signatureHeaders.setAppSecret(appSecret);
        return signatureHeaders;
    }*/
}
