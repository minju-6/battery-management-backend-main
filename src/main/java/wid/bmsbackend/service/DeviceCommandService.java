package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.DeviceCommandRequest;
import wid.bmsbackend.dto.DeviceCommandResponse;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.DeviceCommand;
import wid.bmsbackend.entity.DeviceCommandType;
import wid.bmsbackend.repository.DeviceCommandRepository;
import wid.bmsbackend.utils.ExcelStyleUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceCommandService {
    private final DeviceCommandRepository deviceCommandRepository;

    @Transactional
    public void createDeviceCommand(DeviceCommandRequest deviceCommandRequest) {
        for (String eui : deviceCommandRequest.getEuiList()) {
            DeviceCommand deviceCommand = new DeviceCommand(eui, deviceCommandRequest.getCommandType());
            deviceCommandRepository.save(deviceCommand);
        }
    }

    @Transactional(readOnly = true)
    public DeviceCommand findByEui(String eui) {
        return deviceCommandRepository.findFirstByEuiAndCommandNotInOrderByCreatedDate(eui,
                List.of(DeviceCommandType.PULSE_START_PROCESSED, DeviceCommandType.PULSE_STOP_PROCESSED,
                        DeviceCommandType.CELL_BALANCING_START_PROCESSED, DeviceCommandType.CELL_BALANCING_STOP_PROCESSED))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<DeviceCommandResponse> findAllWithSearchOptions(Pageable pageable, SearchOptions searchOptions) {
        return deviceCommandRepository.findAllWithSearchOptions(pageable, searchOptions)
                .map(DeviceCommandResponse::of);
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream downloadFile(Pageable pageable) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        createDeviceCommand(sheet, pageable);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public void createDeviceCommand(Sheet sheet, Pageable pageable) {
        Workbook workbook = sheet.getWorkbook();
        CellStyle headerStyle = ExcelStyleUtil.getHeaderStyle(workbook);
        CellStyle bodyStyle = ExcelStyleUtil.getBodyStyle(workbook);

        String[] header = getHeader(sheet, headerStyle);
        int index = 1;
        Page<DeviceCommand> page;

        do {
            page = deviceCommandRepository.findAll(pageable);
            for (DeviceCommand deviceCommand : page.getContent()) {
                String date = deviceCommand.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Row bodyRow = sheet.createRow(index++);

                bodyRow.createCell(0).setCellValue(index - 1);
                bodyRow.createCell(1).setCellValue(date);
                bodyRow.createCell(2).setCellValue(deviceCommand.getEui());
                bodyRow.createCell(3).setCellValue(deviceCommand.getCommand().toString());
                bodyRow.createCell(4).setBlank();

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
        String[] header = {"Id", "시간", "배터리번호", "명령", "비고"};

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
