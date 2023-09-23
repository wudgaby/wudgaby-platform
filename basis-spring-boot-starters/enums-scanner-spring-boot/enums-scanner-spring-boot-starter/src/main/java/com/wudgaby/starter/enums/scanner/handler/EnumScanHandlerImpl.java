package com.wudgaby.starter.enums.scanner.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.wudgaby.starter.enums.scanner.annotation.EnumScan;
import com.wudgaby.starter.enums.scanner.cached.EnumCache;
import com.wudgaby.starter.enums.scanner.context.ResourcesScanner;
import com.wudgaby.starter.enums.scanner.model.CodeItem;
import com.wudgaby.starter.enums.scanner.model.CodeTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: zhuCan
 * @date: 2020/1/16 13:48
 * @description: 使用码表扫描, 就需要创建一个EnumCache的实现类, 并设置为BEAN
 */
@Slf4j
public class EnumScanHandlerImpl implements EnumScanHandler {

    /**
     * 码表缓存
     */
    private EnumCache cache;

    /**
     * 资源扫描器
     */
    private ResourcesScanner<Class<?>> resourcesScanner;


    public EnumScanHandlerImpl(EnumCache cache, ResourcesScanner<Class<?>> resourcesScanner) {
        this.cache = cache;
        this.resourcesScanner = resourcesScanner;
    }

    /**
     * 通过反射来获取所有需要扫描的枚举属性值,并存入缓存中
     */
    private void cacheHandler() {
        log.info("扫描所有枚举类");
        // 扫描所有枚举类
        List<Class<?>> clazzList = resourcesScanner.classScan();

        List<CodeTable> codeEnums = new ArrayList<>();
        clazzList.forEach(clazz -> {
            // 过滤出 继承了CodeEnum 和 标记了EnumScan注解的枚举
            if (clazz.isEnum() && clazz.isAnnotationPresent(EnumScan.class)) {
                // 枚举的所有实例
                List<Object> items = Arrays.stream(clazz.getEnumConstants()).collect(Collectors.toList());
                //获取默认值
                String defaultCode = clazz.getAnnotation(EnumScan.class).defaultCode();
                String bindCode = clazz.getAnnotation(EnumScan.class).bindCode();
                String bindName = clazz.getAnnotation(EnumScan.class).bindName();

                if (StringUtils.isBlank(bindCode) || StringUtils.isBlank(bindName)) {
                    return;
                }

                List<CodeItem> codeItemList = items.stream().map(item-> {
                    try{
                        Object enumCode = ReflectUtil.getFieldValue(item, bindCode);
                        String enumName = StrUtil.toString(ReflectUtil.getFieldValue(item, bindName));
                        return new CodeItem(enumCode, enumName);
                    }catch (Exception e){
                        log.error(e.getMessage(), e);
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());

                CodeItem defaultCodeItem = codeItemList.stream().filter(Objects::nonNull)
                        .filter(codeItem -> Objects.equals(codeItem.getCode(), defaultCode))
                        .findFirst().orElse(null);

                // 填充数据
                codeEnums.add(new CodeTable(clazz.getSimpleName(), codeItemList, defaultCodeItem, clazz.getName()));
            }
        });
        cache.write(codeEnums);
        log.info("共缓存枚举: {}", CollUtil.size(codeEnums));
    }

    /**
     * 获取系统中的所有枚举
     *
     * @return 码表集合
     */
    @Override
    public List<CodeTable> codeTables() {
        List<CodeTable> read = cache.read();
        if (read == null) {
            // 获取不到数据进行加载
            cacheHandler();
            return cache.read();
        }
        return read;
    }


}
