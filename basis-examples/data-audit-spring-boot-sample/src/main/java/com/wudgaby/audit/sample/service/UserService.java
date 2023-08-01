package com.wudgaby.audit.sample.service;

import com.wudgaby.audit.sample.entity.User;
import com.wudgaby.audit.sample.mapper.UserMapper;
import com.wudgaby.starter.data.audit.audior.DataAuditEvent;
import com.wudgaby.starter.data.audit.audior.DataAuditor;
import lombok.AllArgsConstructor;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 17:16
 * @desc :
 */
@Service
@AllArgsConstructor
public class UserService {
    private UserMapper userMapper;
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public void dataAudit() {
        // 执行数据库操作
        User user = userMapper.selectById(1L);
        System.err.println(user);
        System.err.println("手动调用审计对比方法");
        DataAuditor.compare(user, userMapper.selectById(3L)).forEach(change -> printChange(change));

        System.err.println("-----publishEvent-----begin");
        // 发送事务监听的事件，异步回调
        applicationEventPublisher.publishEvent(new DataAuditEvent((t) -> {
            System.err.println("-----触发异步回调-----");
            User user2 = userMapper.selectById(2L);
            System.err.println(user2);
            t.apply(user, user2).forEach(change -> printChange(change));
        }));
        System.err.println("-----publishEvent-----end");
    }

    private void printChange(Change change) {
        ValueChange vc = (ValueChange) change;
        System.err.println(String.format("%s不匹配，期望值 %s 实际值 %s", vc.getPropertyName(),
                vc.getLeft(), vc.getRight()));
    }
}
