package com.wudgaby.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.sample.domain.SysConfig;
import com.wudgaby.sample.service.SysConfigService;
import com.wudgaby.sample.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author wudgaby
* @description 针对表【sys_config(配置表)】的数据库操作Service实现
* @createDate 2024-01-03 14:45:29
*/
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
    implements SysConfigService{

}




