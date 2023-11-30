package com.wudgaby.sample;

import com.wudgaby.plugin.resubmit.LoginUserService;
import org.springframework.stereotype.Service;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/12/28 0028 15:01
 * @Desc :
 */
@Service
public class LoginedUserServiceImpl implements LoginUserService {
    @Override
    public String getLoggedUserId() {
        return "curtUser";
    }
}
