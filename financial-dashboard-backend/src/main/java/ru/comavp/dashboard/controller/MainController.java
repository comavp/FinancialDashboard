package ru.comavp.dashboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.comavp.dashboard.service.ImportService;
import ru.comavp.dashboard.utils.FileUtils;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MainController {

    private ImportService importService;

    @PostMapping(value = "/import-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importData(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importInvestTransactions(FileUtils.getWorkbook(file));
        return ResponseEntity.ok().build();
    }
}
