package ru.comavp.dashboard;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.comavp.dashboard.service.ImportService;
import ru.comavp.dashboard.utils.DataUtils;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class ImportServiceTest {

    @Autowired
    ImportService importService;

    @Test
    public void testImportAllDataFromWorkBookSheet() throws IOException {
        importService.importAllDataFromWorkBookSheet(readFileToImport());
    }

    private Workbook readFileToImport() throws IOException {
        FileInputStream file = new FileInputStream(DataUtils.FILE_PATH);
        return new XSSFWorkbook(file);
    }
}
