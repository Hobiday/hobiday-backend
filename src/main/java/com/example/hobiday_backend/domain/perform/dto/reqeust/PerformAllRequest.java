package com.example.hobiday_backend.domain.perform.dto.reqeust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PerformAllRequest {
    public String rowStart;
    public String rowEnd;
}
