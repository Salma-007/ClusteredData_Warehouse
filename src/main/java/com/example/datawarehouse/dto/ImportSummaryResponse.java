package com.example.datawarehouse.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportSummaryResponse {

    private int imported;
    private int skipped;

    @Builder.Default
    private List<ErrorDetail> errors = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorDetail {
        private String dealUniqueId;
        private String reason;
    }
}
