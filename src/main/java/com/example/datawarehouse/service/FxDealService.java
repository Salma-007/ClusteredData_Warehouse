package com.example.datawarehouse.service;

import com.example.datawarehouse.dto.request.FxDealRequest;
import com.example.datawarehouse.dto.response.FxDealResponse;
import com.example.datawarehouse.dto.response.ImportSummaryResponse;
import com.example.datawarehouse.exception.DuplicateDealException;
import com.example.datawarehouse.model.FxDeal;
import com.example.datawarehouse.repository.FxDealRepository;
import com.example.datawarehouse.util.FxDealMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxDealService {

    private final FxDealRepository repository;
    private final FxDealMapper mapper;

    public ImportSummaryResponse importDeals(List<FxDealRequest> requests) {
        log.info("Starting import of {} deals", requests.size());

        ImportSummaryResponse summary = ImportSummaryResponse.builder()
                .imported(0)
                .skipped(0)
                .build();

        for (FxDealRequest request : requests) {
            try {
                saveDeal(request);
                summary.setImported(summary.getImported() + 1);
                log.debug("Successfully imported deal: {}", request.getDealUniqueId());

            } catch (DuplicateDealException e) {
                summary.setSkipped(summary.getSkipped() + 1);
                summary.getErrors().add(ImportSummaryResponse.ErrorDetail.builder()
                        .dealUniqueId(request.getDealUniqueId())
                        .reason("Duplicate entry")
                        .build());
                log.warn("Skipped duplicate deal: {}", request.getDealUniqueId());

            } catch (Exception e) {
                summary.setSkipped(summary.getSkipped() + 1);
                summary.getErrors().add(ImportSummaryResponse.ErrorDetail.builder()
                        .dealUniqueId(request.getDealUniqueId())
                        .reason(e.getMessage())
                        .build());
                log.error("Failed to import deal {}: {}", request.getDealUniqueId(), e.getMessage());
            }
        }

        log.info("Import completed: {} imported, {} skipped", summary.getImported(), summary.getSkipped());
        return summary;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FxDealResponse saveDeal(FxDealRequest request) {
        log.debug("Attempting to save deal: {}", request.getDealUniqueId());

        if (repository.existsByDealUniqueId(request.getDealUniqueId())) {
            throw new DuplicateDealException(request.getDealUniqueId());
        }

        FxDeal deal = mapper.toEntity(request);
        FxDeal savedDeal = repository.save(deal);

        log.info("Deal saved successfully: {}", savedDeal.getDealUniqueId());
        return mapper.toResponse(savedDeal);
    }


    @Transactional(readOnly = true)
    public List<FxDealResponse> getAllDeals() {
        log.debug("Fetching all deals");
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
