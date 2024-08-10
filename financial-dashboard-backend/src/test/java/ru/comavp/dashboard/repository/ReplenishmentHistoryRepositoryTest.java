package ru.comavp.dashboard.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.comavp.dashboard.model.entity.ReplenishmentTransaction;
import ru.comavp.dashboard.utils.DataUtils;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReplenishmentHistoryRepositoryTest {

    @Autowired
    private ReplenishmentHistoryRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        Stream.of(
                DataUtils.generateReplenishmentTransaction(LocalDate.of(2000, 1, 1), "Green broker"),
                DataUtils.generateReplenishmentTransaction(LocalDate.of(2000, 4, 5), "Yellow broker"),
                DataUtils.generateReplenishmentTransaction(LocalDate.of(2000, 4, 5), "Pink broker"),
                DataUtils.generateReplenishmentTransaction(LocalDate.of(2000, 5, 5), "Green broker"),
                DataUtils.generateReplenishmentTransaction(LocalDate.of(1999, 5, 5), "Green broker"),
                DataUtils.generateReplenishmentTransaction(LocalDate.of(1999, 5, 5), "Green broker")
        ).forEach(item -> entityManager.persist(item));
    }

    @Test
    public void testFindByBrokerName() {
        String testBroker = "Green broker";
        var result = repository.findByBrokerName(testBroker);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.stream().allMatch(item -> testBroker.equals(item.getBrokerName())));
    }

    @Test
    public void testFindEmptyListByBrokerName() {
        var result = repository.findByBrokerName("Test broker");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    };

    @Test
    public void testFindByTransactionDateGreaterThanEqual() {
        var testDate = LocalDate.of(2000, 1, 1);
        var result = repository.findByTransactionDateGreaterThanEqual(testDate);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.stream().map(ReplenishmentTransaction::getTransactionDate)
                .allMatch(item -> item.isAfter(testDate) || item.equals(testDate)));
    }

    @Test
    public void testFindEmptyListByTransactionDateGreaterThanEqual() {
        var testDate = LocalDate.of(2002, 1, 1);
        var result = repository.findByTransactionDateGreaterThanEqual(testDate);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByBrokerNameAndTransactionDateGreaterThanEqual() {
        String testBroker = "Green broker";
        var testDate = LocalDate.of(2000, 1, 1);
        var result = repository.findByBrokerNameAndTransactionDateGreaterThanEqual(testBroker, testDate);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(item -> testBroker.equals(item.getBrokerName()) &&
                (testDate.equals(item.getTransactionDate()) || item.getTransactionDate().isAfter(testDate))));
    }

    @Test
    public void testFindEmptyListByBrokerNameAndTransactionDateGreaterThanEqual_MissingBrokerName() {
        String testBroker = "Test broker";
        var testDate = LocalDate.of(2000, 1, 1);
        var result = repository.findByBrokerNameAndTransactionDateGreaterThanEqual(testBroker, testDate);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindEmptyListByBrokerNameAndTransactionDateGreaterThanEqual_MissingTransactionDate() {
        String testBroker = "Green broker";
        var testDate = LocalDate.of(2002, 1, 1);
        var result = repository.findByBrokerNameAndTransactionDateGreaterThanEqual(testBroker, testDate);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}