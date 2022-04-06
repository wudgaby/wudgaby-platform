package com.wudgaby.ip2region;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.nutz.plugins.ip2region.DbConfig;
import org.nutz.plugins.ip2region.DbSearcher;
import org.nutz.plugins.ip2region.impl.ByteArrayDBReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 15:18
 * @desc :
 */
@Slf4j
@RequiredArgsConstructor
public class Ip2RegionHelper{
    private final ResourceLoader resourceLoader;
    private final Ip2regionProperties properties;
    private DbSearcher searcher;

    @SneakyThrows
    public RegionInfo memorySearch(long ip) {
        return RegionInfo.of(searcher.memorySearch(ip));
    }

    @SneakyThrows
    public RegionInfo memorySearch(String ip) {
        return RegionInfo.of(searcher.memorySearch(ip));
    }

    @SneakyThrows
    public RegionInfo getByIndexPtr(long ptr) {
        return RegionInfo.of(searcher.getByIndexPtr(ptr));
    }

    @SneakyThrows
    public RegionInfo btreeSearch(long ip) {
        return RegionInfo.of(searcher.btreeSearch(ip));
    }

    @SneakyThrows
    public RegionInfo btreeSearch(String ip) {
        return RegionInfo.of(searcher.btreeSearch(ip));
    }

    @SneakyThrows
    public RegionInfo binarySearch(long ip) {
        return RegionInfo.of(searcher.binarySearch(ip));
    }

    @SneakyThrows
    public RegionInfo binarySearch(String ip) {
        return RegionInfo.of(searcher.binarySearch(ip));
    }

    @SneakyThrows
    @PostConstruct
    public void afterPropertiesSet(){
        log.info("初始化Ip2Region助手");
        DbConfig config = new DbConfig();
        Resource resource = resourceLoader.getResource(properties.getDbFileLocation());
        try (InputStream inputStream = resource.getInputStream()) {
            this.searcher = new DbSearcher(config, new ByteArrayDBReader(StreamUtils.copyToByteArray(inputStream)));
        }
    }

    @SneakyThrows
    @PreDestroy
    public void destorySearcher(){
        if(searcher != null) {
            log.info("销毁searcher对象");
            searcher.close();
        }
    }
}
