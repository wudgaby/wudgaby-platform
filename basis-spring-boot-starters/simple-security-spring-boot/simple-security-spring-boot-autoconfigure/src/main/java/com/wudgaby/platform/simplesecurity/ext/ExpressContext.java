package com.wudgaby.platform.simplesecurity.ext;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * @author wudgaby
 */
public class ExpressContext {

    private final EvaluationContext context;
    private final ExpressionParser parser;

    private ExpressContext(ApplicationContext applicationContext) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (applicationContext != null) {
            context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        }
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
        this.context = context;
        this.parser = new SpelExpressionParser(config);
    }

    /**
     *
     * @param applicationContext  spring上下文, 获取bean.
     * @param args  方法上的参数列表
     * @param method 方法
     */
    public ExpressContext(ApplicationContext applicationContext, Object[] args, Method method) {
        this(applicationContext);

        if(ArrayUtils.isNotEmpty(args)){
            ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
            String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
            for (int i = 0; i < args.length; i++) {
                if (paramNames != null) {
                    context.setVariable(paramNames[i], args[i]);
                }
            }
        }
    }

    public ExpressContext(ApplicationContext applicationContext, Map<String, Object> paramMap) {
        this(applicationContext);

       if(MapUtils.isNotEmpty(paramMap)) {
           paramMap.forEach(context::setVariable);
       }
    }

    public ExpressContext(Object[] args, Method method) {
        this(null);
        ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
        for (int i = 0; i < args.length; i++) {
            if (paramNames != null) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
    }

    public String getStringValue(String express) {
        Expression expression = parser.parseExpression(express);
        return expression.getValue(context, String.class);
    }

    public boolean getBooleanValue(String express) {
        try{
            Expression expression = parser.parseExpression(express);
            return Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
        }catch (EvaluationException e) {
            throw new IllegalArgumentException("表达式求值失败 '" + e.getExpressionString() + "'", e);
        }

    }
}