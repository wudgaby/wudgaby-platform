package com.wudgaby.platform.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.sys.entity.SysNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.sys.form.NoticeForm;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
public interface SysNoticeService extends IService<SysNotice> {
    void addNotice(NoticeForm noticeForm);
    void removeNotice(Long id);
    void updateNoticeStatus(Long id, int status);
    IPage pageList(PageForm pageForm);
}
