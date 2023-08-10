package com.wudgaby.starter.data.security.dict.interceptor;

import com.wudgaby.starter.data.security.dict.DictBindUtil;
import com.wudgaby.starter.data.security.dict.annotation.DictBindBean;
import com.wudgaby.starter.data.security.util.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Properties;

/**
 * @author wudgaby
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {java.sql.Statement.class})
})
@Slf4j
@Order(100)
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

        DictBindUtil.bind(results);
        return results;
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