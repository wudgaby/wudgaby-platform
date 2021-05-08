package com.wudgaby.codegen.ui.service;

import cn.hutool.db.Db;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.google.common.collect.Lists;
import com.wudgaby.codegen.ui.entity.TableEntity;
import com.wudgaby.codegen.ui.form.TableQueryForm;
import com.wudgaby.platform.core.vo.PageVo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * @ClassName : TableService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/6/006 23:44
 * @Desc :   TODO
 */
@Slf4j
@Service
@AllArgsConstructor
public class TableService {
    private final DataSourceProperties dataSourceProperties;

    @SneakyThrows
    public PageVo getTableList(TableQueryForm tableQueryForm){
        String url = StringUtils.isNotBlank(tableQueryForm.getUrl()) ? StringUtils.trim(tableQueryForm.getUrl()) : dataSourceProperties.getUrl();
        String username = StringUtils.isNotBlank(tableQueryForm.getUsername()) ? StringUtils.trim(tableQueryForm.getUsername()) : dataSourceProperties.getUsername();
        String password = StringUtils.isNotBlank(tableQueryForm.getPassword()) ? StringUtils.trim(tableQueryForm.getPassword()) : dataSourceProperties.getPassword();

        if(!url.contains("?")){
            url = tableQueryForm.getUrl() + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false&allowMultiQueries=true&nullCatalogMeansCurrent=true";
        }
        DataSource ds = new SimpleDataSource(url, username, password);

        List<Object> countParam = Lists.newArrayList();
        String countSql = "SELECT count(1) FROM information_schema.tables WHERE table_schema = (select database())";
        if(StringUtils.isNotBlank(tableQueryForm.getTableName())){
            countSql += " AND table_name LIKE CONCAT('%',?,'%')";
            countParam.add(tableQueryForm.getTableName());
        }
        int count = Db.use(ds).queryNumber(countSql, CollectionUtils.isEmpty(countParam) ? null : countParam.toArray(new Object[countParam.size()])).intValue();

        List<Object> param = Lists.newArrayList();
        String sql = "SELECT * FROM information_schema.tables WHERE table_schema = (SELECT database())";
        if(StringUtils.isNotBlank(tableQueryForm.getTableName())){
            sql += " AND table_name LIKE CONCAT('%',?,'%')";
            param.add(tableQueryForm.getTableName());
        }
        sql += " ORDER BY create_time DESC, table_name ASC";
        sql += " LIMIT ?,?";
        param.add(tableQueryForm.getOffset());
        param.add(tableQueryForm.getPageCount());
        if(log.isDebugEnabled()){
            log.debug("{} {}", sql, param);
        }

        List<TableEntity> result = Db.use(ds).query(sql, TableEntity.class, CollectionUtils.isEmpty(param) ? null : param.toArray(new Object[param.size()]));

        PageVo<TableEntity> pageVo = new PageVo<TableEntity>()
                .setPageNum(tableQueryForm.getPageNum())
                .setPageCount(tableQueryForm.getPageCount())
                .setTotalData(count)
                .setDataList(result)
                ;
        return pageVo;
    }
}
