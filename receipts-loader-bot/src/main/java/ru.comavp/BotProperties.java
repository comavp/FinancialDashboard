package ru.comavp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties
@Configuration
public class BotProperties {

    private String telegramApiUrl;
    private String botToken;
    private String botName;
}
