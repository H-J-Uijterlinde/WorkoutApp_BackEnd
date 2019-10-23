package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.dto.WorkoutDto;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/workouts")
@CrossOrigin
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    public ResponseEntity<List<Workout>> findAll() {
        return ResponseEntity.ok(this.workoutService.findAll());
    }

    @GetMapping("/{id}/day{number}")
    public ResponseEntity<TrainingDay> getTrainingDayByWorkoutId(@PathVariable("id") Long id,
                                                                 @PathVariable("number") int dayNumber) {

        return ResponseEntity.ok(this.workoutService.getTrainingDayByWorkoutId(id, dayNumber));
    }

    @PostMapping
    public ResponseEntity<Workout> createWorkout(@RequestBody WorkoutDto workout) {
        Workout savedWorkout = this.workoutService.save(workout.transform());
        return new ResponseEntity<>(savedWorkout, HttpStatus.CREATED);
    }
}
