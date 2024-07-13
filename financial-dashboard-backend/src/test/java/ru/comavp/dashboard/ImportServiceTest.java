package ru.comavp.dashboard;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.comavp.dashboard.service.ImportService;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class ImportServiceTest {

    @Autowired
    ImportService importService;

    @Test
    public void testImportInvestTransactions() throws IOException {
        importService.importInvestTransactions(readFileToImport());
    }

    private Workbook readFileToImport() throws IOException {
        //FileInputStream file = new FileInputStream("test_file.xlsx"); // todo add relative filePath
        FileInputStream file = new FileInputStream("D:\\JavaProjects\\FinancialDashboard\\financial-dashboard-backend\\src\\test\\resources\\test_file.xlsx");
        return new XSSFWorkbook(file);
    }
}
