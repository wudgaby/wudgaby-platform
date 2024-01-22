package com.wudgaby.starter.core.param;

import de.schlichtherle.license.AbstractKeyStoreParam;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 12:07
 * @desc : 自定义keyStore存储方式
 */
@Getter
@Setter
public class CustomKeyStoreParam extends AbstractKeyStoreParam {
    private final String storePath;
    private final String alias;
    private final String storePwd;
    private final String keyPwd;

    public CustomKeyStoreParam(Class clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    /**
     * 用于将公私钥存储文件存放到其他磁盘位置而不是项目中，
     * AbstractKeyStoreParam 里面的 getStream() 方法默认文件是存储的项目中
     */
    @Override
    public InputStream getStream() throws IOException {
        return Files.newInputStream(new File(storePath).toPath());
    }

}
