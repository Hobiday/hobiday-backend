package com.example.hobiday_backend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class HobidayApplication {

    // 한국 시간대 설정은 SpringBoot-RDS-Docker 모두 설정해서 일치시켜야함 (EC2 Host 시간대는 무관)
    @PostConstruct
    void started() { // 스프링 타임존 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
    public static void main(String[] args) {
        SpringApplication.run(HobidayApplication.class, args);
    }
}
