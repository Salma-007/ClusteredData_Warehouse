package com.example.datawarehouse.controller;

import com.example.datawarehouse.dto.FxDealRequestDTO;
import com.example.datawarehouse.dto.ImportSummaryResponse;
import com.example.datawarehouse.service.FxDealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deals")
@RequiredArgsConstructor
public class FxDealController {
    private final FxDealService dealService;

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ImportSummaryResponse importDeals(
            @RequestBody @Valid List<FxDealRequestDTO> requests) {

        return dealService.importDeals(requests);
    }

}
