package wid.bmsbackend.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SearchOptions {
    //시작일 Dto
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    //종료일 Dto
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String eui;
    private DecisionStatus status;
    private Long categoryId;
    private List<String> euiList;
    private List<BatteryModelResponse> batteryModel;
    private String commandCategory;
    private String commandAction;

    public LocalDateTime getStartDatetime() {
        return startDate.atStartOfDay();
    }

    public LocalDateTime getEndDatetime() {
        return endDate.atStartOfDay().plusDays(1).plusSeconds(-1);
    }

}
