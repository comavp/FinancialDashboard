package ru.comavp.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.comavp.dashboard.model.dto.InvestTransactionsFilter;
import ru.comavp.dashboard.service.ReplenishmentHistoryService;
import ru.comavp.dashboard.utils.DataUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReplenishmentHistoryController.class)
class ReplenishmentHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReplenishmentHistoryService replenishmentHistoryService;

    private String BASE_URL = "/api/replenishment-transactions";

    @Test
    public void testFindAllReplenishments() throws Exception {
        when(replenishmentHistoryService.findAll()).thenReturn(List.of(
                DataUtils.generateReplenishmentTransactionDto(),
                DataUtils.generateReplenishmentTransactionDto(),
                DataUtils.generateReplenishmentTransactionDto()
        ));
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].length()").value(5))
                .andExpect(jsonPath("$[1].length()").value(5))
                .andExpect(jsonPath("$[2].length()").value(5));
        verify(replenishmentHistoryService).findAll();
    }

    @Test
    public void testFindReplenishmentsByFilter() throws Exception {
        when(replenishmentHistoryService.findByFilter(any())).thenReturn(List.of(DataUtils.generateReplenishmentTransactionDto()));
        String body = objectMapper.writeValueAsString(new InvestTransactionsFilter());
        mockMvc.perform(post(BASE_URL + "/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].length()").value(5));
        verify(replenishmentHistoryService).findByFilter(any());
    }

    @Test
    public void testGetInvestTransactionsNumber() throws Exception {
        when(replenishmentHistoryService.getReplenishmentsNumber()).thenReturn(10L);
        mockMvc.perform(get(BASE_URL + "/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
        verify(replenishmentHistoryService).getReplenishmentsNumber();
    }
}