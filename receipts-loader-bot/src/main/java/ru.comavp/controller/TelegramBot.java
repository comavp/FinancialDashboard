package ru.comavp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.comavp.BotProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ru.comavp.ApiUtils.FILE_URL;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotProperties botProperties;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        System.out.println(botProperties.getBotName());
        return botProperties.getBotName();
    }

    @Override
    public String getBotToken() {
        System.out.println(botProperties.getBotToken());
        return botProperties.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        var userMessage = update.getMessage();
        String chatId = userMessage.getChatId().toString();
        if (userMessage.hasDocument()) {
            var userDocument = userMessage.getDocument();
            System.out.println("File received: " + userDocument.getFileName());
            String data = downloadReceipt(userMessage.getDocument(), chatId);
            System.out.println(data);
            sendMessage(chatId, "Файл обработан");
        } else if (userMessage.hasText()) {
            System.out.println("Message received: " + update.getMessage().getText());
            sendMessage(chatId, "Сообщение обработано");
        }
    }

    private String downloadReceipt(Document document, String chatId) {
        try {
            String fileName = document.getFileName();
            String fileId = document.getFileId();
            long fileSize = document.getFileSize();
            System.out.println("Received file: " + fileName + " (size: " + fileSize + " bytes)");

            var getFileMethod = new GetFile();
            getFileMethod.setFileId(fileId);
            File file = execute(getFileMethod);

            String fileUrl = botProperties.getTelegramApiUrl() + FILE_URL + botProperties.getBotToken() + "/" + file.getFilePath();
            System.out.println("File url: " + fileUrl);

            return downloadAndReadFile(fileUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String downloadAndReadFile(String fileUrl) throws IOException {
        try (InputStream inputStream = new URL(fileUrl).openStream();
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {

            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            return content.toString();
        }
    }

    private void sendMessage(String chatId, String text) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
