package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.entities.exercise.Category;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.exercise.MuscleGroup;
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

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping("/view")
    public ResponseEntity<List<ExerciseView>> findAllViews() {
        return ResponseEntity.ok(this.service.getExerciseViews());
    }

    /**
     * This method handles http calls to the uri specified in the @GetMapping annotation. All the path variables in the
     * URI are optional and determined by end-user choices.
     *
     * @param name name or part of the exercise name to be used in a where clause.
     * @param category category enum value to be used in the where clause.
     * @param muscleId id of a Muscle entity to be used in the where clause.
     *
     * @return ResponseEntity containing a list of ExerciseView objects.
     */
    @GetMapping("/view/nameContains={name}/category={category}/muscleGroup={muscleGroup}/muscleId={muscleId}")
    public ResponseEntity<List<ExerciseView>> findViewsByCriteria(@PathVariable(value = "name", required = false) String name,
                                                                  @PathVariable(value = "category", required = false)Category category,
                                                                  @PathVariable(value = "muscleGroup", required = false) MuscleGroup muscleGroup,
                                                                  @PathVariable(value = "muscleId", required = false) Long muscleId) {

        return ResponseEntity.ok(this.service.getExerciseViewsByCriteria(name, category, muscleGroup, muscleId));
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
