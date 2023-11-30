package com.wudgaby.platform.utils.qr;

import com.google.common.base.Joiner;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 二维码 工具类
 * <p>
 * 说明: 二维码是一种编码形式， 将内容encode，得到二维码;， 将内容decode,得到数据内容。
 *
 * @author wudgaby
 */
@Slf4j
@UtilityClass
@SuppressWarnings("unused")
public class QrCodeUtil {

    /**
     * 嵌入的图片(被程序处理后)的高度(px)
     * <p>
     * 注:内嵌图片不应占据遮挡二维码太多，否者可能导致生成的二维码无法识别。 建议内嵌图片的尺寸设置为二维码尺寸的1/5 或 1/4
     */
    private static int embeddedImgDefaultWidth;

    /** 嵌入的图片(被程序处理后)的高度(px) */
    private static int embeddedImgDefaultHeight;

    /** 嵌入的图片与二维码图片之间的框的宽度(px) */
    private static int frameWidth;

    /** 嵌入的图片与二维码图片之间的框的颜色. */
    private static int frameWidthColor;

    /** 二维码 码的颜色 */
    private static int qrCodeColor;

    /** 二维码 背景的颜色 */
    private static int qrCodeBackgroundColor;

    /** 图片缩小后，对那些"空"下的区域补色 */
    private static int fillColor;

    /** 二维码写码器 */
    private static final MultiFormatWriter MULTI_FORMAT_WRITER = new MultiFormatWriter();

    /**解析一张图多个二维码*/
    private static final QRCodeMultiReader QR_CODE_MULTI_READER = new QRCodeMultiReader();

    /*
     * 初始化基本参数
     */
    static {
        QrCodeUtil.embeddedImgDefaultWidth = 80;
        QrCodeUtil.embeddedImgDefaultHeight = 80;
        QrCodeUtil.frameWidth = 0;
        QrCodeUtil.frameWidthColor = Color.BLUE.getRGB();
        QrCodeUtil.qrCodeColor = Color.BLACK.getRGB();
        QrCodeUtil.qrCodeBackgroundColor = Color.WHITE.getRGB();
        QrCodeUtil.fillColor = Color.RED.getRGB();
    }

    /**
     * 设置修改基本参数的参数值
     *
     * @param embeddedImgDefaultWidth  嵌入的图片(被程序处理后)的高度(px)
     * @param embeddedImgDefaultHeight 嵌入的图片(被程序处理后)的高度(px)
     * @param frameWidth               嵌入的图片与二维码图片之间的框的宽度(px)
     * @param frameWidthColor          嵌入的图片与二维码图片之间的框的颜色
     * @param qrCodeColor              二维码 码的颜色
     * @param qrCodeBackgroundColor    二维码 背景的颜色
     * @param fillColor                图片缩小后，对那些"空"下的区域补色
     * @date 2019/9/9 22:47
     */
    public static void modifyBasicParamsValues(Integer embeddedImgDefaultWidth, Integer embeddedImgDefaultHeight,
                                               Integer frameWidth, Color frameWidthColor, Color qrCodeColor, Color qrCodeBackgroundColor,
                                               Color fillColor) {
        if (embeddedImgDefaultWidth != null) {
            QrCodeUtil.embeddedImgDefaultWidth = embeddedImgDefaultWidth;
        }
        if (embeddedImgDefaultHeight != null) {
            QrCodeUtil.embeddedImgDefaultHeight = embeddedImgDefaultHeight;
        }
        if (frameWidth != null) {
            QrCodeUtil.frameWidth = frameWidth;
        }
        if (frameWidthColor != null) {
            QrCodeUtil.frameWidthColor = frameWidthColor.getRGB();
        }
        if (qrCodeColor != null) {
            QrCodeUtil.qrCodeColor = qrCodeColor.getRGB();
        }
        if (qrCodeBackgroundColor != null) {
            QrCodeUtil.qrCodeBackgroundColor = qrCodeBackgroundColor.getRGB();
        }
        if (fillColor != null) {
            QrCodeUtil.fillColor = fillColor.getRGB();
        }
    }

