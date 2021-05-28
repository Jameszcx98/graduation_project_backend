package com.shu.service.impl;

import com.shu.service.Test;
import it.sauronsoftware.jave.*;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TestImpl implements Test {
    /*
     * @Author: jameszhang
     * @Description:输入任何音频文件然后转换成wav
     * @param original :
     * @param destination :
     * @return: void
     **/
    @Override
    public void m4aToWav(String original, String destination) {
        try{
            File source = new File(original);

            File target = new File(destination);

            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("pcm_s16le");
            audio.setBitRate(16000);
            audio.setChannels(1);
            audio.setBitRate(16000);
            audio.setSamplingRate(16000);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("wav");
            attrs.setAudioAttributes(audio);

            //Encode
            Encoder encoder = new Encoder();
            encoder.encode(source, target, attrs);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    /*
     * @Author: jameszhang
     * @Description:输入任何音频文件转换成mp3
     * @param original :
     * @param destination :
     * @return: void
     **/
    @Override
    public void m4aToMp3(String original, String destination){
        try{
            File source = new File(original);

            File target = new File(destination);
            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setChannels(2);
            audio.setBitRate(128000);
            audio.setSamplingRate(44100);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);

            //Encode
            Encoder encoder2 = new Encoder();
            encoder2.encode(source, target, attrs);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }



    }



    /*
     * @Author: jameszhang
     * @Description: 输入任何文件然后压缩成mp3文件返回
     * @param original :
     * @param destination :
     * @return: void
     **/
    @Override
    public void m4aCompress(String original, String destination,Double compress_rate) {
        try{
            File source = new File(original);

            File target = new File(destination);


            //Get information
            Encoder encoder = new Encoder();
            MultimediaInfo m = encoder.getInfo(source);
            int bit_rate = m.getAudio().getBitRate();
            int cnl = m.getAudio().getChannels();
            int sample_rate = m.getAudio().getSamplingRate();
            System.out.println(cnl);
            System.out.println(bit_rate);
            System.out.println(sample_rate);

            //set compressed rate

            int bitcompressed = (int)(compress_rate * bit_rate);
            int newBitRate=bitcompressed*1000;


            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();

            audio.setCodec("libmp3lame");

            System.out.println(newBitRate);

            audio.setChannels(cnl);
            audio.setBitRate(newBitRate);
            audio.setSamplingRate(sample_rate);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);

            //Encode
            Encoder encoder2 = new Encoder();
            encoder2.encode(source, target, attrs);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /*
     * @Author: jameszhang
     * @Description:根据后缀判断格式转换之后的视频文件类型
     * //支持avi mp4 mov flv 3gp
     * @param original :
     * @param destination :
     * @return: void
     **/
    @Override
    public void movTomp4(String original, String destination) {
        File source = new File(original);
        File target = new File(destination);
        String fileName = target.getName();
        String fileTyle = fileName.substring(fileName.lastIndexOf("."),fileName.length());//获取文件后缀名以进行判断
        //System.out.println(fileTyle);
        Encoder encoder1 = new Encoder();
        try {
            MultimediaInfo m = encoder1.getInfo(source);
            int width = m.getVideo().getSize().getWidth();//获取视频宽度
            int height = m.getVideo().getSize().getHeight();//获取视频高度
            //System.out.println(width);
            //System.out.println(height);
            String codec = "mpeg4";
            String format = "avi";
            if (fileTyle==".avi") {
                codec = "mpeg4";
                format = "avi";
            }
            if (fileTyle==".mp4") {
                codec = "mpeg4";
                format = "mp4";
            }
            if (fileTyle==".mov") {
                codec = "mpeg4";
                format = "mov";
            }
            if (fileTyle==".flv") {
                codec = "flv";
                format = "flv";
            }
            if (fileTyle==".3gp") {
                codec = "mpeg4";
                format = "3gp";
            }
            System.out.println("开始转码");
            AudioAttributes audio= new AudioAttributes();
            audio.setCodec("libmp3lame");//设置音频编码器
            audio.setBitRate(new Integer(64000));//音频比特率
            audio.setChannels(new Integer(1));//声音频道
            audio.setSamplingRate(new Integer(22050));//节录率
            VideoAttributes video=new VideoAttributes();
            video.setCodec(codec);//设置视频编码器
            video.setBitRate(new Integer(160000));//视频比特率
            video.setFrameRate(new Integer(15));//帧率
            video.setSize(new VideoSize(width,height));//大小
            EncodingAttributes attr=new EncodingAttributes();
            attr.setFormat(format);//设置转码格式
            attr.setAudioAttributes(audio);//添加音频转码时所需音频属性
            attr.setVideoAttributes(video);//添加视频转码时所需音频属性
            Encoder encoder=new Encoder();
            //System.out.println(Arrays.toString(encoder.getSupportedDecodingFormats()));
            //System.out.println(Arrays.toString(encoder.getSupportedEncodingFormats()));
            encoder.encode(source, target, attr);//source:需要转码的源文件;target:需转型成的目标文件;attributes:是一个包含编码所需数据的参数
            System.out.println("转码结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * @Author: jameszhang
     * @Description:压缩视频文件生成的文件为mp4
     * @param original :
     * @param destination :
     * @return: void
     **/
    @Override
    public void audioCompress(String original, String destination) {
        File source = new File(original);
        File target = new File(destination);
        try {
            System.out.println("开始压缩，请等待");

            // 音频编码设置
            AudioAttributes audio= new AudioAttributes();
            audio.setCodec("libmp3lame");   //设置编码器
            audio.setBitRate(new Integer(56000));   //设置比特率
            audio.setChannels(new Integer(1));  //设置声音频道
            audio.setSamplingRate(new Integer(22050));  //设置节录率

            // 视频编码设置
            VideoAttributes video=new VideoAttributes();
            video.setCodec("mpeg4");    //设置编码器
            video.setBitRate(new Integer(800000));  //设置比特率
            video.setFrameRate(new Integer(20));    //设置帧率    调整视频压缩质量主要就是修改这里的值  修改方法在最下面

            // 视频转码编码设置
            EncodingAttributes attr=new EncodingAttributes();
            attr.setFormat("avi");
            attr.setAudioAttributes(audio);
            attr.setVideoAttributes(video);

            // 编码器
            Encoder encoder=new Encoder();
            encoder.encode(source, target, attr);
            System.out.println("压缩结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
