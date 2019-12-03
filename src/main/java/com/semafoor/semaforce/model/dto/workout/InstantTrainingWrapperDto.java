package com.semafoor.semaforce.model.dto.workout;

import com.semafoor.semaforce.model.dto.results.WeeklyResultDto;
import lombok.Data;

import java.util.List;

@Data
public class InstantTrainingWrapperDto {

    private Long userId;
    private TrainingDayDto trainingDayDto;
    private List<WeeklyResultDto> weeklyResultDtos;
}