    /**
     * 生成二维码(到destImagePath指向的File) --- 方式一
     *
     * @param content       二维码的内容
     * @param width         二维码的宽度(px)
     * @param height        二维码的高度(px)
     * @param destImagePath 生成二维码图片的地址
     *
     * @return 生成的二维码文件path
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2019/9/9 16:43
     */
    public static String encodeQrCode(String content, int width, int height, String destImagePath)
            throws IOException, WriterException {

        File dest = getFile(destImagePath);
        // 图像类型
        String format = "jpg";
        Map<EncodeHintType, Object> hints = new HashMap<>(4);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try (FileOutputStream output = new FileOutputStream(dest)) {
            // 生成矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            // 输出图像
            MatrixToImageWriter.writeToStream(bitMatrix, format, output);
        }
        return destImagePath;
    }

    /**
     * 生成二维码(到destImagePath指向的File) --- 方式二
     *
     * @param content       二维码的内容
     * @param width         二维码的宽度(px)
     * @param height        二维码的高度(px)
     * @param destImagePath 生成二维码图片的地址
     *
     * @return 生成的二维码文件path
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2019/9/9 16:43
     */
    public static String encodeQrCodeAnotherWay(String content, int width, int height, String destImagePath)
            throws IOException, WriterException {
        boolean result = ImageIO.write(genBufferedImage(content, width, height), "jpg", getFile(destImagePath));
        log.info(" generate Qr code file {} {}", destImagePath, result ? "success" : "fail");
        return destImagePath;
    }

    /**
     * 生成带图片的二维码(到destImagePath指向的File)
     *
     * @param content         二维码的内容
     * @param width           二维码的宽度(px)
     * @param height          二维码的高度(px)
     * @param embeddedImgPath 被镶嵌的图片的地址
     * @param destImagePath   生成二维码图片的地址
     *
     * @return 生成的二维码文件path
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2019/9/9 15:13
     */
    public static String encodeQrCodeWithEmbeddedImg(String content, int width, int height, String embeddedImgPath,
                                                     String destImagePath) throws IOException, WriterException {
        boolean result = ImageIO.write(genBufferedImageWithEmbeddedImg(content, width, height, embeddedImgPath), "jpg",
                getFile(destImagePath));
        log.info(" generate Qr code file {} {}", destImagePath, result ? "success" : "fail");
        return destImagePath;
    }

    /**
     * 生成带图片的二维码(到outputStream流)
     *
     * @param content         二维码的内容
     * @param width           二维码的宽度(px)
     * @param height          二维码的高度(px)
     * @param embeddedImgPath 被镶嵌的图片的地址 注:被镶嵌的图片,如果尺寸
     *
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2019/9/9 16:43
     */
    public static void encodeQrCodeWithEmbeddedImg(String content, int width, int height, String embeddedImgPath,
                                                   OutputStream outputStream) throws IOException, WriterException {
        boolean result = ImageIO.write(genBufferedImageWithEmbeddedImg(content, width, height, embeddedImgPath), "jpg",
                outputStream);
        log.info(" generate Qr code file to OutputStream {}", result ? "success" : "fail");
    }

