package com.wudgaby.platform.sys.dict;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.wudgaby.platform.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/29 0029 14:37
 * @desc : 字典初始化缓存至内存
 */
@Slf4j
@Component
public class DictInitListener implements ApplicationListener<ApplicationReadyEvent>{
    private static final String DICT_TYPE_LIST_SQL = "select id, `name`,`type`, sort from sys_dict_type where status = 1 and deleted = 0 order by sort asc";
    private static final String DICT_ITEM_LIST_SQL = "select id, parent_id, dict_type,dict_name,dict_value,sort from sys_dict_item where status = 1 and deleted = 0 order by sort asc";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("加载字典数据到内存.");
        TimeInterval interval = new TimeInterval();
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();

        // 获取数据库连接配置
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String dataSourceUsername = environment.getProperty("spring.datasource.username");
        String dataSourcePassword = environment.getProperty("spring.datasource.password");
        String driverClass = environment.getProperty("spring.datasource.driver-class-name");

        // 如果有为空的配置，终止执行
        if (ObjectUtil.hasEmpty(dataSourceUrl, dataSourceUsername)) {
            throw new BusinessException("数据库配置不存在");
        }

        Connection conn = null;
        try {
            Class.forName(driverClass);
            assert dataSourceUrl != null;
            conn = DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword);

            // 获取dictType表的数据
            List<Entity> dictTypeList = SqlExecutor.query(conn, DICT_TYPE_LIST_SQL, new EntityListHandler());
            //获取dictItem表数据
            List<Entity> dictItemList = SqlExecutor.query(conn, DICT_ITEM_LIST_SQL, new EntityListHandler());

            // 字典类型
            Map<String, DictDTO> dictTypeMap = dictTypeList.stream().map(entity -> {
                DictDTO dictDTO = new DictDTO();
                dictDTO.setId(entity.getInt("id"));
                dictDTO.setLabel(entity.getStr("name"));
                dictDTO.setValue(entity.getStr("type"));
                dictDTO.setDictType(entity.getStr("type"));
                return dictDTO;
            }).collect(Collectors.toMap(DictDTO::getDictType, dict -> dict));

            // 字典类型分组
            Map<String, List<DictDTO>> dictParentMap = dictItemList.stream().map(entity -> {
                DictDTO dictDTO = new DictDTO();
                dictDTO.setId(entity.getInt("id"));
                dictDTO.setPid(entity.getInt("parent_id"));
                dictDTO.setLabel(entity.getStr("dict_name"));
                dictDTO.setValue(entity.getStr("dict_value"));
                dictDTO.setDictType(entity.getStr("dict_type"));
                return dictDTO;
            }).collect(Collectors.groupingBy(DictDTO::getDictType));

        } catch (SQLException | ClassNotFoundException e) {
            log.error(">>> 读取数据库constants配置信息出错：");
            e.printStackTrace();
            throw new BusinessException("读取数据库constants配置信息出错");
        } finally {
            DbUtil.close(conn);
        }
        log.info("加载字典数据到内存完成. 耗时: {} ms", interval.interval());
    }
}
