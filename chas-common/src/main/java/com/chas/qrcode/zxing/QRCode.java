package com.chas.qrcode.zxing;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码(利用zxing生成方式)
 *
 * @author Chas Zeng
 * @email 894555763@qq.com
 * @date 2017/2/14
 */
public class QRCode {

    /**
     * 生成二维码到指定路径
     *
     * @param contents 内容
     * @param format 格式
     * @param path 路径
     * @param width 宽度
     * @param height 高度
     * @param hints 参数
     * @throws Exception
     */
    public static void encodeToFile(String contents, String format, String path, int width, int height,
                                    Map<EncodeHintType, Object> hints) throws Exception{
        writeToFile(contents,format,path,width,height,hints);
    }

    /**
     * 生成二维码到指定路径
     *
     * @param contents 内容
     * @param format 格式
     * @param path 路径
     * @param width 宽度
     * @param height 高度
     * @throws Exception
     */
    public static void encodeToFile(String contents, String format, String path, int width, int height) throws Exception{
        writeToFile(contents,format,path,width,height,setCodeParam());
    }

    /**
     * 生成二维码到流(可以直接输出到页面)
     *
     * @param contents 内容
     * @param format 格式
     * @param stream 输出流
     * @param width 宽度
     * @param height 高度
     * @param hints 参数
     * @throws Exception
     */
    public static void encodeToStream(String contents, String format, OutputStream stream, int width, int height,
                                      Map<EncodeHintType, Object> hints) throws Exception {
       writeToStream(contents,format,stream,width,height,hints);
    }

    /**
     * 生成二维码到流(可以直接输出到页面)
     *
     * @param contents 内容
     * @param format 格式
     * @param stream 输出流
     * @param width 宽度
     * @param height 高度
     * @throws Exception
     */
    public static void encodeToStream(String contents, String format, OutputStream stream, int width, int height) throws Exception {
        writeToStream(contents,format,stream,width,height,setCodeParam());
    }

    /**
     * 解析二维码
     *
     * @param file 二维码文件
     * @return 内容
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        String resultStr;
        if (null == file) {
            throw new Exception("this is file could not decode!");
        } else {
            BufferedImage bufferedImage = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            HybridBinarizer hybridBinarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
            //设置解析编码
            Map hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap,hints);
            resultStr = result.getText();
        }
        return resultStr;
    }

    /**
     * 定义二维码参数
     *
     * @return 二维码默认参数
     */
    private static HashMap<EncodeHintType, Object> setCodeParam(){
        //定义二维码参数
        HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        //设置字符编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //设置接纠错等级 L(7%)、M(15%)、Q(25%)、H(30%)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        //设置边距
        hints.put(EncodeHintType.MARGIN, 2);
        return hints;
    }

    private static void writeToFile (String contents, String format, String path, int width, int height,
                               Map<EncodeHintType, Object> hints) throws Exception{
        File file = new File(path);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,BarcodeFormat.QR_CODE,width,height,hints);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage,format,file);
    }

    private static void writeToStream(String contents, String format, OutputStream stream, int width, int height,
                                      Map<EncodeHintType, Object> hints) throws Exception{
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,BarcodeFormat.QR_CODE,width,height,hints);
        MatrixToImageWriter.writeToStream(bitMatrix,format,stream);
    }
}
