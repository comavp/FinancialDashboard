package ru.comavp.dashboard.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@Disabled
public class MoexServiceTest {

    @Test
    public void testGettingPriceByIssuerNameAndDate() {
        MoexService moexService = new MoexService();
        moexService.getPriceByIssuerNameAndDate("test", LocalDate.now());
    }
}
