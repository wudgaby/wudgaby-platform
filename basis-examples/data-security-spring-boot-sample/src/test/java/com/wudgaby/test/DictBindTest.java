package com.wudgaby.test;

import com.google.common.collect.Lists;
import com.wudgaby.sample.data.sensitive.MyDictSource;
import com.wudgaby.sample.data.sensitive.entity.SysUser;
import com.wudgaby.starter.data.security.dict.DictBindUtil;
import com.wudgaby.starter.data.security.dict.handler.DictHandlerFactory;
import com.wudgaby.starter.data.security.dict.handler.StringDictBindHandler;
import com.wudgaby.starter.data.security.enums.HandlerType;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/15 0015 19:06
 * @desc :
 */
public class DictBindTest {
    public static void main(String[] args) {
        DictHandlerFactory.put(HandlerType.STRING, new StringDictBindHandler(new MyDictSource()));

        List<SysUser> userList = Lists.newArrayList();
        SysUser sysUser = new SysUser();
        sysUser.setStatus(1);
        sysUser.setSex("0");
        userList.add(sysUser);

        sysUser = new SysUser();
        sysUser.setStatus(1);
        sysUser.setSex("1");
        userList.add(sysUser);

        sysUser = new SysUser();
        sysUser.setStatus(0);
        sysUser.setSex("0");
        userList.add(sysUser);

        sysUser = new SysUser();
        sysUser.setStatus(0);
        sysUser.setSex("1");
        userList.add(sysUser);
        DictBindUtil.bind(userList).stream().forEach(System.out::println);
    }
}
