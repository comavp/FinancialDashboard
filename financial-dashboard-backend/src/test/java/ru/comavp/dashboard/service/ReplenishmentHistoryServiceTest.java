package ru.comavp.dashboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.comavp.dashboard.model.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.model.dto.ReplenishmentsFilter;
import ru.comavp.dashboard.model.mappers.ReplenishmentMapper;
import ru.comavp.dashboard.repository.ReplenishmentHistoryRepository;
import ru.comavp.dashboard.utils.DataUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplenishmentHistoryServiceTest {

    @Mock
    private ReplenishmentHistoryRepository repository;
    @Spy
    private ReplenishmentMapper mapper = Mappers.getMapper(ReplenishmentMapper.class);

    @InjectMocks
    private ReplenishmentHistoryService replenishmentHistoryService;

    private final PageRequest EXPECTED_PAGING = PageRequest.of(0, 100);

    @Test
    public void testSaveAllTransactions() {
        replenishmentHistoryService.saveAllTransactions(new ArrayList<>());
        verify(repository).saveAll(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindAll() {
        var entityList = List.of(
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1")
        );
        when(repository.findAll()).thenReturn(entityList);

        var result = replenishmentHistoryService.findAll();

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().map(ReplenishmentTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testFindAllWithEmptyResult() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        var result = replenishmentHistoryService.findAll();

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFilterWithEmptyParams() {
        var entityList = List.of(
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1")
        );
        var entityPage = new PageImpl<>(entityList, PageRequest.of(0, 3), 3);
        when(repository.findAll(any(Pageable.class))).thenReturn(entityPage);

        var result = replenishmentHistoryService.findByFilter(new ReplenishmentsFilter());

        verify(repository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().map(ReplenishmentTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testFindByFilterWithEmptyYear() {
        var entityList = List.of(
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1")
        );
        var filter = new ReplenishmentsFilter();
        filter.setBrokerName("Broker 1");
        when(repository.findByBrokerName(any(), any())).thenReturn(entityList);

        var result = replenishmentHistoryService.findByFilter(filter);

        verify(repository).findByBrokerName(eq("Broker 1"), eq(EXPECTED_PAGING));
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().map(ReplenishmentTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testFindByFilterWithEmptyBrokerName() {
        var entityList = List.of(
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1")
        );
        var filter = new ReplenishmentsFilter();
        filter.setYear(2024);
        when(repository.findByTransactionDateGreaterThanEqual(any(), any())).thenReturn(entityList);

        var result = replenishmentHistoryService.findByFilter(filter);

        verify(repository).findByTransactionDateGreaterThanEqual(eq(LocalDate.of(2024, 1, 1)), eq(EXPECTED_PAGING));
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().map(ReplenishmentTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testFindByFilterWithFullFilter() {
        var entityList = List.of(
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1"),
                DataUtils.generateReplenishmentTransaction(LocalDate.now(), "Broker 1")
        );
        var filter = new ReplenishmentsFilter(2024, "Broker 1");
        when(repository.findByBrokerNameAndTransactionDateGreaterThanEqual(anyString(), any(), any())).thenReturn(entityList);

        var result = replenishmentHistoryService.findByFilter(filter);

        verify(repository).findByBrokerNameAndTransactionDateGreaterThanEqual(eq("Broker 1"),
                eq(LocalDate.of(2024, 1, 1)), eq(EXPECTED_PAGING));
        verifyNoMoreInteractions(repository);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().map(ReplenishmentTransactionDto::getBrokerName).allMatch("Broker 1"::equals));
    }

    @Test
    public void testGetReplenishmentsNumber() {
        when(repository.count()).thenReturn(10L);
        long actualResult = replenishmentHistoryService.getReplenishmentsNumber();

        verify(repository).count();
        verifyNoMoreInteractions(repository);

        assertEquals(10L, actualResult);
    }
}