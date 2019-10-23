package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.view.ExerciseView;
import com.semafoor.semaforce.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class responsible for exposing endpoints for receiving and sending http requests containing the Exercise
 * entity or its derivatives.
 */
@Controller
@RequestMapping("/exercises")
@CrossOrigin
public class ExerciseController {

    private ExerciseService service;

    @Autowired
    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Exercise>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/view")
    public ResponseEntity<List<ExerciseView>> findAllViews() {
        return ResponseEntity.ok(this.service.getExerciseViews());
    }

    @PostMapping
    public ResponseEntity<Exercise> createExercise(@RequestBody Exercise exercise) {
        exercise = this.service.save(exercise);
        return new ResponseEntity<>(exercise, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Exercise> updateUser(@RequestBody Exercise exercise) {
        this.service.save(exercise);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        this.service.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
