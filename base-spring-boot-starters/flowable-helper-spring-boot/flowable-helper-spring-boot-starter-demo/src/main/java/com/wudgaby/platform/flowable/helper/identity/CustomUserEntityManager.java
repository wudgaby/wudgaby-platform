package com.wudgaby.platform.flowable.helper.identity;

import com.wudgaby.platform.flowable.helper.mapper.FlowableCommonMapper;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.UserQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntity;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityManagerImpl;
import org.flowable.idm.engine.impl.persistence.entity.data.UserDataManager;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 庄金明
 * @date 2020年3月24日
 */
public class CustomUserEntityManager extends UserEntityManagerImpl {
    @Resource
    private FlowableCommonMapper flowableCommonMapper;

    public CustomUserEntityManager(IdmEngineConfiguration idmEngineConfiguration, UserDataManager userDataManager) {
        super(idmEngineConfiguration, userDataManager);
    }

    @Override
    public UserEntity findById(String entityId) {
        UserQueryImpl aUserQueryImpl = (UserQueryImpl) new UserQueryImpl().userId(entityId);
        List<User> userEntities = flowableCommonMapper.selectUserByQueryCriteria(aUserQueryImpl);
        if (userEntities != null && userEntities.size() > 0) {
            return (UserEntity) userEntities.get(0);
        }
        return null;
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query) {
        return flowableCommonMapper.selectUserByQueryCriteria(query);
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        return flowableCommonMapper.selectUserCountByQueryCriteria(query);
    }
}
