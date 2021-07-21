package com.wudgaby.codegen.ui.service;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.wudgaby.codegen.ui.config.CodeGenProperties;
import com.wudgaby.codegen.ui.form.CodeDownLoadForm;
import com.wudgaby.platform.core.entity.BaseEntity;
import com.wudgaby.platform.utils.ZipUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @ClassName : GeneratorCodeService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/6/006 21:41
 * @Desc :
 */
@Slf4j
@Service
@AllArgsConstructor
public class GeneratorCodeService {
    private final DataSourceProperties dataSourceProperties;
    private final CodeGenProperties codeGenProperties;

    public void downloadZip(OutputStream outputStream, CodeDownLoadForm codeDownLoadForm) throws IOException{
        genCode(codeDownLoadForm);

        File outZip = new File(codeGenProperties.getZipPath());
        File genCodeDir = new File(codeGenProperties.getOutputPath());
        ZipUtil.deCompress(genCodeDir, codeGenProperties.getZipPath());

        try(InputStream in = new FileInputStream(outZip)){
            IOUtils.copy(in, outputStream);
        }
        FileUtils.deleteQuietly(genCodeDir);
        FileUtils.deleteQuietly(outZip);
    }

    public void genCode(CodeDownLoadForm codeDownLoadForm){
        GlobalConfig config = new GlobalConfig();

        String url = StringUtils.isNotBlank(codeDownLoadForm.getUrl()) ? StringUtils.trim(codeDownLoadForm.getUrl()) : dataSourceProperties.getUrl();
        String username = StringUtils.isNotBlank(codeDownLoadForm.getUsername()) ? StringUtils.trim(codeDownLoadForm.getUsername()) : dataSourceProperties.getUsername();
        String password = StringUtils.isNotBlank(codeDownLoadForm.getPassword()) ? StringUtils.trim(codeDownLoadForm.getPassword()) : dataSourceProperties.getPassword();

        if(!url.contains("?")){
            url = codeDownLoadForm.getUrl() + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false&allowMultiQueries=true&nullCatalogMeansCurrent=true";
        }

        //db配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setDriverName(dataSourceProperties.getDriverClassName());

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);

        //实体父类
        if(StringUtils.isNotBlank(codeDownLoadForm.getSuperEntityClass())){
            strategy.setSuperEntityClass(codeDownLoadForm.getSuperEntityClass());
            if (codeDownLoadForm.getSuperEntityClass().equals(ClassUtils.getName(BaseEntity.class))) {
                if(StringUtils.isNotBlank(codeDownLoadForm.getSuperEntityColumns())){
                    strategy.setSuperEntityColumns(codeDownLoadForm.getSuperEntityColumns().split(","));
                }else{
                    Field[] fields = ReflectUtil.getFields(BaseEntity.class);
                    String[] columns = Arrays.stream(fields).map(f -> StrUtil.toUnderlineCase(f.getName())).toArray(String[]::new);
                    strategy.setSuperEntityColumns(columns);
                }
            }else{
                if(StringUtils.isNotBlank(codeDownLoadForm.getSuperEntityColumns())){
                    strategy.setSuperEntityColumns(codeDownLoadForm.getSuperEntityColumns().split(","));
                }
            }
        }

        //表前缀
        if(StringUtils.isNotBlank(codeDownLoadForm.getTablePrefix())){
            strategy.setTablePrefix(codeDownLoadForm.getTablePrefix());
        }

        //字段前缀 无效果
        /*if(StringUtils.isNotBlank(codeDownLoadForm.getFieldPrefix())){
            strategy.setFieldPrefix(codeDownLoadForm.getFieldPrefix());
        }*/

        //未设置则所有表
        if(CollectionUtils.isNotEmpty(codeDownLoadForm.getTableNames())){
            strategy.setInclude(codeDownLoadForm.getTableNames().toArray(new String[0]));
        }

        //全局配置
        config.setActiveRecord(false)
                .setAuthor(codeGenProperties.getAuthor())
                .setOutputDir(codeGenProperties.getOutputPath())
                .setFileOverride(true)
                .setSwagger2(true)
                .setOpen(false)
                .setBaseColumnList(true)
                .setBaseResultMap(true)
                .setIdType(IdType.ASSIGN_ID)
                .setDateType(DateType.ONLY_DATE)
                .setServiceName("%sService")
        ;

        //包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(codeDownLoadForm.getBasePackage());

        if (StringUtils.isNotBlank(codeDownLoadForm.getControllerPackage())){
            packageConfig.setController(codeDownLoadForm.getControllerPackage());
        }

        if(StringUtils.isNotBlank(codeDownLoadForm.getEntityPackage())){
            packageConfig.setEntity(codeDownLoadForm.getEntityPackage());
        }

        if(StringUtils.isNotBlank(codeDownLoadForm.getServicePackage())){
            packageConfig.setService(codeDownLoadForm.getServicePackage());
        }

        if(StringUtils.isNotBlank(codeDownLoadForm.getServiceImplPackage())){
            packageConfig.setServiceImpl(codeDownLoadForm.getServiceImplPackage());
        }

        if(StringUtils.isNotBlank(codeDownLoadForm.getMapperPackage())){
            packageConfig.setMapper(codeDownLoadForm.getMapperPackage());
        }

        if(StringUtils.isNotBlank(codeDownLoadForm.getXmlPackage())){
            packageConfig.setXml(codeDownLoadForm.getXmlPackage());
        }


        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        AutoGenerator autoGenerator = new AutoGenerator().setGlobalConfig(config)
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .setDataSource(dataSourceConfig)
                .setStrategy(strategy)
                .setCfg(cfg)
                //.setTemplate(tmplConfig)
                .setPackageInfo(packageConfig);

        autoGenerator.execute();
    }
}
