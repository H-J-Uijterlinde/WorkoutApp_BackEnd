package com.semafoor.semaforce.services;

import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.repositories.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private TrainingDay trainingDay;
    private Workout workout;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.trainingDay = mock(TrainingDay.class);
        this.workout = mock(Workout.class);
    }

    @Test
    void getTrainingDaysFromInstantWorkoutByUserId() {
        when(this.workoutRepository.getAllTrainingDaysFromInstantWorkoutByUserId(1L)).thenReturn(Collections.singletonList(this.trainingDay));

        List<TrainingDay> returnedTrainingDays = this.workoutService.getInstantWorkoutTrainingDays(1L);

        verify(this.workoutRepository).getAllTrainingDaysFromInstantWorkoutByUserId(1L);
        assertThat(returnedTrainingDays).isNotNull().contains(this.trainingDay);
    }
}