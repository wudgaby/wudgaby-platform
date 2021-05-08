package com.wudgaby.platform.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wudgaby.platform.sys.dto.InMailDTO;
import com.wudgaby.platform.sys.dto.InMailTypeStDTO;
import com.wudgaby.platform.sys.entity.SysInMail;
import com.wudgaby.platform.sys.form.InMailQueryForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 站内信 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
public interface SysInMailMapper extends BaseMapper<SysInMail> {
    InMailDTO queryById(@Param("id")Long id);
    InMailDTO queryByIdAndUserId(@Param("id")Long id, @Param("userId")Long userId);
    IPage listPage(IPage page, @Param("param") InMailQueryForm inMailQueryForm);
    List<InMailTypeStDTO> listUnReadCountGroupByNoticeType(@Param("userId")Long userId);
}
