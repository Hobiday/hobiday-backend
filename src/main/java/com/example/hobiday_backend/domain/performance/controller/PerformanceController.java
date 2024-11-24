package com.example.hobiday_backend.domain.performance.controller;

import com.example.hobiday_backend.domain.performance.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Tag(name="공연 정보")
public class PerformanceController {
    private final PerformanceService performanceService;

    @Operation(summary="프론트에서 사용X", description = "")
    @GetMapping("/parsing")
    public void runTesT() throws ParserConfigurationException, IOException, SAXException {
        performanceService.save();
    }
}

