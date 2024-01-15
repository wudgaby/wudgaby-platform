package sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.starter.openapi.entity.ClientAppInfo;
import org.springframework.stereotype.Service;
import sample.mapper.ClientAppInfoMapper;
import sample.service.ClientAppInfoService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-12-01
 */
@Service
public class ClientAppInfoServiceImpl extends ServiceImpl<ClientAppInfoMapper, ClientAppInfo> implements ClientAppInfoService {

}
