package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.dto.workout.WorkoutDto;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.model.view.ScheduledExerciseViewWrapper;
import com.semafoor.semaforce.model.view.WorkoutView;
import com.semafoor.semaforce.services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class responsible for exposing endpoints for receiving and sending http requests containing the Workout
 * entity or its derivatives.
 */
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

    @GetMapping("/{id}/training_days")
    public ResponseEntity<List<TrainingDay>> getAllTrainingDaysByWorkoutId(@PathVariable("id") Long id) {

        return ResponseEntity.ok(this.workoutService.getAllTrainingDaysFromWorkout(id));
    }

    @GetMapping("/user={userId}")
    public ResponseEntity<List<WorkoutView>> getWorkoutViewsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(this.workoutService.getWorkoutViewsByUserId(userId));
    }

    @GetMapping("/{workoutId}/training_day_views")
    public ResponseEntity<List<ScheduledExerciseViewWrapper>> getTrainingDayViewsByWorkoutId(@PathVariable("workoutId") Long workoutId) {
        return ResponseEntity.ok(this.workoutService.getTrainingDayViewsByWorkoutId(workoutId));
    }

    @PostMapping
    public ResponseEntity<Workout> createWorkout(@RequestBody WorkoutDto workout) {
        Workout savedWorkout = this.workoutService.save(workout.transform());
        return new ResponseEntity<>(savedWorkout, HttpStatus.CREATED);
    }
}
