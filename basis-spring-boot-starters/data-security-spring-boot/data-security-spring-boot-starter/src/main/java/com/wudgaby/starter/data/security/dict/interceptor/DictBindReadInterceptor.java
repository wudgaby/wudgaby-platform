package com.wudgaby.starter.data.security.dict.interceptor;

import com.google.common.collect.Maps;
import com.wudgaby.starter.data.security.dict.annotation.DictBindBean;
import com.wudgaby.starter.data.security.dict.annotation.DictBindField;
import com.wudgaby.starter.data.security.dict.handler.DictHandlerFactory;
import com.wudgaby.starter.data.security.util.IgnoreClassUtil;
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
public class DictBindReadInterceptor implements Interceptor {
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
        DictBindBean dictBindBean = result.getClass().getAnnotation(DictBindBean.class);
        if (dictBindBean == null || resultMap == null) {
            return results;
        }

        Map<String, DictBindField> fieldMap = getFieldMap(resultMap);
        for (Object obj: results) {
            final MetaObject objMetaObject = SystemMetaObject.forObject(obj);
            for (Map.Entry<String, DictBindField> entry : fieldMap.entrySet()) {
                String property = entry.getKey();
                Object value = objMetaObject.getValue(property);
                if (Objects.nonNull(value)) {
                    Object transformed = DictHandlerFactory.getHandler(value, entry.getValue()).transform(value, entry.getValue());
                    if(value instanceof String || IgnoreClassUtil.inIgnoreClass(value.getClass())){
                        objMetaObject.setValue(entry.getValue().target(), transformed);
                    }else{
                        objMetaObject.setValue(property, transformed);
                    }
                }
            }
        }
        return results;
    }

    private Map<String, DictBindField> getFieldMap(ResultMap resultMap) {
        Map<String, DictBindField> fieldMap = Maps.newHashMap();

        if (resultMap == null) {
            return fieldMap;
        }

        for (Field field: resultMap.getType().getDeclaredFields()) {
            DictBindField dictBindField = field.getAnnotation(DictBindField.class);
            if (dictBindField != null) {
                fieldMap.put(field.getName(), dictBindField);
            }
        }
        return fieldMap;
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