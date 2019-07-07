package com.shuzhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.shuzhi.mapper")
//@EnableEurekaClient
public class BusStationWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusStationWebApplication.class, args);

    }

}
