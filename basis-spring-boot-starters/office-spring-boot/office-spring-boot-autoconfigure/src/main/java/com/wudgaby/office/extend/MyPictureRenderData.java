package com.wudgaby.office.extend;

import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.util.BufferedImageUtils;
import com.deepoove.poi.util.ByteUtils;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/12/10 0010 14:03
 * @Desc :
 */
@Data
public class MyPictureRenderData {
    private static final long serialVersionUID = 1L;
    private int width;
    private int height;
    private byte[] image;
    private PictureType pictureType;
    private String altMeta;
    private Map<String, Object> extendMap;

    MyPictureRenderData() {
        this.altMeta = "";
    }

    public MyPictureRenderData(int width, int height, String path) {
        this(width, height, new File(path));
    }

    public MyPictureRenderData(int width, int height, File picture) {
        this(width, height, PictureType.suggestFileType(picture.getPath()), ByteUtils.getLocalByteArray(picture));
    }

    public MyPictureRenderData(int width, int height, PictureType pictureType, InputStream inputStream) {
        this(width, height, pictureType, ByteUtils.toByteArray(inputStream));
    }

    public MyPictureRenderData(int width, int height, PictureType pictureType, BufferedImage image) {
        this(width, height, pictureType, BufferedImageUtils.getBufferByteArray(image, pictureType.format()));
    }

    public MyPictureRenderData(int width, int height, PictureType pictureType, BufferedImage image, Map<String, Object> extendMap) {
        this(width, height, pictureType, BufferedImageUtils.getBufferByteArray(image, pictureType.format()));
        this.extendMap = extendMap;
    }

    public MyPictureRenderData(int width, int height, PictureType pictureType, byte[] data) {
        this.altMeta = "";
        this.width = width;
        this.height = height;
        this.pictureType = pictureType;
        this.image = data;
    }

}
