package com.example.hobiday_backend.domain.perform.controller;

import com.example.hobiday_backend.domain.perform.service.ParsingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParsingController {
    private final ParsingService performParsing;
    private final static String pw = "orange"; // => secret으로 뺄예정

    @Operation(summary="프론트에서 사용X")
    @GetMapping("/parsing/{password}")
    public void runParsing(@PathVariable String password) {
        if (password.equals(pw)){
            performParsing.saveAll();
        }
    }
}
