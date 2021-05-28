package com.shu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


//@EnableDiscoveryClient
//@EnableEurekaClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AudioAndVideo_82 {
    public static void main(String[] args) {
        SpringApplication.run(AudioAndVideo_82.class, args);
    }

}
