package ru.comavp.dashboard.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.comavp.dashboard.model.entity.InvestTransaction;
import ru.comavp.dashboard.utils.DataUtils;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InvestTransactionsRepositoryTest {

    @Autowired
    private InvestTransactionsRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        Stream.of(
                DataUtils.generateInvestTransaction("issuer 1", LocalDate.of(2000, 1, 1), "Green broker"),
                DataUtils.generateInvestTransaction("issuer 1", LocalDate.of(2000, 4, 5), "Yellow broker"),
                DataUtils.generateInvestTransaction("issuer 1", LocalDate.of(2000, 4, 5), "Pink broker"),
                DataUtils.generateInvestTransaction("issuer 2", LocalDate.of(2000, 5, 5), "Green broker"),
                DataUtils.generateInvestTransaction("issuer 1", LocalDate.of(1999, 5, 5), "Green broker"),
                DataUtils.generateInvestTransaction("issuer 1", LocalDate.of(1999, 5, 5), "Green broker")
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
        assertTrue(result.stream().map(InvestTransaction::getTransactionDate)
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

    @Test
    public void testGetInvestmentPortfolioInfo() {
        var result = repository.getInvestmentPortfolioInfo();
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(3, result.stream().filter(item -> "issuer 1".equals(item.getIssuerName())).count());
        assertEquals(1, result.stream().filter(item -> "issuer 2".equals(item.getIssuerName())).count());
        assertEquals(2, result.stream().filter(item -> "Green broker".equals(item.getBrokerName())).count());
        assertEquals(1, result.stream().filter(item -> "Yellow broker".equals(item.getBrokerName())).count());
        assertEquals(1, result.stream().filter(item -> "Pink broker".equals(item.getBrokerName())).count());
    }
}