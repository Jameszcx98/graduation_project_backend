package com.shu.controller;

import com.shu.service.impl.TestImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api(tags = "")
public class test {
    @Autowired
    private TestImpl test;

    @GetMapping("/m4aToMp3")
    @ApiOperation("输入任何音频文件转换成mp3")
    public void m4aToMp3(@ApiParam("原地址") @RequestParam String original,
                         @ApiParam("目标地址") @RequestParam String destination){
        test.m4aToMp3(original,destination);
    }
    @GetMapping("/m4aToWav")
    @ApiOperation("输入任何音频文件然后转换成wav")
    public void m4aToWav(@ApiParam("原地址") @RequestParam String original,
                         @ApiParam("目标地址") @RequestParam String destination){
        test.m4aToWav(original,destination);
    }
    @GetMapping("/m4aCompress")
    @ApiOperation("输入任何文件然后压缩成mp3文件返回")
    public void m4aCompress(@ApiParam("原地址") @RequestParam String original,
                         @ApiParam("目标地址") @RequestParam String destination,
                            @ApiParam("目标地址") @RequestParam Double compress_rate){
        test.m4aCompress(original,destination,compress_rate);
    }
    @GetMapping("/movTomp4")
    @ApiOperation("根据后缀判断格式转换之后的视频文件类型")
    public void movTomp4(@ApiParam("原地址") @RequestParam String original,
                         @ApiParam("目标地址") @RequestParam String destination){
        test.movTomp4(original,destination);
    }
    @GetMapping("/audioCompress")
    @ApiOperation("压缩视频文件生成的文件为mp4")
    public void audioCompress(@ApiParam("原地址") @RequestParam String original,
                         @ApiParam("目标地址") @RequestParam String destination){
        test.audioCompress(original,destination);
    }


}
