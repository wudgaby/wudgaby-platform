package com.wudgaby.platform.lab.oauth2.repository;

import com.wudgaby.platform.lab.oauth2.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/9/22 14:19
 * @Desc :
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    User findUserByPhone(String phone);

    User findUserByEmail(String email);
}
