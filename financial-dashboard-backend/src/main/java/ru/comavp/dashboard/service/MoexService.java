package ru.comavp.dashboard.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.exdata.moex.IssClient;
import ru.exdata.moex.IssClientBuilder;
import ru.exdata.moex.Request;
import ru.exdata.moex.response.Block;
import ru.exdata.moex.response.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MoexService {

    private IssClient client = IssClientBuilder.builder().build();

    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Integer RETRIES_CNT = 5;

    public Double getPriceByIssuerNameAndDate(String issuerName, LocalDate date) {
        try {
            var client = IssClientBuilder.builder().build();
            var request = client
                    .iss()
                    .securities()
                    .security(issuerName)
                    .aggregates()
                    .format()
                    .json();

            var response = request.get(Map.of("date", date.format(DATE_PATTERN)));
            var block = response.findBlock("aggregates").get();
            int cnt = 1;
            LocalDate nearestDate = date;
            while (cnt <= RETRIES_CNT && (block.getData() == null || block.getData().isEmpty())) {
                nearestDate = nearestDate.minus(1, ChronoUnit.DAYS);
                block = getPriceByNearestDate(request, nearestDate);
                cnt++;
            }
            return extractPrice(block.getData());
        } catch (Exception e) {
            log.error("Во время отправки запроса в MOEX ISS произошла ошибка: ", e);
            return 0.0;
        }
    }

    public List<Double> getPricesByIssuerNameBetweenDates(String issuerName, LocalDateTime from, LocalDateTime till) {
        return List.of(0.0);
    }

    private Block getPriceByNearestDate(Request<Response> request, LocalDate date) {
        var response = request.get(Map.of("date", date.format(DATE_PATTERN)));
        return response.findBlock("aggregates").orElseThrow();
    }

    private Double extractPrice(List<List<Object>> data) {
        var item = data.get(0);
        return (Double) item.get(5) / (Integer) item.get(6);
    }
}
