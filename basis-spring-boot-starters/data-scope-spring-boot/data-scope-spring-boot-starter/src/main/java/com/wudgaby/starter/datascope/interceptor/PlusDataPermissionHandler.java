package com.wudgaby.starter.datascope.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.security.core.RoleVo;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.starter.datascope.annotation.DataColumn;
import com.wudgaby.starter.datascope.annotation.DataPermission;
import com.wudgaby.starter.datascope.enums.DataScopeType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据权限过滤
 *
 * @author Lion Li
 * @version 3.5.0
 */
@Slf4j
public class PlusDataPermissionHandler {

    /**
     * 方法或类(名称) 与 注解的映射关系缓存
     */
    private final Map<String, DataPermission> dataPermissionCacheMap = new ConcurrentHashMap<>();

    /**
     * spel 解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParserContext parserContext = new TemplateParserContext();
    /**
     * bean解析器 用于处理 spel 表达式中对 bean 的调用
     */
    private final BeanResolver beanResolver = new BeanFactoryResolver(SpringUtil.getBeanFactory());


    public Expression getSqlSegment(Expression where, String mappedStatementId, boolean isSelect) {
        DataColumn[] dataColumns = findAnnotation(mappedStatementId);
        UserInfo currentUser = DataPermissionHelper.getVariable("user");
        if (ObjectUtil.isNull(currentUser)) {
            currentUser = SecurityUtils.getCurrentUser();
            DataPermissionHelper.setVariable("user", currentUser);
        }
        // 如果是超级管理员或租户管理员，则不过滤数据
        if (SecurityUtils.isSuperAdmin() || SecurityUtils.isTenantAdmin()) {
            return where;
        }
        String dataFilterSql = buildDataFilter(dataColumns, isSelect);
        if (StringUtils.isBlank(dataFilterSql)) {
            return where;
        }
        try {
            Expression expression = CCJSqlParserUtil.parseExpression(dataFilterSql);
            // 数据权限使用单独的括号 防止与其他条件冲突
            Parenthesis parenthesis = new Parenthesis(expression);
            if (ObjectUtil.isNotNull(where)) {
                return new AndExpression(where, parenthesis);
            } else {
                return parenthesis;
            }
        } catch (JSQLParserException e) {
            throw new BusinessException("数据权限解析异常 => " + e.getMessage());
        }
    }

    /**
     * 构造数据过滤sql
     */
    private String buildDataFilter(DataColumn[] dataColumns, boolean isSelect) {
        // 更新或删除需满足所有条件
        String joinStr = isSelect ? " OR " : " AND ";
        UserInfo user = DataPermissionHelper.getVariable("user");
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(beanResolver);
        DataPermissionHelper.getContext().forEach(context::setVariable);
        Set<String> conditions = new HashSet<>();
        for (RoleVo role : user.getRoleList()) {
            // 获取角色权限泛型
            DataScopeType type = DataScopeType.findCode(role.getDataScope());
            if (ObjectUtil.isNull(type)) {
                throw new RuntimeException("角色数据范围异常 => " + role.getDataScope());
            }
            // 全部数据权限直接返回
            if (type == DataScopeType.ALL) {
                return "";
            }
            boolean isSuccess = false;
            for (DataColumn dataColumn : dataColumns) {
                if (dataColumn.key().length != dataColumn.value().length) {
                    throw new RuntimeException("角色数据范围异常 => key与value长度不匹配");
                }
                // 不包含 key 变量 则不处理
                if (!StringUtils.containsAny(type.getSqlTemplate(),
                    Arrays.stream(dataColumn.key()).map(key -> "#" + key).toArray(String[]::new)
                )) {
                    continue;
                }
                // 设置注解变量 key 为表达式变量 value 为变量值
                for (int i = 0; i < dataColumn.key().length; i++) {
                    context.setVariable(dataColumn.key()[i], dataColumn.value()[i]);
                }

                // 解析sql模板并填充
                String sql = parser.parseExpression(type.getSqlTemplate(), parserContext).getValue(context, String.class);
                conditions.add(joinStr + sql);
                isSuccess = true;
            }
            // 未处理成功则填充兜底方案
            if (!isSuccess && StringUtils.isNotBlank(type.getElseSql())) {
                conditions.add(joinStr + type.getElseSql());
            }
        }

        if (CollUtil.isNotEmpty(conditions)) {
            String sql = StreamUtil.join(conditions.stream(), "", Function.identity());
            return sql.substring(joinStr.length());
        }
        return "";
    }

    public DataColumn[] findAnnotation(String mappedStatementId) {
        StringBuilder sb = new StringBuilder(mappedStatementId);
        int index = sb.lastIndexOf(".");
        String clazzName = sb.substring(0, index);
        String methodName = sb.substring(index + 1, sb.length());
        Class<?> clazz;
        try {
            clazz = ClassUtil.loadClass(clazzName);
        } catch (Exception e) {
            return null;
        }
        List<Method> methods = Arrays.stream(ClassUtil.getDeclaredMethods(clazz))
            .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
        DataPermission dataPermission;
        // 获取方法注解
        for (Method method : methods) {
            dataPermission = dataPermissionCacheMap.get(mappedStatementId);
            if (ObjectUtil.isNotNull(dataPermission)) {
                return dataPermission.value();
            }
            if (AnnotationUtil.hasAnnotation(method, DataPermission.class)) {
                dataPermission = AnnotationUtil.getAnnotation(method, DataPermission.class);
                dataPermissionCacheMap.put(mappedStatementId, dataPermission);
                return dataPermission.value();
            }
        }
        dataPermission = dataPermissionCacheMap.get(clazz.getName());
        if (ObjectUtil.isNotNull(dataPermission)) {
            return dataPermission.value();
        }
        // 获取类注解
        if (AnnotationUtil.hasAnnotation(clazz, DataPermission.class)) {
            dataPermission = AnnotationUtil.getAnnotation(clazz, DataPermission.class);
            dataPermissionCacheMap.put(clazz.getName(), dataPermission);
            return dataPermission.value();
        }
        return null;
    }

}
