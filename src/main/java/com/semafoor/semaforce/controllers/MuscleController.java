package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.view.MuscleView;
import com.semafoor.semaforce.model.entities.exercise.Muscle;
import com.semafoor.semaforce.services.MuscleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class responsible for exposing endpoints for receiving and sending http requests containing the Muscle
 * entity or its derivatives.
 */
@Controller
@RequestMapping("/muscles")
@CrossOrigin
public class MuscleController {

    private MuscleService service;

    @Autowired
    public MuscleController(MuscleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Muscle>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Muscle> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping("/view")
    public ResponseEntity<List<MuscleView>> findAllViews() {
        return ResponseEntity.ok(this.service.getAllMuscleViews());
    }

    @GetMapping("/exercise_id={id}")
    public ResponseEntity<List<MuscleView>> getMuscleViewsByExerciseId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.service.getMuscleViewsByExerciseId(id));
    }

    @PostMapping
    public ResponseEntity<Muscle> createMuscle(@RequestBody Muscle muscle) {
        this.service.save(muscle);
        return new ResponseEntity<>(muscle, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMuscle(@PathVariable("id") Long id) {
        this.service.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
