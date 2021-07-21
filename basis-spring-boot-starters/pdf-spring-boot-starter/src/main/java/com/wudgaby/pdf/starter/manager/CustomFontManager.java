package com.wudgaby.pdf.starter.manager;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @ClassName : CustomFontManager
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/7 10:42
 * @Desc :
 */
@Slf4j
public class CustomFontManager {
    private CustomFontManager(){}
    private static Map<String, String> fontMap = Maps.newHashMap();
    private static Map<String, byte[]> resourceFontMap = Maps.newHashMap();

    public static void init() throws IOException {
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(null)
                                .getResources( "classpath*:font/*");

        for(Resource res : resources){
            File file = res.getFile();
            log.info("加载字体: {}, {}", file.getName(), file.getAbsolutePath());
            try (InputStream fis = res.getInputStream()) {
                byte[] data = IOUtils.toByteArray(fis);
                resourceFontMap.put(FilenameUtils.removeExtension(file.getName()), data);
            }
        }
    }

    public static void initFromFileSys(String path){
        File tempFileDir = new File(path);
        if(!tempFileDir.exists()){
            return;
        }

        for(File file : tempFileDir.listFiles()){
            log.info("加载字体: {}, {}", file.getName(), file.getAbsolutePath());
            fontMap.put(FilenameUtils.removeExtension(file.getName()), file.getAbsolutePath());
        }
    }

    public static String getFontPath(String fontName){
        return fontMap.get(fontName);
    }

}
