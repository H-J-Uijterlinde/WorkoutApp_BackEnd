package com.semafoor.semaforce.model.dto.results;

import lombok.Data;

@Data
public class ChartRequest {

    private Long userId;
    private Long exerciseId;
    private int numReps;
}
