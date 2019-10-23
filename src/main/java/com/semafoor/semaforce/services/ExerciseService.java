package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.view.ExerciseView;
import com.semafoor.semaforce.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service class serves as the bridge between the data access layer and the controller layer for the Exercise
 * entity. This class is where the business logic can be implemented.
 */
@Service
@Transactional
public class ExerciseService {

    private final ExerciseRepository repository;

    @Autowired
    public ExerciseService(ExerciseRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Exercise> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public List<ExerciseView> getExerciseViews() {
        return Lists.newArrayList(repository.getExerciseViews());
    }

    @Transactional(readOnly = true)
    public Exercise findById(Long id) {
        return repository.findById(id).get();
    }

    public Exercise save(Exercise exercise) {
        return repository.save(exercise);
    }

    public void delete(Long id) {
        Exercise exercise = repository.findById(id).get();
        repository.delete(exercise);
    }
}
