package com.semafoor.semaforce.model.view;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingDayView {

    private Long trainingDayId;
    private LocalDateTime createdDate;

    public TrainingDayView(Long trainingDayId, LocalDateTime createdDate) {
        this.trainingDayId = trainingDayId;
        this.createdDate = createdDate;
    }
}
