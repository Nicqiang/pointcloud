package com.github.nicqiang.pointcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PointcloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(PointcloudApplication.class, args);
    }

}
