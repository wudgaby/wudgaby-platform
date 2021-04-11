package com.wudgaby.platform.flowable.helper.extend;

import org.flowable.common.engine.impl.persistence.deploy.DeploymentCache;
import org.flowable.engine.impl.persistence.deploy.ProcessDefinitionCacheEntry;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 当大量的流程定义出现的时候，我们势必会不停的查询流程定义，然而流程定义之后和版本对应很少发生变化，这个时候，我们可以把这个流程定义缓存起来，以提高系统性能
 * 本地缓存
 */
@Component
public class CustomDeploymentCache implements DeploymentCache<ProcessDefinitionCacheEntry> {
    public static final Map<String,ProcessDefinitionCacheEntry> caches = new ConcurrentHashMap<>();

    @Override
    public ProcessDefinitionCacheEntry get(String s) {
        return caches.get(s);
    }

    @Override
    public boolean contains(String s) {
        boolean flag = false;
        if (caches.get(s) != null) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void add(String s, ProcessDefinitionCacheEntry processDefinitionCacheEntry) {
        caches.put(s, processDefinitionCacheEntry);
    }

    @Override
    public void remove(String s) {
        caches.remove(s);
    }

    @Override
    public void clear() {
        caches.clear();
    }

    @Override
    public Collection<ProcessDefinitionCacheEntry> getAll() {
        return caches.values();
    }

    @Override
    public int size() {
        return caches.size();
    }
}
