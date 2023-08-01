package com.wudgaby.audit.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudgaby.audit.sample.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 17:08
 * @desc :
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
