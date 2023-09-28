package com.wudgaby.starter.captcha.config;

import com.wf.captcha.base.Captcha;
import com.wudgaby.starter.captcha.enums.CaptchaAutoCheckModeEnum;
import com.wudgaby.starter.captcha.enums.CaptchaEnum;
import com.wudgaby.starter.captcha.enums.CaptchaStoreEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 10:08
 * @desc :
 */
@Data
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProp {

    /**
     * 端点路径
     */
    private String createPath = "/captcha";

    /**
     * 验证码位数，默认值： 4
     */
    private int len = 4;

    /**
     * 默认宽度，默认值： 130
     */
    private int width = 130;

    /**
     * 默认高度，默认值：48
     */
    private int height = 48;

    /**
     * 验证码类型
     */
    private CaptchaEnum captchaType = CaptchaEnum.GIF;

    /**
     * 验证码文本类型, 默认纯数字. 只有Spec,GIF类型有效
     */
    private int charType = Captcha.TYPE_ONLY_NUMBER;

    /**
     * 内置字体
     */
    private int fontType = Captcha.FONT_1;

    /**
     * 存储prefix key
     */
    private String storePrefixKey = "captcha";

    /**
     * 存储redis时间
     */
    private Duration duration = Duration.ofMinutes(5L);

    /**
     * 存储类型
     */
    private CaptchaStoreEnum storeType = CaptchaStoreEnum.MEMORY;

    /**
     * 过滤器模式
     */
    private AutoCheckMode autoCheckMode = new AutoCheckMode();


    @Data
    public static class AutoCheckMode{
        /**
         * 是否开启
         */
        private boolean enabled = false;

        /**
         * 模式
         */
        private CaptchaAutoCheckModeEnum mode = CaptchaAutoCheckModeEnum.ASPECT;

        /**
         * 过滤器需要拦截的url
         */
        private List<String> filterUrls;

        /**
         * 请求参数中key的名称
         */
        private String keyName = "key";

        /**
         * 请求参数中验证码的名称
         */
        private String captchaName = "captcha";
    }

}
