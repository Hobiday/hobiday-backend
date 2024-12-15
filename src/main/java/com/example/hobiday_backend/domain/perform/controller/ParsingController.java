package com.example.hobiday_backend.domain.perform.controller;

import com.example.hobiday_backend.domain.perform.service.PerformParsing;
import com.example.hobiday_backend.domain.perform.util.KopisParsing;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("/parsing/{password}")
    public void runParsing(@PathVariable String password) {
        if (password.equals(pw)){
            performParsing.saveAll();
        }
    }

    // 매일 04시 20분마다 공연상태 체크 작업 수행
    @Scheduled(cron = "0 20 4 * * *")
    public void autoStatusCheck(){
        performParsing.statusCheck();
        log.info("자동 공연상태 체크");
    }

    // 매일 04시 30분마다 파싱 수행
    @Scheduled(cron = "0 30 4 * * *")
    public void autoKopisParsing(){
        performParsing.setParsingPeriod();
//        performParsing.saveAll();
        log.info("자동 파싱 실행");
    }

//    @Scheduled(cron = "0 23 20 * * *")
//    public void test(){
//        log.info("스케줄러 실행");
//    }

}
