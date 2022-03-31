package com.wudgaby.sample;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/31 0031 18:18
 * @desc :
 */
@Data
@Schema(name = "测试模块名称")
public class DemoModel {
    @Schema(name = "姓名", required = true)
    private String name;

    @Schema(name = "密码", accessMode = Schema.AccessMode.READ_ONLY)
    private String password;
}
