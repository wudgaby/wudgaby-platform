package com.wudgaby.downloadsample;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

public interface PeopleMapper extends BaseMapper<People> {
    /**
     *  方式一 多次获取，一次多行
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("SELECT t1.* FROM biz_people_copy2 t1 ${ew.customSqlSegment} ")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000000)
    IPage<People> pageList(@Param("page") IPage<People> page, @Param(Constants.WRAPPER) QueryWrapper<People> queryWrapper);

    /**
     * 方式二 一次获取，一次一行
     * @param queryWrapper
     * @param handler
     */
    @Select("SELECT t1.* FROM biz_people_copy2 t1 ${ew.customSqlSegment} ")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 100000)
    @ResultType(People.class)
    void listData(@Param(Constants.WRAPPER) QueryWrapper<People> queryWrapper, ResultHandler<People> handler);
}
