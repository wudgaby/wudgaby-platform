package com.wudgaby.starter.data.security.crypt.intercept;

import com.google.common.collect.Maps;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoBean;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import com.wudgaby.starter.data.security.crypt.handler.CryptHandlerFactory;
import com.wudgaby.starter.data.security.util.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author wudgaby
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {java.sql.Statement.class})
})
@Slf4j
public class CryptReadInterceptor implements Interceptor {
    private static final String MAPPED_STATEMENT = "mappedStatement";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final List<Object> results = (List<Object>) invocation.proceed();
        if (results.isEmpty()) {
            return results;
        }

        final ResultSetHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        final MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        final MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(MAPPED_STATEMENT);
        final ResultMap resultMap = mappedStatement.getResultMaps().isEmpty() ? null : mappedStatement.getResultMaps().get(0);

        Object result = results.get(0);
        CryptoBean cryptoBean = result.getClass().getAnnotation(CryptoBean.class);
        if (cryptoBean == null || resultMap == null) {
            return results;
        }

        Map<String, CryptoField> cryptFieldMap = getCryptFieldMap(resultMap);
        for (Object obj: results) {
            final MetaObject objMetaObject = SystemMetaObject.forObject(obj);
            for (Map.Entry<String, CryptoField> entry : cryptFieldMap.entrySet()) {
                String property = entry.getKey();
                Object value = objMetaObject.getValue(property);
                if (Objects.nonNull(value)) {
                    objMetaObject.setValue(property, CryptHandlerFactory.getCryptHandler(value, entry.getValue()).decrypt(value, entry.getValue()));
                }
            }
        }
        return results;
    }

    private Map<String, CryptoField> getCryptFieldMap(ResultMap resultMap) {
        Map<String, CryptoField> cryptFieldMap = Maps.newHashMap();

        if (resultMap == null) {
            return cryptFieldMap;
        }

        for (Field field: resultMap.getType().getDeclaredFields()) {
            CryptoField cryptoField = field.getAnnotation(CryptoField.class);
            if (cryptoField != null && cryptoField.decrypt()) {
                cryptFieldMap.put(field.getName(), cryptoField);
            }
        }
        return cryptFieldMap;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}