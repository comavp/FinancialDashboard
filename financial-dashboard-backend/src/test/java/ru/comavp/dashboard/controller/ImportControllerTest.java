package ru.comavp.dashboard.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.comavp.dashboard.service.ImportService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImportController.class)
class ImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportService importService;

    private MockMultipartFile file;

    private String BASE_URL = "/api/import-data";

    @BeforeEach
    public void init() throws IOException {
        file = new MockMultipartFile("file", "file.xlsx", "application/vnd.ms-excel",
                getClass().getClassLoader().getResourceAsStream("test_file.xlsx"));
    }

    @Test
    public void testImportAllData() throws Exception {
        mockMvc.perform(multipart(BASE_URL)
                        .file(file))
                .andExpect(status().isOk());
        verify(importService).importAllDataFromWorkBookSheet(any());
    }

    @Test
    public void testImportInvestTransactions() throws Exception {
        mockMvc.perform(multipart(BASE_URL + "/invest-transactions")
                        .file(file))
                .andExpect(status().isOk());
        verify(importService).importInvestTransactions(any());
    }

    @Test
    public void testImportReplenishments() throws Exception {
        mockMvc.perform(multipart(BASE_URL + "/replenishments")
                        .file(file))
                .andExpect(status().isOk());
        verify(importService).importReplenishmentTransactions(any());
    }
}