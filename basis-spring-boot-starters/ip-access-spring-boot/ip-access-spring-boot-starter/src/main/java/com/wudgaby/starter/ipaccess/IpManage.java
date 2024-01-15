package com.wudgaby.starter.ipaccess;

import com.wudgaby.starter.ipaccess.enums.BwType;
import org.springframework.lang.NonNull;

import java.util.Collection;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 10:31
 * @desc :
 */
public interface IpManage {
    /**
     * 添加名单
     * @param ip
     * @param type
     */
    void add(@NonNull String ip, @NonNull BwType type);

    /**
     * 批量添加
     * @param ipList
     * @param type
     */
    void batchAdd(@NonNull Collection<String> ipList, @NonNull BwType type);

    /**
     * 删除名单
     * @param ip
     * @param type
     */
    void del(@NonNull String ip, @NonNull BwType type);

    /**
     * 删除名单
     * @param ip
     * @param type
     */
    void batchDel(@NonNull Collection<String> ipList, @NonNull BwType type);

    /**
     * 获取名单列表
     * @param type
     */
    Collection<String> list(@NonNull BwType type);

    /**
     * 是否包含指定ip
     * @param ip
     * @param type
     * @return
     */
    boolean containIp(@NonNull String ip, @NonNull BwType type);

}
