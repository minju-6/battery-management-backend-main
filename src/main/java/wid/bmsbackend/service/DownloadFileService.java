package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.DeviceMessageResponse;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.utils.ExcelStyleUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DownloadFileService {
    private final DeviceMessageService deviceMessageService;

    @Transactional(readOnly = true)
    public ByteArrayInputStream downloadFile(Pageable pageable, SearchOptions searchOptions) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        createDeviceMessageSheet(sheet, pageable, searchOptions);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public void createDeviceMessageSheet(Sheet sheet, Pageable pageable, SearchOptions searchOptions) throws IOException {
        Workbook workbook = sheet.getWorkbook();
        CellStyle headerStyle = ExcelStyleUtil.getHeaderStyle(workbook);
        CellStyle bodyStyle = ExcelStyleUtil.getBodyStyle(workbook);

        String[] header = getHeader(sheet, headerStyle);
        int index = 1;
        Page<DeviceMessageResponse> page;

        do {
            page = deviceMessageService.findAll(pageable, searchOptions);
            for (DeviceMessageResponse deviceMessage : page.getContent()) {
                String date = deviceMessage.createdDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Row bodyRow = sheet.createRow(index++);

                bodyRow.createCell(0).setCellValue(index-1);
                bodyRow.createCell(1).setCellValue(date);
                bodyRow.createCell(2).setCellValue(deviceMessage.eui());
                bodyRow.createCell(3).setCellValue(deviceMessage.status().toString());
                bodyRow.createCell(4).setCellValue(deviceMessage.temp());
                bodyRow.createCell(5).setCellValue(deviceMessage.current());
                bodyRow.createCell(6).setCellValue(deviceMessage.voltage());
                bodyRow.createCell(7).setCellValue(deviceMessage.impedance());
                bodyRow.createCell(8).setBlank();

                for (int i = 0; i < header.length; i++) {
                    bodyRow.getCell(i).setCellStyle(bodyStyle);
                }
                bodyRow.setHeightInPoints(20);
            }
            pageable = pageable.next();
        } while (page.hasNext());
    }


    private static String[] getHeader(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        String[] header = {"Id", "시간", "배터리번호", "상태", "온도", "전류", "전압", "임피던스", "비고"};

        for (int i = 0; i < header.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(header[i]);
            headerCell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 256 * 18);
        }

        headerRow.setHeightInPoints(25);
        return header;
    }


}
