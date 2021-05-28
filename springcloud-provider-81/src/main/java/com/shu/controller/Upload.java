package com.shu.controller;
import com.shu.constant.Result;
import com.shu.constant.ShopCode;
import com.shu.utils.IDWorker;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.UUID;
@Slf4j
@RestController
@Api(tags = "")
public class Upload {


    @Value("${file.upload}")
    private String filePath;

    @Autowired
    private IDWorker idWorker;

    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) throws Exception{
        //如果这里没有设置路径的话，会保存在项目的根目录下
        log.info("接收到文件---"+file.getOriginalFilename());
        String id=String.valueOf(idWorker.nextId());
        String name=filePath+id+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String name1=id+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //获取文件原始的后缀名
        BufferedOutputStream outputStream=new BufferedOutputStream(new FileOutputStream(name));
        outputStream.write(file.getBytes());
        outputStream.flush();
        outputStream.close();
        log.info("成功存储文件--->"+name);
        return new Result(ShopCode.SHOP_SUCCESS.getCode(),name1);
    }
    @GetMapping("/download")
    public ResponseEntity download(@RequestParam String file) throws Exception{
        FileSystemResource fileSystemResource = new FileSystemResource(filePath+file);
        HttpHeaders headers=new HttpHeaders();
        //在响应头中添加这个，设置下载文件默认的名称
        headers.add("Content-Disposition","attachment;filename="+file);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileSystemResource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))//响应的内容通过字节流的方式传输
                .body(new InputStreamResource(fileSystemResource.getInputStream()));

    }

}
