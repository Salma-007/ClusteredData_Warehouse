package com.example.datawarehouse.dto;

import java.util.List;

public record ImportSummaryResponse(
        int imported,
        int skipped,
        List<ErrorDetail> errors
) {}
