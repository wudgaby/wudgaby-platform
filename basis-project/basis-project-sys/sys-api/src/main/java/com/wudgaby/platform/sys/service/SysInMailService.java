package com.wudgaby.platform.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.sys.entity.SysInMail;
import com.wudgaby.platform.sys.form.InMailForm;
import com.wudgaby.platform.sys.form.InMailQueryForm;

import java.util.List;

/**
 * <p>
 * 站内信 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
public interface SysInMailService extends IService<SysInMail> {
    IPage pageList(InMailQueryForm pageForm);

    void saveNotice(InMailForm noticeForm);

    void delNotice(List<Long> ids);
}
