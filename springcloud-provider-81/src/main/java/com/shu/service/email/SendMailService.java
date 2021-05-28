package com.shu.service.email;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.Properties;

@Service
public class SendMailService {
    public String send(String receiver,String title,String text) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");
        mailSender.setUsername("897966503@qq.com");
        mailSender.setPassword("gqzmzyzomtlebdjb");
//        mailSender.setPort(82);
        //加认证机制
        Properties javaMailProperties = new Properties();
        MailSSLSocketFactory sf=null;
        try {
            sf=new MailSSLSocketFactory();
        }catch (GeneralSecurityException e){
            e.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.ssl.enable", true);
//        javaMailProperties.put("mail.smtp.timeout", 5000);
        javaMailProperties.put("mail.smtp.port","465");
        javaMailProperties.put("mail.transport.protocol","smtp");
        javaMailProperties.put("mail.smtp.ssl.socketFactory",sf);

        mailSender.setJavaMailProperties(javaMailProperties);
        //创建邮件内容
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("897966503@qq.com");
        message.setTo(receiver);
        message.setSubject(title);
        message.setText(text);
        //发送邮件
        mailSender.send(message);
        return "success";
    }
}
