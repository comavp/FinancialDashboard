package ru.comavp.dashboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.comavp.dashboard.model.dto.InvestTransactionDto;
import ru.comavp.dashboard.model.dto.InvestTransactionsFilter;
import ru.comavp.dashboard.model.dto.InvestmentPortfolioInfoDto;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.model.entity.InvestmentPortfolioInfo;
import ru.comavp.dashboard.model.mappers.InvestTransactionMapper;
import ru.comavp.dashboard.repository.InvestTransactionsRepository;
import ru.comavp.dashboard.utils.DataUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestTransactionsServiceTest {

    @Mock
    private InvestTransactionsRepository repository;
    @Spy
    InvestTransactionMapper mapper = Mappers.getMapper(InvestTransactionMapper.class);

    @InjectMocks
    InvestTransactionsService investTransactionsService;

    @Test
    public void testSaveAllTransactions() {
        investTransactionsService.saveAllTransactions(new ArrayList<>());
        verify(repository).saveAll(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindAll() {
        var entityList = List.of(
                DataUtils.generateInvestTransaction("Issuer 1", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 2", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 3", LocalDate.now(), "Broker 1")
        );
        when(repository.findAll()).thenReturn(entityList);

        var result = investTransactionsService.findAll();

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(entityList.stream().map(InvestTransaction::getIssuerName).toList(),
                result.stream().map(InvestTransactionDto::getIssuerName).toList());
        assertTrue(result.stream().map(InvestTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testFindAllWithEmptyResult() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        var result = investTransactionsService.findAll();

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFilterWithEmptyParams() {
        var entityList = List.of(
                DataUtils.generateInvestTransaction("Issuer 1", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 2", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 3", LocalDate.now(), "Broker 1")
        );
        when(repository.findAll()).thenReturn(entityList);

        var result = investTransactionsService.findByFilter(new InvestTransactionsFilter());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(entityList.stream().map(InvestTransaction::getIssuerName).toList(),
                result.stream().map(InvestTransactionDto::getIssuerName).toList());
        assertTrue(result.stream().map(InvestTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testFindByFilterWithEmptyYear() {
        var entityList = List.of(
                DataUtils.generateInvestTransaction("Issuer 1", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 2", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 3", LocalDate.now(), "Broker 1")
        );
        var filter = new InvestTransactionsFilter();
        filter.setBrokerName("Broker 1");
        when(repository.findByBrokerName(any())).thenReturn(entityList);

        var result = investTransactionsService.findByFilter(filter);

        verify(repository).findByBrokerName(eq("Broker 1"));
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(entityList.stream().map(InvestTransaction::getIssuerName).toList(),
                result.stream().map(InvestTransactionDto::getIssuerName).toList());
        assertTrue(result.stream().map(InvestTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testFindByFilterWithEmptyBrokerName() {
        var entityList = List.of(
                DataUtils.generateInvestTransaction("Issuer 1", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 2", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 3", LocalDate.now(), "Broker 1")
        );
        var filter = new InvestTransactionsFilter();
        filter.setYear(2024);
        when(repository.findByTransactionDateGreaterThanEqual(any())).thenReturn(entityList);

        var result = investTransactionsService.findByFilter(filter);

        verify(repository).findByTransactionDateGreaterThanEqual(eq(LocalDate.of(2024, 1, 1)));
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(entityList.stream().map(InvestTransaction::getIssuerName).toList(),
                result.stream().map(InvestTransactionDto::getIssuerName).toList());
        assertTrue(result.stream().map(InvestTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testFindByFilterWithFullFilter() {
        var entityList = List.of(
                DataUtils.generateInvestTransaction("Issuer 1", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 2", LocalDate.now(), "Broker 1"),
                DataUtils.generateInvestTransaction("Issuer 3", LocalDate.now(), "Broker 1")
        );
        var filter = new InvestTransactionsFilter("Broker 1", 2024);
        when(repository.findByBrokerNameAndTransactionDateGreaterThanEqual(anyString(), any())).thenReturn(entityList);

        var result = investTransactionsService.findByFilter(filter);

        verify(repository).findByBrokerNameAndTransactionDateGreaterThanEqual(eq("Broker 1"),
                eq(LocalDate.of(2024, 1, 1)));
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(entityList.stream().map(InvestTransaction::getIssuerName).toList(),
                result.stream().map(InvestTransactionDto::getIssuerName).toList());
        assertTrue(result.stream().map(InvestTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testGetInvestmentPortfolioInfo() {
        var entityList = List.of(
                new InvestmentPortfolioInfo("Issuer 1", "Broker 1", 1L),
                new InvestmentPortfolioInfo("Issuer 1", "Broker 2", 5L),
                new InvestmentPortfolioInfo("Issuer 2", "Broker 3", 10L),
                new InvestmentPortfolioInfo("Issuer 1", null, 12L)
        );
        when(repository.getInvestmentPortfolioInfo()).thenReturn(entityList);

        var result = investTransactionsService.getInvestmentPortfolioInfo();

        verify(repository).getInvestmentPortfolioInfo();
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(entityList.stream().map(InvestmentPortfolioInfo::getIssuerName).distinct().toList(),
                result.stream().map(InvestmentPortfolioInfoDto::getIssuerName).toList());
        assertEquals("Issuer 1", result.get(0).getIssuerName());
        assertEquals("Issuer 2", result.get(1).getIssuerName());

        var firstItem = result.get(0).getBrokerNameToQuantityMap();
        assertNotNull(firstItem);
        assertEquals(2, firstItem.size());
        assertEquals(1L, firstItem.get("Broker 1"));
        assertEquals(5L, firstItem.get("Broker 2"));

        var secondItem = result.get(1).getBrokerNameToQuantityMap();
        assertNotNull(secondItem);
        assertEquals(1, secondItem.size());
        assertEquals(10L, secondItem.get("Broker 3"));
    }

    @Test
    public void testGetEmptyInvestmentPortfolioInfo() {
        when(repository.getInvestmentPortfolioInfo()).thenReturn(new ArrayList<>());
        var result = investTransactionsService.getInvestmentPortfolioInfo();

        verify(repository).getInvestmentPortfolioInfo();
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}