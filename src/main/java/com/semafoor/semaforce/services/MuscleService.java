package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.view.MuscleView;
import com.semafoor.semaforce.model.entities.exercise.Muscle;
import com.semafoor.semaforce.repositories.ExerciseRepository;
import com.semafoor.semaforce.repositories.MuscleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

/**
 * This service class serves as the bridge between the data access layer and the controller layer for the Muscle
 * entity. This class is where the business logic can be implemented.
 */
@Service
@Transactional
public class MuscleService {

    private final MuscleRepository muscleRepository;
    private final ExerciseRepository exerciseRepository;


    @Autowired
    public MuscleService(MuscleRepository repository, ExerciseRepository exerciseRepository) {
        this.muscleRepository = repository;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional(readOnly = true)
    public List<Muscle> findAll() {
        return Lists.newArrayList(muscleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<MuscleView> getAllMuscleViews() {
        return Lists.newArrayList(this.muscleRepository.getAllMuscleViews());
    }

    @Transactional(readOnly = true)
    public List<MuscleView> getMuscleViewsByExerciseId(Long id) {
        return Lists.newArrayList(this.muscleRepository.getMuscleViewsByExerciseId(id));
    }

    @Transactional(readOnly = true)
    public Muscle findById(Long id) {
        return muscleRepository.findById(id).get();
    }

    public Muscle save(@Valid Muscle muscle) {
        return muscleRepository.save(muscle);
    }

    public void delete(Long id) {
        Muscle muscle = muscleRepository.findById(id).get();
        exerciseRepository.deletePrimaryMuscleTrained(id);
        muscleRepository.delete(muscle);
    }
}
