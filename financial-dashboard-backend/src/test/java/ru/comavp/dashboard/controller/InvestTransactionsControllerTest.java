package ru.comavp.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.comavp.dashboard.model.dto.InvestTransactionsFilter;
import ru.comavp.dashboard.service.InvestTransactionsService;
import ru.comavp.dashboard.utils.DataUtils;

import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestTransactionsController.class)
class InvestTransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvestTransactionsService investTransactionsService;

    private String BASE_URL = "/api/invest-transactions";

    @Test
    public void testFindAllInvestTransactions() throws Exception {
        when(investTransactionsService.findAll()).thenReturn(List.of(
                DataUtils.generateInvestTransactionDto(),
                DataUtils.generateInvestTransactionDto(),
                DataUtils.generateInvestTransactionDto()
        ));
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].length()").value(9))
                .andExpect(jsonPath("$[1].length()").value(9))
                .andExpect(jsonPath("$[2].length()").value(9));
        verify(investTransactionsService).findAll();
    }

    @Test
    public void testFindInvestTransactionsByFilter() throws Exception {
        when(investTransactionsService.findByFilter(any())).thenReturn(List.of(DataUtils.generateInvestTransactionDto()));
        String body = objectMapper.writeValueAsString(new InvestTransactionsFilter());
        mockMvc.perform(post(BASE_URL + "/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].length()").value(9));
        verify(investTransactionsService).findByFilter(any());
    }

    @Test
    public void testGetInvestmentPortfolioInfo() throws Exception {
        when(investTransactionsService.getInvestmentPortfolioInfo()).thenReturn(List.of(DataUtils.generateInvestmentPortfolioDto()));
        mockMvc.perform(get(BASE_URL + "/portfolio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].length()").value(2))
                .andExpect(jsonPath("$[0].brokerNameToQuantityMap.length()").value(2));
        verify(investTransactionsService).getInvestmentPortfolioInfo();
    }

    @Test
    public void testGetInvestTransactionsNumber() throws Exception {
        when(investTransactionsService.getInvestTransactionsNumber()).thenReturn(100L);
        mockMvc.perform(get(BASE_URL + "/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
        verify(investTransactionsService).getInvestTransactionsNumber();
    }
}