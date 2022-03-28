package com.wudgaby.apiversion;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@AllArgsConstructor
public class ApiVersionWebMvcRegistrations implements WebMvcRegistrations {

    @NonNull
    private ApiVersionProperties apiVersionProperties;

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new VersionedRequestMappingHandlerMapping(apiVersionProperties);
    }

}
