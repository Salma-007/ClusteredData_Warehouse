package com.example.datawarehouse.service;

import com.example.datawarehouse.dto.ErrorDetail;
import com.example.datawarehouse.dto.FxDealRequestDTO;
import com.example.datawarehouse.dto.ImportSummaryResponse;
import com.example.datawarehouse.model.FxDeal;
import com.example.datawarehouse.repository.FxDealRepository;
import com.example.datawarehouse.util.FxDealMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxDealService {
    private final FxDealRepository repository;
    private final FxDealMapper mapper;

    public ImportSummaryResponse importDeals(List<FxDealRequestDTO> requests) {
        int importedCount = 0;
        List<ErrorDetail> errors = new ArrayList<>();

        for (FxDealRequestDTO request : requests) {
            ErrorDetail error = saveSingleDealIsolated(request);
            if (error == null) {
                importedCount++;
            } else {
                errors.add(error);
            }
        }

        return new ImportSummaryResponse(importedCount, errors.size(), errors);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ErrorDetail saveSingleDealIsolated(FxDealRequestDTO request) {
        String dealId = request.dealUniqueId();

        if (repository.existsByDealUniqueId(dealId)) {
            log.warn("Skipping deal: Duplicate ID found: {}", dealId);
            return new ErrorDetail(dealId, "Duplicate deal ID");
        }

        try {
            FxDeal deal = mapper.toEntity(request);
            repository.save(deal);
            log.info("Successfully imported deal: {}", dealId);
            return null; // Success
        } catch (DataIntegrityViolationException e) {
            log.error("Database error saving deal {}: {}", dealId, e.getMessage());
            return new ErrorDetail(dealId, "Database integrity violation (e.g., data too long)");
        } catch (Exception e) {
            log.error("Unexpected error saving deal {}: {}", dealId, e.getMessage());
            return new ErrorDetail(dealId, "Unexpected processing error");
        }
    }
}
