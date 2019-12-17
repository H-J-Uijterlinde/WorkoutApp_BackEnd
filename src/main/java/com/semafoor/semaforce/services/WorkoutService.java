package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.model.view.ScheduledExerciseViewWrapper;
import com.semafoor.semaforce.model.view.WorkoutView;
import com.semafoor.semaforce.repositories.UserRepository;
import com.semafoor.semaforce.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This service class serves as the bridge between the data access layer and the controller layer for the Workout
 * entity. This class is where the business logic can be implemented.
 */
@Service
@Transactional
public class WorkoutService {

    private final WorkoutRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public WorkoutService(WorkoutRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Workout> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public Workout findById(Long id) {
        return repository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public TrainingDay getTrainingDayByWorkoutId(Long id, int dayNumber) {
        return this.repository.getTrainingDay(id, dayNumber);
    }

    @Transactional(readOnly = true)
    public List<TrainingDay> getAllTrainingDaysFromWorkout(Long id) {
        return this.repository.getAllTrainingDaysFromWorkout(id);
    }

    @Transactional(readOnly = true)
    public List<WorkoutView> getWorkoutViewsByUserId(Long userId) {
        return this.repository.getWorkoutViewsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<ScheduledExerciseViewWrapper> getTrainingDayViewsByWorkoutId(Long workoutId) {

        List<TrainingDay> trainingDays = this.repository.getAllTrainingDaysFromWorkout(workoutId);
        List<ScheduledExerciseViewWrapper> trainingDayViews = new ArrayList<>();
        trainingDays.forEach(
                trainingDay -> trainingDayViews.add(ScheduledExerciseViewWrapper.transformToView(trainingDay))
        );

        return trainingDayViews;
    }

    public Workout save(Workout workout) {

        User user = userRepository.findById(workout.getUser().getId()).get();
        user.setCurrentWorkout(workout);

        return repository.save(workout);
    }

    public void delete(Long id) {
        Workout workout = repository.findById(id).get();
        repository.delete(workout);
    }

    // todo: add controller method
    public List<TrainingDay> getInstantWorkoutTrainingDays(long userId) {
        return this.repository.getAllTrainingDaysFromInstantWorkoutByUserId(userId);
    }
}
