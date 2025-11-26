package com.example.datawarehouse.controller;

import com.example.datawarehouse.dto.FxDealRequest;
import com.example.datawarehouse.dto.FxDealResponse;
import com.example.datawarehouse.dto.ImportSummaryResponse;
import com.example.datawarehouse.service.FxDealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/deals")
@RequiredArgsConstructor
@Slf4j
public class FxDealController {

    private final FxDealService service;

    @PostMapping("/import")
    public ResponseEntity<ImportSummaryResponse> importDeals(
            @Valid @RequestBody List<@Valid FxDealRequest> requests) {

        log.info("Received import request for {} deals", requests.size());

        if (requests.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ImportSummaryResponse summary = service.importDeals(requests);
        return ResponseEntity.status(HttpStatus.OK).body(summary);
    }


    @GetMapping
    public ResponseEntity<List<FxDealResponse>> getAllDeals() {
        log.info("Fetching all deals");
        List<FxDealResponse> deals = service.getAllDeals();
        return ResponseEntity.ok(deals);
    }
}
