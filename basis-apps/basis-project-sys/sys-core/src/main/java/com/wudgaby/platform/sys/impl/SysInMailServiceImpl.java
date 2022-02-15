package com.wudgaby.platform.sys.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.sys.entity.SysInMail;
import com.wudgaby.platform.sys.entity.SysInMailFile;
import com.wudgaby.platform.sys.entity.SysInMailReceiver;
import com.wudgaby.platform.sys.entity.SysUser;
import com.wudgaby.platform.sys.enums.InMailType;
import com.wudgaby.platform.sys.form.InMailForm;
import com.wudgaby.platform.sys.form.InMailQueryForm;
import com.wudgaby.platform.sys.mapper.SysInMailMapper;
import com.wudgaby.platform.sys.service.SysInMailFileService;
import com.wudgaby.platform.sys.service.SysInMailReceiverService;
import com.wudgaby.platform.sys.service.SysInMailService;
import com.wudgaby.platform.sys.service.SysUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 站内信 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysInMailServiceImpl extends ServiceImpl<SysInMailMapper, SysInMail> implements SysInMailService {
    private final SysInMailFileService sysInMailFileService;
    private final SysInMailReceiverService sysInMailReceiverService;
    private final SysUserService sysUserService;

    @Override
    public IPage pageList(InMailQueryForm pageForm) {
        IPage page = new Page(pageForm.getPageNum(), pageForm.getPageSize());
        this.baseMapper.listPage(page, pageForm);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNotice(InMailForm noticeForm) {
        Date now = new Date();
        SysInMail sysInMail = new SysInMail();
        if(noticeForm.getInMailType() == InMailType.SYSTEM){
            sysInMail.setSenderId(0L);
            sysInMail.setCreateBy("系统");
        }else{
            sysInMail.setSenderId((long)SecurityUtils.getCurrentUser().getId());
            sysInMail.setCreateBy(SecurityUtils.getCurrentUser().getUsername());
        }
        sysInMail.setCreateTime(now);
        sysInMail.setSendAll(noticeForm.getSendAll());
        sysInMail.setInMailType(noticeForm.getInMailType());
        sysInMail.setTitle(noticeForm.getTitle());
        sysInMail.setContent(noticeForm.getContent());
        this.save(sysInMail);

        //通知附件
        if(CollUtil.isNotEmpty(noticeForm.getAttachmentList())){
            List<SysInMailFile> noticeFileEntityList = noticeForm.getAttachmentList().stream().map(dto -> {
                SysInMailFile fileEntity = new SysInMailFile();
                fileEntity.setFileName(dto.getFileName());
                fileEntity.setFileUrl(dto.getFileUrl());
                fileEntity.setImId(sysInMail.getId());
                fileEntity.setCreateTime(now);
                return fileEntity;
            }).collect(Collectors.toList());
            sysInMailFileService.saveBatch(noticeFileEntityList);
        }

        //接收人关系
        if(!noticeForm.getSendAll()){
            AssertUtil.notEmpty(noticeForm.getReceiverIds(), "请选择接收人!");

            List<SysUser> receiverList = sysUserService.listByIds(noticeForm.getReceiverIds());
            Map<Long, String> userMap = receiverList.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getUserName));

            List<SysInMailReceiver> sysInMailReceiverList = noticeForm.getReceiverIds().stream().map(rid -> {
                SysInMailReceiver sysInMailReceiver = new SysInMailReceiver();
                sysInMailReceiver.setImId(sysInMail.getId());
                sysInMailReceiver.setStatus(0);
                sysInMailReceiver.setReceiverId(rid);

                String userName = userMap.get(rid);
                sysInMailReceiver.setReceiverName(userName == null ? "" : userName);
                sysInMailReceiver.setCreateTime(now);
                return sysInMailReceiver;
            }).collect(Collectors.toList());
            sysInMailReceiverService.saveBatch(sysInMailReceiverList);
        } else {
            List<SysUser> userList = sysUserService.list();
            List<SysInMailReceiver> receiverList = userList.stream().map(user -> {
                SysInMailReceiver sysNoticeReceiverEntity = new SysInMailReceiver();
                sysNoticeReceiverEntity.setImId(sysInMail.getId());
                sysNoticeReceiverEntity.setStatus(0);
                sysNoticeReceiverEntity.setReceiverId(user.getId());

                sysNoticeReceiverEntity.setReceiverName(user.getUserName());
                sysNoticeReceiverEntity.setCreateTime(now);
                return sysNoticeReceiverEntity;
            }).collect(Collectors.toList());
            sysInMailReceiverService.saveBatch(receiverList);
        }
    }

    @Override
    public void delNotice(List<Long> ids) {
        //删除通知
        this.removeByIds(Lists.newArrayList(ids));
        //删除附件
        sysInMailFileService.remove(Wrappers.<SysInMailFile>lambdaQuery().in(SysInMailFile::getImId, ids));
        //删除接收关系
        sysInMailReceiverService.remove(Wrappers.<SysInMailReceiver>lambdaQuery().in(SysInMailReceiver::getImId, ids));
    }
}
