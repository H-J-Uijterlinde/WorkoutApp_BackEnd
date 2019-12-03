package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.dto.results.WeeklyResultDto;
import com.semafoor.semaforce.model.dto.workout.InstantTrainingWrapperDto;
import com.semafoor.semaforce.model.dto.workout.TrainingDayDto;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.services.TrainingDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class responsible for exposing endpoints for receiving and sending http requests containing the TrainingDay
 * entity or its derivatives.
 */
@Controller
@RequestMapping("/training_days")
@CrossOrigin
public class TrainingDayController {

    private final TrainingDayService trainingDayService;

    @Autowired
    public TrainingDayController(TrainingDayService trainingDayService) {
        this.trainingDayService = trainingDayService;
    }

    @PostMapping("/{id}/add_results")
    public ResponseEntity<TrainingDay> addResults(@PathVariable("id") Long trainingDayId,
                                                  @RequestBody List<WeeklyResultDto> weeklyResultDtos) {

        return ResponseEntity.ok(this.trainingDayService.addWeeklyResult(trainingDayId, weeklyResultDtos));
    }

    @PostMapping("/instant_training")
    public ResponseEntity<TrainingDay> addNewInstantTrainingDay(@RequestBody InstantTrainingWrapperDto wrapperDto) {

        return ResponseEntity.ok(this.trainingDayService.addNewInstantTrainingDay(wrapperDto.getUserId(),
                wrapperDto.getTrainingDayDto(), wrapperDto.getWeeklyResultDtos()));
    }
}
