package com.wudgaby.platform.data.auth.interceptor;

import cn.hutool.core.util.ReflectUtil;
import com.wudgaby.platform.data.auth.annotation.DataAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName : MybatisDataAuthInterceptor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/7/7 16:30
 * @Desc :   TODO
 */
@Slf4j
@Intercepts(
    {@Signature(type = Executor.class, method = "query",
              args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})}
)
public class MybatisDataAuthInterceptor implements Interceptor, ApplicationContextAware{
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //被代理对象
        Executor executor = (Executor) invocation.getTarget();

        //SystemMetaObject.forObject(invocation);

        //代理方法
        Method method = invocation.getMethod();

        //方法参数
        Object[] args = invocation.getArgs();

        MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(executor, "mappedStatement");
        //千万不能用下面注释的这个方法，会造成对象丢失，以致转换失败
        //MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        DataAuth dataAuth = AnnotationUtils.findAnnotation(method, DataAuth.class);
        if(dataAuth != null){

        }

        Object result = invocation.proceed();
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * <p>
     * 获得真正的处理对象,可能多层代理.
     * </p>
     */
    @SuppressWarnings("unchecked")
    public static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }
}
