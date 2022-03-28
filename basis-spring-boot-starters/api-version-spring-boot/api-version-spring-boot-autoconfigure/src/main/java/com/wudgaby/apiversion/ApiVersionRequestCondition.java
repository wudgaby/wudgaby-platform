package com.wudgaby.apiversion;

import cn.hutool.core.comparator.VersionComparator;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Getter
public class ApiVersionRequestCondition implements RequestCondition<ApiVersionRequestCondition> {

    private final String apiVersion;
    private final ApiVersionProperties apiVersionProperties;

    public ApiVersionRequestCondition(@NonNull String apiVersion, @NonNull ApiVersionProperties apiVersionProperties) {
        this.apiVersion = apiVersion.trim();
        this.apiVersionProperties = apiVersionProperties;
    }

    @Override
    public ApiVersionRequestCondition combine(ApiVersionRequestCondition other) {
        // method annotation first
        return new ApiVersionRequestCondition(other.getApiVersion(), other.getApiVersionProperties());
    }

    @Override
    public int compareTo(ApiVersionRequestCondition other, HttpServletRequest request) {
        return other.getApiVersion().compareTo(getApiVersion());
    }

    @Override
    public ApiVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
        ApiVersionProperties.Type type = apiVersionProperties.getType();
        String reqVersion = null;
        switch (type) {
            case HEADER:
                reqVersion = request.getHeader(apiVersionProperties.getHeader());
                break;
            case PARAM:
                reqVersion = request.getParameter(apiVersionProperties.getParam());
                break;
            default:
                //nothing
                break;
        }

        if(StringUtils.isNotBlank(reqVersion)) {
            if(reqVersion.trim().equals(apiVersion)) {
                return this;
            }
            //使用最新版本
            if(VersionComparator.INSTANCE.compare(apiVersion, reqVersion) > 0) {
                return this;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "@ApiVersion(" + apiVersion + ")";
    }
}