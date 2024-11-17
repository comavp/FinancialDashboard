package ru.comavp.dashboard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "import")
@Configuration
@Data
public class ImportProperties {

    private String historySheetName;
    private String issuersInfoSheetName;
    private String replenishmentsStartPosition;
    private String investmentsStartPosition;
    private String issuersInfoStartPosition;
}
