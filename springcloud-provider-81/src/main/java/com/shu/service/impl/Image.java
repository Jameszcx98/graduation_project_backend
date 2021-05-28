package com.shu.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
@Service
public class Image {
    public void work(String tmpFile,String outFile,String rate) throws Exception {
        log.info("输入文件地址"+tmpFile);
        log.info("输出文件地址"+outFile);
//        String tmpFile = "urlDownload.tmp";	// url下载保存路径
//        String tmpFile="/Users/jameszhang/IdeaProjects/file-platform-backend/"+tmpFile1;
//        String outFile = tmpFile.substring(0,tmpFile.lastIndexOf("."))+"_compressed"+rate+".jpg";	// 输出文件路径
//        String url = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1089874897,1268118658&fm=26&gp=0.jpg";
//        download(url, tmpFile);			// 从url下载图片，保存到tmpfile
        File input = new File(tmpFile);  	// 输入文件(无需给定正确文件后缀)
        BufferedImage image = ImageIO.read(input);	// 从指定文件读取图像
//        BufferedImage resized = resize(image, 1024, 768);	// 按分辨率缩放图像
        BufferedImage resized = resize(image, Float.valueOf(rate));	// 按比例缩放图像
        File output = new File(outFile);  	// 输出文件
        saveImage(resized, "jpg", output, 0.7f);	// 保存图像到指定文件
    }

//    //从url下载图片
//    private  void download(String urlString, String saveFile) throws Exception {	// 构造URL
//        URL url = new URL(urlString);	// 打开连接
//        URLConnection con = url.openConnection();	// 输入流
//        InputStream is = con.getInputStream();	// 1K的数据缓冲
//        byte[] bs = new byte[1024];	// 读取到的数据长度
//        int len;	// 输出的文件流
//        File file = new File(saveFile);
//        FileOutputStream os = new FileOutputStream(file, true);	// 开始读取
//        while ((len = is.read(bs)) != -1) {
//            os.write(bs, 0, len);
//        }	// 完毕，关闭所有链接
//        os.close();
//        is.close();
//    }

    //通用图像保存
    private  void saveImage(BufferedImage image, String formatName, File outFile, float quality) throws IOException {
        // 输入: BufferedImage类型的待保存图像, String类型保存格式, File类型保存目标文件
        // 输出: 无输出, 保存图像到指定路径
        if (formatName.equalsIgnoreCase("jpg") || formatName.equalsIgnoreCase("jpeg")) { //重画jpeg图像
            BufferedImage tag;
            tag = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            image = tag;
            saveJpegImage(image, outFile, quality);	// 按给定参数保存压缩后的jpeg图像
        }
        else {
            ImageIO.write(image, formatName, outFile);	// 非jpeg格式直接按给定格式保存
        }
    }

    //jpeg压缩保存
    private  void saveJpegImage(BufferedImage image,File jpegOutFile,float quality) throws IOException {
        // 指定图片质量保存jpeg图像.1f代表质量为100%,0.5f代表50%.
        // 输入: BufferedImage类型的待保存图像, File类型保存目标文件, float类型压缩质量
        // 输出: 无输出, 保存jpeg到指定路径
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(quality);	// 设置压缩质量
        jpgWriter.setOutput(ImageIO.createImageOutputStream(jpegOutFile));
        IIOImage outputImage = new IIOImage(image, null, null);
        jpgWriter.write(null, outputImage, jpgWriteParam);
        jpgWriter.dispose();
    }

    // 不按比例缩放
    private  BufferedImage resize(BufferedImage img, int width, int height) {
        // 输入: BufferedImage类型的待缩放图像, int型新长, int型新宽
        // 输出: BufferedImage类型的缩放后图像
        java.awt.Image tmp = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    // 按比例缩放
    private  BufferedImage resize(BufferedImage img, float scaleFactor) {
        // 输入: BufferedImage类型的待缩放图像, float型缩放参数
        // 输出: BufferedImage类型的缩放后图像
        int ori_width = img.getWidth();
        int ori_height = img.getHeight();
        int new_width = (int)(scaleFactor * ori_width);
        int new_height = (int)(scaleFactor * ori_height);
        java.awt.Image tmp = img.getScaledInstance(new_width, new_height, java.awt.Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
