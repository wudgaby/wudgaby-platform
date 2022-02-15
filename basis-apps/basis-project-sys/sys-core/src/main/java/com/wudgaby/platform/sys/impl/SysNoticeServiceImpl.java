package com.wudgaby.platform.sys.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.sys.entity.SysNotice;
import com.wudgaby.platform.sys.form.NoticeForm;
import com.wudgaby.platform.sys.mapper.SysNoticeMapper;
import com.wudgaby.platform.sys.service.SysNoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {
    @Override
    public void addNotice(NoticeForm noticeForm) {
        SysNotice sysNotice = new SysNotice()
                .setTitle(noticeForm.getTitle())
                .setContent(noticeForm.getContent())
                .setStatus(Objects.isNull(noticeForm.getStatus()) ? 1 : 0)
                .setStick(new Boolean(noticeForm.getStick()))
                ;
        this.save(sysNotice);
    }

    @Override
    public void removeNotice(Long id) {
        this.removeById(id);
    }

    @Override
    public void updateNoticeStatus(Long id, int status) {
        SysNotice sysNotice = new SysNotice();
        sysNotice.setId(id);
        sysNotice.setStatus(status);
        this.updateById(sysNotice);
    }

    @Override
    public IPage pageList(PageForm pageForm) {
        return this.page(new Page<>(pageForm.getPageNum(), pageForm.getPageSize()));
    }
}
