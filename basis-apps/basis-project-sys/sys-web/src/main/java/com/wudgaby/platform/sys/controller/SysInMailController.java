package com.wudgaby.platform.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.sys.dto.InMailDTO;
import com.wudgaby.platform.sys.dto.InMailTypeStDTO;
import com.wudgaby.platform.sys.entity.SysInMail;
import com.wudgaby.platform.sys.entity.SysInMailReceiver;
import com.wudgaby.platform.sys.enums.InMailType;
import com.wudgaby.platform.sys.form.InMailForm;
import com.wudgaby.platform.sys.form.InMailQueryForm;
import com.wudgaby.platform.sys.mapper.SysInMailMapper;
import com.wudgaby.platform.sys.service.SysInMailReceiverService;
import com.wudgaby.platform.sys.service.SysInMailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 站内信 前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Api(tags = "站内信")
@RestController
@RequestMapping("/inMails")
@AllArgsConstructor
public class SysInMailController {
    private final SysInMailService sysInMailService;
    private final SysInMailReceiverService sysInMailReceiverService;

    @ApiOperation("我的通知列表")
    @GetMapping("/myInMailList")
    public ApiPageResult<SysInMail> myList(InMailQueryForm queryForm) {
        long userId = (long) SecurityUtils.getCurrentUser().getId();
        queryForm.setUserId(userId);
        return ApiPageResult.success(sysInMailService.pageList(queryForm));
    }

    @ApiOperation("消息中心")
    @GetMapping("/inMailCenter")
    public ApiResult<List<InMailTypeStDTO>> inMailCenter() {
        long userId = (long)SecurityUtils.getCurrentUser().getId();
        List<InMailTypeStDTO> list = ((SysInMailMapper)sysInMailService.getBaseMapper()).listUnReadCountGroupByNoticeType(userId);

        if(CollectionUtils.isEmpty(list)){
            list = Arrays.stream(InMailType.values()).map(type -> new InMailTypeStDTO().setInMailType(type).setUnreadTotal(0L)).collect(Collectors.toList());
        }else if(list.size() != InMailType.values().length){
            Map<InMailType, Long> map = list.stream().collect(Collectors.toMap(InMailTypeStDTO::getInMailType, InMailTypeStDTO::getUnreadTotal));

            for(InMailType noticeType : InMailType.values()){
                if(map.get(noticeType) == null){
                    list.add(new InMailTypeStDTO().setInMailType(noticeType).setUnreadTotal(0L));
                }
            }
        }
        return ApiResult.success(list);
    }

    @ApiOperation("查看我的通知")
    @GetMapping("/my/{id}")
    public ApiResult<InMailDTO> myInfo(@PathVariable("id") Long id) {
        long userId = (long)SecurityUtils.getCurrentUser().getId();
        InMailDTO noticeDTO = ((SysInMailMapper)sysInMailService.getBaseMapper()).queryByIdAndUserId(id, userId);
        Assert.notNull(noticeDTO, "无该条通知");

        noticeDTO.setReceiverNameList(null);
        SysInMailReceiver sysInMailReceiver = new SysInMailReceiver();
        sysInMailReceiver.setStatus(1);
        sysInMailReceiverService.update(sysInMailReceiver, Wrappers.<SysInMailReceiver>lambdaQuery()
                .eq(SysInMailReceiver::getImId, id)
                .eq(SysInMailReceiver::getReceiverId, userId));
        return ApiResult.success(noticeDTO);
    }

    @ApiOperation("列表")
    @GetMapping
    public ApiPageResult<SysInMail> list(InMailQueryForm queryForm) {
        return ApiPageResult.success(sysInMailService.pageList(queryForm));
    }

    @ApiOperation("查看信息")
    @GetMapping("/info/{id}")
    public ApiResult<InMailDTO> info(@PathVariable("id") Long id) {
        InMailDTO noticeDTO = ((SysInMailMapper)sysInMailService.getBaseMapper()).queryById(id);
        return ApiResult.success(noticeDTO);
    }

    @ApiOperation("添加站内信")
    @PostMapping
    public ApiResult addInMail(@RequestBody @Validated InMailForm inMailForm) {
        sysInMailService.saveNotice(inMailForm);
        return ApiResult.success();
    }

    @ApiOperation("删除站内信")
    @DeleteMapping
    public ApiResult delete(@RequestBody List<Long> ids) {
        sysInMailService.delNotice(ids);
        return ApiResult.success();
    }
}
