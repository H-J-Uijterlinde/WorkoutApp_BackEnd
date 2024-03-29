package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.dto.goals.ExerciseGoalsDto;
import com.semafoor.semaforce.model.dto.goals.TotalWorkoutsDto;
import com.semafoor.semaforce.model.entities.goals.ExerciseGoals;
import com.semafoor.semaforce.model.entities.goals.Goals;
import com.semafoor.semaforce.model.entities.goals.TotalWorkouts;
import com.semafoor.semaforce.model.view.GoalsView;
import com.semafoor.semaforce.services.GoalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/goals")
@CrossOrigin
public class GoalsController {

    private final GoalsService service;

    @Autowired
    public GoalsController(GoalsService service) {
        this.service = service;
    }

    @PostMapping("/total_workouts")
    public ResponseEntity<Goals> postNewTotalWorkoutsGoal(@RequestBody TotalWorkoutsDto totalWorkoutsDto) {
        TotalWorkouts totalWorkouts = totalWorkoutsDto.transform();
        return new ResponseEntity<>(this.service.postNewGoal(totalWorkouts), HttpStatus.CREATED);
    }

    @PostMapping("/exercise_goals")
    public ResponseEntity<Goals> postNewExerciseGoal(@RequestBody ExerciseGoalsDto exerciseGoalsDto) {
        ExerciseGoals exerciseGoals = exerciseGoalsDto.transform();
        return new ResponseEntity<>(this.service.postNewGoal(exerciseGoals), HttpStatus.CREATED);
    }

    @PutMapping("/exercise_goals")
    public ResponseEntity<Goals> updateExerciseGoals(@RequestBody ExerciseGoalsDto goals) {
        return new ResponseEntity<>(this.service.updateGoal(goals.transform()), HttpStatus.OK);
    }

    @GetMapping("/view/user={id}/active={active}")
    public ResponseEntity<List<GoalsView>> getAllGoalsViewsByUserId(@PathVariable("id") Long id, @PathVariable("active") boolean isActive) {
        return ResponseEntity.ok(this.service.getAllGoalsViews(id, isActive));
    }

    @GetMapping("/user={id}")
    public ResponseEntity<List<Goals>> findAllGoalsByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.service.findAllGoalsByUserId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGoals(@PathVariable("id") Long id) {
        this.service.deleteGoal(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
