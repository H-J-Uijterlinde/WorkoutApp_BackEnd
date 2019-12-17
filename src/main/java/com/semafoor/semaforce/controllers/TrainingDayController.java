package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.dto.results.WeeklyResultDto;
import com.semafoor.semaforce.model.dto.workout.InstantTrainingWrapperDto;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.view.TrainingDayView;
import com.semafoor.semaforce.services.GoalsService;
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
    private final GoalsService goalsService;

    @Autowired
    public TrainingDayController(TrainingDayService trainingDayService, GoalsService goalsService) {
        this.trainingDayService = trainingDayService;
        this.goalsService = goalsService;
    }

    @GetMapping("/{trainingDayId}")
    public ResponseEntity<TrainingDay> getTrainingDayById(@PathVariable("trainingDayId") Long trainingDayId) {
        return ResponseEntity.ok(this.trainingDayService.getTrainingDayById(trainingDayId));
    }

    @GetMapping("/views/user={userId}")
    public ResponseEntity<List<TrainingDayView>> getTrainingDaysByInstantWorkoutAndUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(this.trainingDayService.getTrainingDaysByInstantWorkoutAndUserId(userId));
    }

    @PostMapping("/{trainingDayId}/add_results/user={userId}")
    public ResponseEntity<TrainingDay> addResults(@PathVariable("trainingDayId") Long trainingDayId,
                                                  @PathVariable("userId") Long userId,
                                                  @RequestBody List<WeeklyResultDto> weeklyResultDtos) {

        // pass weekly result dto's on to goals service to update active goals if needed.
        this.goalsService.updateActiveGoals(userId, weeklyResultDtos);

        return ResponseEntity.ok(this.trainingDayService.addWeeklyResult(trainingDayId, weeklyResultDtos));
    }

    @PostMapping("/instant_training")
    public ResponseEntity<TrainingDay> addNewInstantTrainingDay(@RequestBody InstantTrainingWrapperDto wrapperDto) {


        this.goalsService.updateActiveGoals(wrapperDto.getUserId(), wrapperDto.getWeeklyResultDtos());

        return ResponseEntity.ok(this.trainingDayService.addNewInstantTrainingDay(wrapperDto.getUserId(),
                wrapperDto.getTrainingDayDto(), wrapperDto.getWeeklyResultDtos()));
    }
}
