package ru.comavp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.comavp.entity.Item;
import ru.comavp.entity.Receipt;
import ru.comavp.entity.Request;
import ru.comavp.enums.ReceiptType;
import ru.comavp.enums.RequestStatus;
import ru.comavp.repository.ItemRepository;
import ru.comavp.repository.ReceiptRepository;
import ru.comavp.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ReceiptLoaderService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private RequestRepository requestRepository;

    public void saveFileDate(String data) {
        Request request = parseRequest(data);
        requestRepository.save(request);
        request.getReceipts().forEach(receipt -> {
            receipt.setRequest(request);
            receipt.getItems().forEach(item -> {
                item.setReceipt(receipt);
            });
        });
        receiptRepository.saveAll(request.getReceipts());
        request.getReceipts().forEach(receipt -> itemRepository.saveAll(receipt.getItems()));
    }

    private Request parseRequest(String data) {
        var request = Request.builder()
                .data(data)
                .status(RequestStatus.SUCCESS.toString());
        try {
            request.receipts(Arrays.stream(objectMapper.readValue(data, JsonNode[].class))
                    .map(this::parseReceipt)
                    .toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return request.build();
    }

    private Receipt parseReceipt(JsonNode receiptData) {
        var receipt = Receipt.builder()
                .originalId(receiptData.get("_id").asText());
        JsonNode jsonNode = receiptData.get("ticket").get("document");
        if (jsonNode.has("receipt")) {
            jsonNode = jsonNode.get("receipt");
        } else if (jsonNode.has("bso")) {
            jsonNode = jsonNode.get("bso");
            receipt.type(ReceiptType.BSO.toString());
        } else {
            throw new RuntimeException("Wrong document type");
        }
        return receipt
                .totalSum(jsonNode.get("totalSum").asLong()) // todo
                .retailPlace(jsonNode.get("retailPlace").asText())
                .receiptCreationDate(LocalDateTime.parse(jsonNode.get("dateTime").asText()))
                .items(StreamSupport.stream(jsonNode.get("items").spliterator(), false)
                        .map(this::parseItem)
                        .toList())
                .build();
    }

    private Item parseItem(JsonNode itemData) {
        return Item.builder()
                .name(itemData.get("name").asText())
                .price(itemData.get("price").asDouble()) // todo
                .quantity(itemData.get("quantity").asLong())
                .build();
    }
}
