package com.example.datawarehouse.controller;

import com.example.datawarehouse.dto.FxDealRequest;
import com.example.datawarehouse.dto.FxDealResponse;
import com.example.datawarehouse.dto.ImportSummaryResponse;
import com.example.datawarehouse.service.FxDealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FxDealController.class)
class FxDealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FxDealService fxDealService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- Data Setup ---
    private FxDealRequest createValidRequest() {
        return FxDealRequest.builder()
                .dealUniqueId("FX001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(LocalDateTime.now())
                .dealAmount(new BigDecimal("1000.00"))

                .build();
    }

    private ImportSummaryResponse createSummary(int imported, int skipped) {
        return ImportSummaryResponse.builder()
                .imported(imported)
                .skipped(skipped)
                .errors(Collections.emptyList())
                .build();
    }

    // --- Test: POST /deals/import ---

    @Test
    void importDeals_WhenValidRequests_ShouldReturnOkAndSummary() throws Exception {
        // Arrange
        List<FxDealRequest> requests = Collections.singletonList(createValidRequest());
        ImportSummaryResponse mockSummary = createSummary(1, 0);

        // Simuler le service qui retourne un résumé d'importation
        when(fxDealService.importDeals(anyList())).thenReturn(mockSummary);

        // Act & Assert
        mockMvc.perform(post("/deals/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk()) // Vérifie le statut HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.imported").value(1))
                .andExpect(jsonPath("$.skipped").value(0));
    }

    @Test
    void importDeals_WhenEmptyList_ShouldReturnBadRequest() throws Exception {
        // Arrange
        List<FxDealRequest> emptyRequests = Collections.emptyList();

        // Act & Assert
        mockMvc.perform(post("/deals/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequests)))
                .andExpect(status().isBadRequest()); // Vérifie le statut HTTP 400
    }


    // --- Test: GET /deals ---

    @Test
    void getAllDeals_WhenDealsExist_ShouldReturnOkAndList() throws Exception {
        // Arrange
        FxDealResponse deal1 = FxDealResponse.builder().dealUniqueId("FX001").build();
        FxDealResponse deal2 = FxDealResponse.builder().dealUniqueId("FX002").build();
        List<FxDealResponse> mockDeals = List.of(deal1, deal2);

        // Simuler le service qui retourne une liste de deals
        when(fxDealService.getAllDeals()).thenReturn(mockDeals);

        // Act & Assert
        mockMvc.perform(get("/deals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie le statut HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].dealUniqueId").value("FX001"));
    }

    @Test
    void getAllDeals_WhenNoDeals_ShouldReturnOkAndEmptyList() throws Exception {
        // Arrange
        List<FxDealResponse> emptyList = Collections.emptyList();

        // Simuler le service qui retourne une liste vide
        when(fxDealService.getAllDeals()).thenReturn(emptyList);

        // Act & Assert
        mockMvc.perform(get("/deals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie le statut HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}