    /**
     * 生成上方带内嵌图片、带文字的二维码
     * <p>
     * 注:若 嵌入的图片的参数值为null 或者为 空的字符，那么 只生成带文字的二维码
     *
     * @param param 参数模型
     *
     * @return 生成的二维码文件path
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2019/9/10 0:11
     */
    public static String encodeQrCodeWithEmbeddedImgAndFonts(QrImage param) throws IOException, WriterException {
        // 首先生成二维码图片
        BufferedImage qrImage;
        String embeddedImgFilePath = param.getEmbeddedImgFilePath();
        String qrCodeContent = param.getQrCodeContent();
        Integer qrCodeWidth = param.getQrCodeWidth();
        Integer qrCodeHeight = param.getQrCodeHeight();
        if (embeddedImgFilePath == null || embeddedImgFilePath.trim().isEmpty()) {
            qrImage = genBufferedImage(qrCodeContent, qrCodeWidth, qrCodeHeight);
        } else {
            qrImage = genBufferedImageWithEmbeddedImg(qrCodeContent, qrCodeWidth, qrCodeHeight, embeddedImgFilePath);
        }
        int qrCodeImageWidth = qrImage.getWidth();
        int qrCodeImageHeight = qrImage.getHeight();

        String[] splitStrLines;
        splitStrLines = splitStrLines(param.getWordContent(), qrCodeImageWidth / param.getWordSize());
        // 文字图片的高度 = 文字行数 * 每行高度(文字高度) + 10px;
        // 为了防止 文字图片 下面部分显示不全， 特意在这里额外加10像素的高度。
        int fontsImageHeight = splitStrLines.length * param.getWordSize() + 10;
        // 创建顶部文字的图片
        BufferedImage imageWithFontsBufferedImage = new BufferedImage(qrCodeImageWidth, fontsImageHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics fontsImageGraphics = imageWithFontsBufferedImage.getGraphics();
        fontsImageGraphics.fillRect(0, 0, qrCodeImageWidth, fontsImageHeight);
        fontsImageGraphics.setColor(Color.black);
        fontsImageGraphics.setFont(new Font("宋体", Font.PLAIN, param.getWordSize()));

        // 文字长度大于一行的长度，进行分行
        // 每行限制的文字个数
        if (param.getWordContent().length() > qrCodeImageWidth / param.getWordSize()) {
            for (int i = 0; i < splitStrLines.length; i++) {
                fontsImageGraphics.drawString(splitStrLines[i], 0, param.getWordSize() * (i + 1));
            }
        } else {
            fontsImageGraphics.drawString(param.getWordContent(),
                    // 总长度减去字长度除以2为向右偏移长度
                    ((qrCodeImageWidth / param.getWordSize() - param.getWordContent().length()) / 2)
                            * param.getWordSize(),
                    param.getWordSize());
        }

        // 从图片中读取RGB
        int[] imageArrayFonts = new int[qrCodeImageWidth * fontsImageHeight];
        imageArrayFonts = imageWithFontsBufferedImage.getRGB(0, 0, qrCodeImageWidth, fontsImageHeight, imageArrayFonts,
                0, qrCodeImageWidth);

        int[] imageArrayQr = new int[qrCodeImageWidth * qrCodeImageHeight];
        imageArrayQr = qrImage.getRGB(0, 0, qrCodeImageWidth, qrCodeImageHeight, imageArrayQr, 0, qrCodeImageWidth);

        // 生成新图片(在setsetRGB时，通过控制图像的startX与startY, 可达到不同的拼接效果)
        BufferedImage newBufferedImage = new BufferedImage(qrCodeImageWidth, qrCodeImageHeight + fontsImageHeight,
                BufferedImage.TYPE_INT_RGB);
        // 图片在上， 文字在下
        // 设置上半部分的RGB
        newBufferedImage.setRGB(0, 0, qrCodeImageWidth, qrCodeImageHeight, imageArrayQr, 0, qrCodeImageWidth);
        // 设置下半部分的RGB
        newBufferedImage.setRGB(0, qrCodeImageHeight, qrCodeImageWidth, fontsImageHeight, imageArrayFonts, 0,
                qrCodeImageWidth);
        // 文字在上，图片在下
        // // 设置上半部分的RGB
        /// newBufferedImage.setRGB(0, 0, qrCodeImageWidth, fontsImageHeight,
        // imageArrayFonts, 0, qrCodeImageWidth);
        // // 设置下半部分的RGB
        /// newBufferedImage.setRGB(0, fontsImageHeight, qrCodeImageWidth,
        // qrCodeImageHeight, imageArrayQr, 0, qrCodeImageWidth);

        String qrCodeFileOutputPath = param.getQrCodeFileOutputPath();
        File outFile = getFile(qrCodeFileOutputPath);
        boolean result = ImageIO.write(newBufferedImage, "jpg", outFile);
        log.info(" generate Qr code file {} {}", qrCodeFileOutputPath, result ? "success" : "fail");
        return qrCodeFileOutputPath;

    }

    /**
     * 识别二维码内容信息
     *
     * @param file 二维码图片文件
     *
     * @return 二维码内容
     * @throws NotFoundException NotFoundException
     * @throws IOException       IOException
     * @date 2019/9/10 1:59
     */
    public static String decodeQrCode(File file) throws NotFoundException, IOException {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        String data = decodeQrCode(image);
        log.info(" Qr code from [{}] data is -> {}", file.getAbsolutePath(), data);
        return data;
    }

    /**
     * 识别二维码内容信息
     *
     * @param is 二维码图片文件流
     *
     * @return 二维码内容
     * @throws NotFoundException NotFoundException
     * @throws IOException       IOException
     * @date 2019/9/10 1:59
     */
    public static String decodeQrCode(InputStream is) throws NotFoundException, IOException {
        BufferedImage image;
        image = ImageIO.read(is);
        if (image == null) {
            return null;
        }
        String data = decodeQrCode(image);
        log.info(" Qr code from InputStream data is -> {}", data);
        return data;
    }

    /**
     * 识别多个二维码
     * @param file
     * @return
     * @throws NotFoundException
     * @throws IOException
     */
    public static Result[] decodeMultiQCodes(File file) throws NotFoundException, IOException {
        return decodeMultiQCodes(ImageIO.read(file));
    }

    public static Result[] decodeMultiQCodes(InputStream is) throws NotFoundException, IOException {
        return decodeMultiQCodes(ImageIO.read(is));
    }

    public static Result[] decodeMultiQCodes(BufferedImage image) throws NotFoundException, IOException {
        if(image == null){
            log.error("无效图片");
            return null;
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap binarybitmap = new BinaryBitmap(new HybridBinarizer(source));

        Map<DecodeHintType, Object> hints = new HashMap<>(8);
        hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        //优化精度
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        //复杂模式，开启PURE_BARCODE模式
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

        Result[] results = QR_CODE_MULTI_READER.decodeMultiple(binarybitmap, hints);
        if(ArrayUtils.isNotEmpty(results)){
            String text = Joiner.on(",").join(Arrays.stream(results).map(Result::getText).collect(Collectors.toList())).trim();
            log.info("Qr code data count: {} is  -> {}", results.length, text);
        }
        return results;
    }

    /// --------------------------------------------------------以下为辅助方法、辅助类

    /**
     * 获取文件(顺带创建文件夹，如果需要的话)
     *
     * @param filePath 文件path
     * @return 文件对象
     * @date 2019/9/10 10:48
     */
    private static File getFile(String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            boolean result = file.getParentFile().mkdirs();
            log.info(" create directory {} {}", file.getParent(), result);
        }
        return file;
    }

    /**
     * 识别二维码内容信息
     *
     * @param image 二维码图片信息BufferedImage
     *
     * @return 二维码内容
     * @throws NotFoundException NotFoundException
     * @date 2019/9/10 1:59
     */
    private static String decodeQrCode(BufferedImage image) throws NotFoundException {
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        HashMap<DecodeHintType, Object> hints = new HashMap<>(4);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 生成二维码图片信息BufferedImage
     * <p>
     * 注:BufferedImage即为二维码图片的数据容器，可以利用其进一步生成二维码图片
     *
     * @param content 二维码的数据内容
     * @param width   二维码的宽(px)
     * @param height  二维码的高(px)
     *
     * @return 二维码图片信息BufferedImage
     * @throws WriterException WriterException
     * @date 2019/9/9 16:39
     */
    private static BufferedImage genBufferedImage(String content, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = getHints();
        // 生成一个矩阵(即:生成一个二维数组)
        BitMatrix matrix;
        matrix = MULTI_FORMAT_WRITER.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        int[] pixels = new int[width * height];
        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // 控制二维码颜色, 前面那个为 二维码的颜色， 后面那个为底色
                pixels[y * width + x] = matrix.get(x, y) ? qrCodeColor : qrCodeBackgroundColor;
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);
        return image;
    }

    /**
     * 生成(带内嵌图片的)二维码图片信息BufferedImage
     * <p>
     * 注:BufferedImage即为二维码图片的数据容器，可以利用其进一步生成二维码图片
     *
     * @param content           二维码的数据内容
     * @param width             二维码的宽(px)
     * @param height            二维码的高(px)
     * @param embeddedImagePath 二维码中要嵌套的图片path
     *
     * @return 二维码图片信息BufferedImage
     * @throws IOException     IOException
     * @throws WriterException WriterException
     * @date 2019/9/9 16:39
     */
    private static BufferedImage genBufferedImageWithEmbeddedImg(String content, int width, int height,
                                                                 String embeddedImagePath) throws WriterException, IOException {
        // 读取要嵌套的图像，并将其压缩到指定的宽高
        BufferedImage scaleImage = scale(embeddedImagePath, embeddedImgDefaultWidth, embeddedImgDefaultHeight, false);
        int embeddedImgFinalWidth = scaleImage.getWidth();
        int embeddedImgFinalHalfWidth = embeddedImgFinalWidth / 2;
        int embeddedImgFinalHeight = scaleImage.getHeight();
        int embeddedImgFinalHalfHeight = embeddedImgFinalHeight / 2;
        int[][] srcPixels = new int[embeddedImgFinalWidth][embeddedImgFinalHeight];
        for (int i = 0; i < embeddedImgFinalWidth; i++) {
            for (int j = 0; j < embeddedImgFinalHeight; j++) {
                srcPixels[i][j] = scaleImage.getRGB(i, j);
            }
        }
        Map<EncodeHintType, Object> hint = getHints();
        // 生成一个矩阵(即:生成一个二维数组)( 注:下面会在这个矩阵上绘图)
        BitMatrix matrix = MULTI_FORMAT_WRITER.encode(content, BarcodeFormat.QR_CODE, width, height, hint);
        // 二维码矩阵转为一维像素数组
        int qrCodeHalfWidth = matrix.getWidth() / 2;
        int qrCodeHalfHeight = matrix.getHeight() / 2;
        int[] pixels = new int[width * height];
        // 是否处于【嵌入图片 与 二维码图片 之间的框】里
        boolean atEmbeddedImgFrameArea;
        // 是否处于【嵌入图片的位置】里
        boolean atEmbeddedImgArea;
        // 内嵌的图片(左下角) 在 二维码图片 中的坐标X
        int embeddedImgCoordinatesX = qrCodeHalfWidth - embeddedImgFinalHalfWidth;
        // 内嵌的图片(左下角) 在 二维码图片 中的坐标Y
        int embeddedImgCoordinatesY = qrCodeHalfHeight - embeddedImgFinalHalfHeight;
        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {

                atEmbeddedImgArea = x > qrCodeHalfWidth - embeddedImgFinalHalfWidth
                        && x < qrCodeHalfWidth + embeddedImgFinalHalfWidth
                        && y > qrCodeHalfHeight - embeddedImgFinalHalfHeight
                        && y < qrCodeHalfHeight + embeddedImgFinalHalfHeight;

                atEmbeddedImgFrameArea = (x > qrCodeHalfWidth - embeddedImgFinalHalfWidth - frameWidth
                        && x < qrCodeHalfWidth - embeddedImgFinalHalfWidth + frameWidth
                        && y > qrCodeHalfHeight - embeddedImgFinalHalfHeight - frameWidth
                        && y < qrCodeHalfHeight + embeddedImgFinalHalfHeight + frameWidth)

                        ||

                        (x > qrCodeHalfWidth + embeddedImgFinalHalfWidth - frameWidth
                                && x < qrCodeHalfWidth + embeddedImgFinalHalfWidth + frameWidth
                                && y > qrCodeHalfHeight - embeddedImgFinalHalfHeight - frameWidth
                                && y < qrCodeHalfHeight + embeddedImgFinalHalfHeight + frameWidth)

                        ||

                        (x > qrCodeHalfWidth - embeddedImgFinalHalfWidth - frameWidth
                                && x < qrCodeHalfWidth + embeddedImgFinalHalfWidth + frameWidth
                                && y > qrCodeHalfHeight - embeddedImgFinalHalfHeight - frameWidth
                                && y < qrCodeHalfHeight - embeddedImgFinalHalfHeight + frameWidth)

                        ||

                        (x > qrCodeHalfWidth - embeddedImgFinalHalfWidth - frameWidth
                                && x < qrCodeHalfWidth + embeddedImgFinalHalfWidth + frameWidth
                                && y > qrCodeHalfHeight + embeddedImgFinalHalfHeight - frameWidth
                                && y < qrCodeHalfHeight + embeddedImgFinalHalfHeight + frameWidth);

                // 在二维码矩阵数像素组里 嵌入 嵌入图片的像素数组
                if (atEmbeddedImgArea) {
                    pixels[y * width + x] = srcPixels[x - embeddedImgCoordinatesX][y - embeddedImgCoordinatesY];
                }
                // 嵌入图片 与 二维码图片 之间的框(设置其颜色)
                else if (atEmbeddedImgFrameArea) {
                    pixels[y * width + x] = frameWidthColor;
                    // 二维码图片区域
                } else {
                    // 控制二维码颜色, 前面那个为 二维码的颜色， 后面那个为底色
                    pixels[y * width + x] = matrix.get(x, y) ? qrCodeColor : qrCodeBackgroundColor;
                }
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);
        return image;
    }

    /**
     * 获取hint信息
     *
     * @return hint
     * @date 2019/9/10 1:14
     */
    private static Map<EncodeHintType, Object> getHints() {
        Map<EncodeHintType, Object> hint = new HashMap<>(8);
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 二维码整体白框
        hint.put(EncodeHintType.MARGIN, 1);
        return hint;
    }

    /**
     * 把传入的原始图像按高度(或宽度)进行缩小，生成符合要求的图标的BufferedImage信息
     * <p>
     * 注:若图片原来的大小比给定的宽高小，那么不会进行放大，而是仍然保持原来的大小。
     *
     * @param srcImageFile 源文件地址
     * @param width        (缩小后的)宽度
     * @param height       (缩小后的)高度
     * @param autoFill     是否对按比例缩小后的图片进行填充，使其完全达到给定的宽高。
     *                     <p>
     *                     注:图片按照最长边与对应的宽(或高)的比例缩小后,最长边缩小后的尺寸与给定的宽(或高)一致了，
     *                     但是其高(或宽)，与缩小后的最短边不一定一样。此时，我们可以考虑使用给定的颜色对 其进行填充。如:
     *                     想要嵌入进去的图片的实际宽是80， 高是100； 但是这里指定缩小后的尺寸为 宽为10，高为10。
     *                     由下面的逻辑可知:缩小比例为0.1， 缩小后，图片的宽就变成了8，高就变
     *                     成了10，此时高是满足我们的要求的，但是宽差了2个像素。此时就可以将其进行填充了。
     *                     <p>
     *                     注:建议为false。
     *
     * @return 图片的BufferedImage信息
     * @throws IOException IOException
     * @date 2019/9/10 1:59
     */
    private static BufferedImage scale(String srcImageFile, int width, int height, boolean autoFill)
            throws IOException {
        // 缩小比例
        double ratio;
        File file = getFile(srcImageFile);
        BufferedImage srcImage = ImageIO.read(file);
        Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        // 如果原图片的高或者宽 大于 指定的高或宽时，(以最长的一边来计算缩放比例，并)进行缩放
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
            if (srcImage.getHeight() > srcImage.getWidth()) {
                ratio = (new Integer(height)).doubleValue() / srcImage.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue() / srcImage.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            destImage = op.filter(srcImage, null);
        }
        if (autoFill) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = image.createGraphics();
            graphic.setColor(Color.red);
            graphic.fillRect(0, 0, width, height);
            if (width == destImage.getWidth(null)) {
                graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null),
                        destImage.getHeight(null), Color.white, null);
            } else {
                graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null),
                        destImage.getHeight(null), Color.white, null);
            }
            graphic.dispose();
            destImage = image;
        }
        return (BufferedImage) destImage;
    }

    /**
     * 一行字符串拆分成多行
     */
    private static String[] splitStrLines(String str, int len) {
        int blocks = str.length() / len + 1;
        String[] strArray = new String[blocks];
        for (int i = 0; i < blocks; i++) {
            if ((i + 1) * len > str.length()) {
                strArray[i] = str.substring(i * len);
            } else {
                strArray[i] = str.substring(i * len, (i + 1) * len);
            }
        }
        return strArray;
    }

    /**
     * Writes a {@link BitMatrix} to {@link BufferedImage}, file or stream. Provided
     * here instead of core since it depends on Java SE libraries.
     */
    private static class MatrixToImageWriter {

        private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

        private MatrixToImageWriter() {
        }

        /**
         * Renders a {@link BitMatrix} as an image, where "false" bits are rendered as
         * white, and "true" bits are rendered as black.
         */
        private static BufferedImage toBufferedImage(BitMatrix matrix) {
            return toBufferedImage(matrix, DEFAULT_CONFIG);
        }

        /**
         * As {@link #toBufferedImage(BitMatrix)}, but allows customization of the
         * output.
         */
        private static BufferedImage toBufferedImage(BitMatrix matrix, MatrixToImageConfig config) {
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, config.getBufferedImageColorModel());
            int onColor = config.getPixelOnColor();
            int offColor = config.getPixelOffColor();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
                }
            }
            return image;
        }

        /**
         * @deprecated use {@link #writeToPath(BitMatrix, String, Path)}
         */
        @Deprecated
        private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
            writeToPath(matrix, format, file.toPath());
        }

        /**
         * Writes a {@link BitMatrix} to a file.
         *
         * @see #toBufferedImage(BitMatrix)
         */
        private static void writeToPath(BitMatrix matrix, String format, Path file) throws IOException {
            writeToPath(matrix, format, file, DEFAULT_CONFIG);
        }

        /**
         * @deprecated use
         *             {@link #writeToPath(BitMatrix, String, Path, MatrixToImageConfig)}
         */
        @Deprecated
        private static void writeToFile(BitMatrix matrix, String format, File file, MatrixToImageConfig config)
                throws IOException {
            writeToPath(matrix, format, file.toPath(), config);
        }

        /**
         * As {@link #writeToFile(BitMatrix, String, File)}, but allows customization of
         * the output.
         */
        @SuppressWarnings("all")
        private static void writeToPath(BitMatrix matrix, String format, Path file, MatrixToImageConfig config)
                throws IOException {
            BufferedImage image = toBufferedImage(matrix, config);
            if (!ImageIO.write(image, format, file.toFile())) {
                throw new IOException("Could not write an image of format " + format + " to " + file);
            }
        }

        /**
         * Writes a {@link BitMatrix} to a stream.
         *
         * @see #toBufferedImage(BitMatrix)
         */
        private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
            writeToStream(matrix, format, stream, DEFAULT_CONFIG);
        }

        /**
         * As {@link #writeToStream(BitMatrix, String, OutputStream)}, but allows
         * customization of the output.
         */
        private static void writeToStream(BitMatrix matrix, String format, OutputStream stream,
                                          MatrixToImageConfig config) throws IOException {
            BufferedImage image = toBufferedImage(matrix, config);
            if (!ImageIO.write(image, format, stream)) {
                throw new IOException("Could not write an image of format 【" + format + "】");
            }
        }

        /**
         * 配置类封装
         * <p>
         * Encapsulates custom configuration used in methods of
         * {@link MatrixToImageWriter}.
         */
        private static class MatrixToImageConfig {

            private static final int BLACK = Color.BLACK.getRGB();

            private static final int WHITE = Color.WHITE.getRGB();

            private final int onColor;

            private final int offColor;

            /**
             * Creates a default config with on color {@link #BLACK} and off color
             * {@link #WHITE}, generating normal black-on-white barcodes.
             */
            private MatrixToImageConfig() {
                this(BLACK, WHITE);
            }

            /**
             * @param onColor  pixel on color, specified as an ARGB value as an int
             * @param offColor pixel off color, specified as an ARGB value as an int
             */
            private MatrixToImageConfig(int onColor, int offColor) {
                this.onColor = onColor;
                this.offColor = offColor;
            }

            private int getPixelOnColor() {
                return onColor;
            }

            private int getPixelOffColor() {
                return offColor;
            }

            private int getBufferedImageColorModel() {
                // Use faster BINARY if colors match default
                return onColor == BLACK && offColor == WHITE ? BufferedImage.TYPE_BYTE_BINARY
                        : BufferedImage.TYPE_INT_RGB;
            }
        }
    }

}