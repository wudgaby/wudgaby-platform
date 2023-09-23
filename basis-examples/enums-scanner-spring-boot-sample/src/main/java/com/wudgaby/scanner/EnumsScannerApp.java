package com.wudgaby.scanner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.enums.scanner.handler.EnumScanHandler;
import com.wudgaby.starter.enums.scanner.model.CodeItem;
import com.wudgaby.starter.enums.scanner.model.CodeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/23 0023 12:53
 * @desc :
 */
@RestController
@SpringBootApplication
public class EnumsScannerApp {
    public static void main(String[] args) {
        SpringApplication.run(EnumsScannerApp.class, args);
    }

    @Autowired
    private EnumScanHandler enumScanHandler;

    @GetMapping
    public ApiResult allEnums(){
        List<CodeTable> codeTableList = enumScanHandler.codeTables();
        Map<String, List<CodeItem>> enumRespMap = Maps.newHashMap();
        for(CodeTable ct : codeTableList){
            List<CodeItem> enumRespVOList = Lists.newArrayList();
            enumRespMap.put(ct.getEnumName(), enumRespVOList);
            ct.getItems().forEach(item -> {
                enumRespVOList.add(item);
            });
        }
        return ApiResult.success(enumRespMap);
    }

    @GetMapping("/byType")
    public ApiResult enumsByType(@RequestParam String type){
        List<CodeTable> codeTableList = enumScanHandler.codeTables();
        Map<String, CodeTable> map = codeTableList.stream().collect(Collectors.toMap(CodeTable::getEnumName, v -> v));
        CodeTable findCodeTable = map.get(type);
        if(findCodeTable == null){
            return ApiResult.success();
        }
        List<CodeItem> enumRespVOList = Lists.newArrayList();
        findCodeTable.getItems().forEach(item -> {
            enumRespVOList.add(item);
        });
        return ApiResult.success(enumRespVOList);
    }
}
