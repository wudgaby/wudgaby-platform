package com.wudgaby.starter.tenant.db;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.wudgaby.starter.tenant.config.TenantProperties;
import com.wudgaby.starter.tenant.context.TenantContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义租户处理器
 *
 * @author Lion Li
 */
@Slf4j
@AllArgsConstructor
public class PlusTenantLineHandler implements TenantLineHandler {

    private final Set<String> ignoreTables = new HashSet<>();

    public PlusTenantLineHandler(TenantProperties tenantProperties) {
        // 不同 DB 下，大小写的习惯不同，所以需要都添加进去
        tenantProperties.getIgnoreTables().forEach(table -> {
            ignoreTables.add(table.toLowerCase());
            ignoreTables.add(table.toUpperCase());
        });
    }

    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContextHolder.getTenantId();
        if (null == tenantId) {
            log.error("无法获取有效的租户id -> Null");
            return new NullValue();
        }
        // 返回固定租户
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        Long tenantId = TenantContextHolder.getTenantId();
        // 判断是否有租户
        if (null == tenantId) {
            // 不需要过滤租户的表
            return CollUtil.contains(ignoreTables, tableName);
        }
        return true;
    }

}
