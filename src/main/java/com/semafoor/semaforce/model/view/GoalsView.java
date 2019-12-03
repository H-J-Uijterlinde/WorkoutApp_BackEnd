package com.semafoor.semaforce.model.view;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GoalsView {

    private Long id;
    private String title;
    private String subTitle;
    private int completionPercentage;
    private LocalDateTime startedDate;
    private LocalDateTime lastUpdateDate;

    public GoalsView(Long id, String title, String subTitle, int completionPercentage,
                     LocalDateTime startedDate, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.completionPercentage = completionPercentage;
        this.startedDate = startedDate;
        this.lastUpdateDate = lastUpdateDate;
    }
}
