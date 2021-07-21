package com.wudgaby.platform.flowable.helper.config;

import com.wudgaby.platform.flowable.helper.enums.VoteResultType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : FlowableMultiCaseProperites
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/20 9:03
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "flowable")
public class FlowableProperites {
    private boolean isDynamicFlow;
    private MultiCase multiCase;

    @Data
    public static class MultiCase {
        private VoteResultType voteResultType = VoteResultType.ALL_PASS;
        private double passPercent = 0.5;
    }
}
