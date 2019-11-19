package com.semafoor.semaforce.model.dto.results;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChartResponse {

    private List<LocalDateTime> dates;
    private List<Double> estimatedWeight;

    public void addDataPoint(LocalDateTime date, double estimatedWeight) {
        this.dates.add(date);
        this.estimatedWeight.add(estimatedWeight);
    }
}
