package com.wudgaby.platform.websocket.storage;

import com.google.common.collect.Maps;
import com.wudgaby.platform.websocket.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @ClassName : UserStorage
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/26 20:53
 * @Desc :   TODO
 */
@Slf4j
@Component
public class UserStorage implements InitializingBean {
    private Map<String, UserVo> USER_STORAGE = Maps.newHashMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        USER_STORAGE.put("刘一", new UserVo(1, "刘一", "aa0000", "刘一"));
        USER_STORAGE.put("陈二", new UserVo(1, "陈二", "aa0000", "陈二"));
        USER_STORAGE.put("张三", new UserVo(1, "张三", "aa0000", "张三"));
        USER_STORAGE.put("李四", new UserVo(2, "李四", "aa0000", "李四"));
        USER_STORAGE.put("王五", new UserVo(3, "王五", "aa0000", "王五"));
        USER_STORAGE.put("赵六", new UserVo(3, "赵六", "aa0000", "赵六"));
        USER_STORAGE.put("孙七", new UserVo(3, "孙七", "aa0000", "孙七"));
        USER_STORAGE.put("周八", new UserVo(3, "周八", "aa0000", "周八"));
        USER_STORAGE.put("吴九", new UserVo(3, "吴九", "aa0000", "吴九"));
        USER_STORAGE.put("郑十", new UserVo(3, "郑十", "aa0000", "郑十"));

        USER_STORAGE.put("wudgaby", new UserVo(4, "wudgaby", "aa0000", "wudgaby"));
        USER_STORAGE.put("hkbird", new UserVo(5, "hkbird", "aa0000", "hkbird"));
    }

    @PostConstruct
    public void postConstruct(){
        log.info("初始化storage");
    }

    public UserVo find(String account, String password){
        UserVo userVo = USER_STORAGE.get(account);
        if(userVo != null && userVo.getPassword().equals(password)){
            return userVo;
        }
        return null;
    }
}
