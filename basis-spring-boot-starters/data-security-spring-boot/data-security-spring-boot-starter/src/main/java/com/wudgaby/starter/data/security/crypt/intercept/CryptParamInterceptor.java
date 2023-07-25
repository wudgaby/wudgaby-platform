package com.wudgaby.starter.data.security.crypt.intercept;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoBean;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import com.wudgaby.starter.data.security.crypt.handler.CryptHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author wudgaby
 */
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)
})
@Slf4j
@RequiredArgsConstructor
public class CryptParamInterceptor implements Interceptor {
    /**
     * 是否不修改原有对象
     * true: 原有对象不变
     * false: 返回克隆的对象
     */
    @Setter
    private boolean keepBean;
    private static final String GENERIC_NAME_PREFIX = "param";
    private static final String MAPPED_STATEMENT = "mappedStatement";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(parameterHandler);
        //MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(MAPPED_STATEMENT);

        Object params = metaObject.getValue("parameterObject");
        if (Objects.isNull(params)) {
            return invocation.proceed();
        }

        if (params instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap<Object>) params;
            for (Map.Entry<String, Object> paramObj : paramMap.entrySet()) {
                Object paramValue = paramObj.getValue();
                entityEncrypt(paramValue, metaObject);
            }
        } else if (params instanceof Map) {
            return invocation.proceed();
        } else {
            entityEncrypt(params, metaObject);
        }
        return invocation.proceed();
    }

    /**
     * @param param
     * @throws Exception
     */
    private void entityEncrypt(Object param, MetaObject paramMetaObject){
        CryptoBean cryptoBeanAnnotation = param.getClass().getDeclaredAnnotation(CryptoBean.class);
        if (Objects.isNull(cryptoBeanAnnotation)) {
            return;
        }

        Object modifyParam = param;
        if(BooleanUtil.isTrue(keepBean)) {
            modifyParam = ObjectUtil.cloneByStream(param);
            paramMetaObject.setValue("parameterObject", modifyParam);
        }

        MetaObject metaObject = SystemMetaObject.forObject(modifyParam);
        Arrays.stream(param.getClass().getDeclaredFields())
                .filter(item -> Objects.nonNull(item.getAnnotation(CryptoField.class)) && item.getAnnotation(CryptoField.class).encrypt())
                .forEach(item -> {
                    String fieldName = item.getName();
                    cryptField(fieldName, metaObject, item.getAnnotation(CryptoField.class));
                });

    }

    private void cryptField(String fieldName, MetaObject metaObject, CryptoField cryptoField) {
        Object fieldVal = metaObject.getValue(fieldName);
        Object encryptVal = CryptHandlerFactory.getCryptHandler(fieldVal, cryptoField).encrypt(fieldVal, cryptoField);
        metaObject.setValue(fieldName, encryptVal);
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ParameterHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}