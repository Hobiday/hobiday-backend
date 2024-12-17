package com.example.hobiday_backend.domain.perform.controller;

import com.example.hobiday_backend.domain.perform.service.PerformParsing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParsingController {
    @Value("${property.my.pw}")
    private String pw;

    private final PerformParsing performParsing;

    // 수동 파싱
    @GetMapping("/parsing/{password}")
    public void runParsing(@PathVariable String password) throws InterruptedException {
        if (password.equals(pw)){
//            performParsing.saveAll();
            autoStatusUpdate();
            Thread.sleep(60000); // 60초
            autoPerformParsing();
        }
    }

    // 매일 04시 30분마다 공연DB 업데이트 작업 자동 수행
    @Scheduled(cron = "0 30 4 * * *")
    public void autoStatusUpdate(){
        performParsing.statusUpdate();
        log.info("자동 공연DB 업데이트 완료");
    }

    // 매일 04시 40분마다 파싱 자동 수행
    @Scheduled(cron = "0 40 4 * * *")
    public void autoPerformParsing(){
        performParsing.setParsingPeriod();
        performParsing.saveAll();
        log.info("자동 파싱 완료");
    }

}
