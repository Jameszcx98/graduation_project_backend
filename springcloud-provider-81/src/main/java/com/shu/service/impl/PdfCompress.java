package com.shu.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PdfCompress {
    /***
     *
     *
     *   使用方式
     *
     *   maven 项目中需要引入:
     *   <dependency>
     <groupId>org.apache.pdfbox</groupId>
     <artifactId>pdfbox</artifactId>
     <version>2.0.21</version>
     </dependency>

     * @param filePath    需要压缩的pdf路径
     * @param dpi    目标图片分辨率（数据类型为float）；值越大生成的图片分辨率越高，转换时间也就越长
     * @param quality    目标图片质量（数据类型为float）；值在[0,1]，越接近1质量越好
     */

    public  List<String> PDFtoImage(String filePath, float dpi, float quality)throws IOException {
        List<String> list = new ArrayList<>();
        String fileDirectory = filePath.substring(0,filePath.lastIndexOf("."));//获取去除后缀的文件路径
        //在该文件夹下保存转换得到的图片

        String imagePath;
        File file = new File(filePath);
        try {
            File f = new File(fileDirectory);
            if(!f.exists()){
                f.mkdir();
            }
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for(int i=0; i<pageCount; i++){
                // 第二个参数是设置缩放比(即像素)
                BufferedImage image = renderer.renderImage(i, dpi);
                imagePath = fileDirectory + "/"+i + ".jpg";       //图片存储路径
                ImageIO.write(image, "JPG", new File(imagePath));
                //利用Thumbnails对图片尺寸和大小的处理，scale中1f表示图片原尺寸，outputQuality中参数为图片质量
                Thumbnails.of(fileDirectory+"/"+i+".jpg").scale(1f).outputQuality(quality).toFile(fileDirectory+"/"+i+".jpg");
                list.add(imagePath);
            }
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *
     * @param outPdfFilepath 生成pdf文件路径
     * @param imageFiles 需要转换的图片File类Array,按array的顺序合成图片
     */
    public  void ImagetoPDF(String outPdfFilepath, File[] imageFiles) throws Exception {
        File file = new File(outPdfFilepath);
        if(file.exists())
            file.delete();
        // 第一步：创建一个document对象。
        Document document = new Document();
        document.setMargins(0, 0, 0, 0);
        // 第二步：
        // 创建一个PdfWriter实例，
        PdfWriter.getInstance(document, new FileOutputStream(file));
        // 第三步：打开文档。
        document.open();
        // 第四步：在文档中增加图片。
        int len = imageFiles.length;

        for (int i = 0; i < len; i++) {
            if (imageFiles[i].getName().toLowerCase().endsWith(".bmp")
                    || imageFiles[i].getName().toLowerCase().endsWith(".jpg")
                    || imageFiles[i].getName().toLowerCase().endsWith(".jpeg")
                    || imageFiles[i].getName().toLowerCase().endsWith(".gif")
                    || imageFiles[i].getName().toLowerCase().endsWith(".png")) {
                String temp = imageFiles[i].getAbsolutePath();

                File picture = new File(temp);
                BufferedImage sourceImg =ImageIO.read(new FileInputStream(picture));
                Image img = Image.getInstance(temp);
                img.setAlignment(Image.ALIGN_CENTER);
                img.scaleAbsolute(sourceImg.getWidth(), sourceImg.getHeight());// 直接设定显示尺寸
                // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                document.setPageSize(new Rectangle(sourceImg.getWidth(), sourceImg.getHeight()));
                document.newPage();
                document.add(img);
            }
        }
        // 第五步：关闭文档。
        document.close();
    }

    public  boolean deleteFile(String df) {
        File dirFile = new File(df);
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file.getAbsolutePath());
            }
        }

        return dirFile.delete();
    }

    public void start(String filePath,String OutPDFPath,String rate) throws Exception {
        //原pdf文件路径
//        String filePath="/Users/jameszhang/IdeaProjects/file-platform-backend/"+filePath1;
        log.info("输入文件地址"+filePath);
        log.info("输出文件地址"+OutPDFPath);
        float dpi=rate.equals("low")?1.5f:1.25f;
        float quality=rate.equals("low")?1f:Float.valueOf(rate);//0.75f
        List<String> imageList=PDFtoImage(filePath, dpi, quality);
        Iterator<String> iterator = imageList.iterator();

        String fileDirectory = filePath.substring(0,filePath.lastIndexOf("."));
        //目标pdf文件路径
//        String OutPDFPath=fileDirectory+"_compressed"+rate+".pdf";
        File d=new File(fileDirectory);
        File list[]=d.listFiles();
        File[] imgArray=new File[list.length];
        for(int i=0; i<list.length; i++) {
            File img=new File(fileDirectory+"/"+i+".jpg");
            imgArray[i]=img;
        }
        ImagetoPDF(OutPDFPath, imgArray);
        deleteFile(fileDirectory);
        log.info("文件："+filePath+"压缩完成");
    }
}
