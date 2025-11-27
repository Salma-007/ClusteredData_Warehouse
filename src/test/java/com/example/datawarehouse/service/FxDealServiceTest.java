package com.example.datawarehouse.service;

import com.example.datawarehouse.dto.request.FxDealRequest;
import com.example.datawarehouse.dto.response.FxDealResponse;
import com.example.datawarehouse.dto.response.ImportSummaryResponse;
import com.example.datawarehouse.exception.DuplicateDealException;
import com.example.datawarehouse.model.FxDeal;
import com.example.datawarehouse.repository.FxDealRepository;
import com.example.datawarehouse.util.FxDealMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FxDealServiceTest {

    @Mock
    private FxDealRepository repository;

    @Mock
    private FxDealMapper mapper;

    @InjectMocks
    private FxDealService service;

    private FxDealRequest validRequest;
    private FxDeal validDeal;

    @BeforeEach
    void setUp() {
        validRequest = FxDealRequest.builder()
                .dealUniqueId("FX001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(LocalDateTime.now())
                .dealAmount(new BigDecimal("1000.00"))
                .build();

        validDeal = FxDeal.builder()
                .id(1L)
                .dealUniqueId("FX001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(LocalDateTime.now())
                .dealAmount(new BigDecimal("1000.00"))
                .build();
    }

    @Test
    void saveDeal_WhenValidDeal_ShouldSaveSuccessfully() {
        // Arrange
        when(repository.existsByDealUniqueId(anyString())).thenReturn(false);
        when(mapper.toEntity(any(FxDealRequest.class))).thenReturn(validDeal);
        when(repository.save(any(FxDeal.class))).thenReturn(validDeal);
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(new FxDealResponse());

        // Act
        FxDealResponse response = service.saveDeal(validRequest);

        // Assert
        assertThat(response).isNotNull();
        verify(repository).existsByDealUniqueId("FX001");
        verify(repository).save(any(FxDeal.class));
    }

    @Test
    void saveDeal_WhenDuplicateDeal_ShouldThrowException() {
        // Arrange
        when(repository.existsByDealUniqueId(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> service.saveDeal(validRequest))
                .isInstanceOf(DuplicateDealException.class)
                .hasMessageContaining("FX001");

        verify(repository, never()).save(any(FxDeal.class));
    }

    @Test
    void importDeals_WhenAllValid_ShouldImportAll() {
        // Arrange
        FxDealRequest request1 = createRequest("FX001");
        FxDealRequest request2 = createRequest("FX002");
        List<FxDealRequest> requests = Arrays.asList(request1, request2);

        when(repository.existsByDealUniqueId(anyString())).thenReturn(false);
        when(mapper.toEntity(any(FxDealRequest.class))).thenReturn(validDeal);
        when(repository.save(any(FxDeal.class))).thenReturn(validDeal);
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(new FxDealResponse());

        // Act
        ImportSummaryResponse summary = service.importDeals(requests);

        // Assert
        assertThat(summary.getImported()).isEqualTo(2);
        assertThat(summary.getSkipped()).isEqualTo(0);
        assertThat(summary.getErrors()).isEmpty();
    }

    @Test
    void importDeals_WhenSomeDuplicates_ShouldSkipDuplicates() {
        // Arrange
        FxDealRequest request1 = createRequest("FX001");
        FxDealRequest request2 = createRequest("FX002");
        List<FxDealRequest> requests = Arrays.asList(request1, request2);

        when(repository.existsByDealUniqueId("FX001")).thenReturn(false);
        when(repository.existsByDealUniqueId("FX002")).thenReturn(true);
        when(mapper.toEntity(any(FxDealRequest.class))).thenReturn(validDeal);
        when(repository.save(any(FxDeal.class))).thenReturn(validDeal);
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(new FxDealResponse());

        // Act
        ImportSummaryResponse summary = service.importDeals(requests);

        // Assert
        assertThat(summary.getImported()).isEqualTo(1);
        assertThat(summary.getSkipped()).isEqualTo(1);
        assertThat(summary.getErrors()).hasSize(1);
        assertThat(summary.getErrors().get(0).getDealUniqueId()).isEqualTo("FX002");
        assertThat(summary.getErrors().get(0).getReason()).isEqualTo("Duplicate entry");
    }

    @Test
    void getAllDeals_ShouldReturnAllDeals() {
        // Arrange
        List<FxDeal> deals = Arrays.asList(validDeal, validDeal);
        when(repository.findAll()).thenReturn(deals);
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(new FxDealResponse());

        // Act
        List<FxDealResponse> responses = service.getAllDeals();

        // Assert
        assertThat(responses).hasSize(2);
        verify(repository).findAll();
    }

    private FxDealRequest createRequest(String dealId) {
        return FxDealRequest.builder()
                .dealUniqueId(dealId)
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(LocalDateTime.now())
                .dealAmount(new BigDecimal("1000.00"))
                .build();
    }
}
