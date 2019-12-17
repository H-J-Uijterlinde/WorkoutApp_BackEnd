package com.semafoor.semaforce.services;

import com.semafoor.semaforce.model.dto.results.WeeklyResultDto;
import com.semafoor.semaforce.model.dto.workout.TrainingDayDto;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.ScheduledExercise;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class TrainingDayServiceTest {

    @Mock
    private TrainingDayRepository trainingDayRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private TrainingDayService trainingDayService;

    private WeeklyResultDto weeklyResultDto1;
    private WeeklyResultDto weeklyResultDto2;
    private TrainingDay trainingDay;
    private List<WeeklyResultDto> weeklyResultDtos = new ArrayList<>();
    private Result result1;
    private Result result2;
    private User user;
    private Workout workout;
    private TrainingDayDto trainingDayDto;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.initializeWeeklyResults();
        this.initializeTrainingDay();
        this.trainingDayDto = mock(TrainingDayDto.class);
    }

    private void initializeTrainingDay() {
        this.trainingDay = new TrainingDay(1);
        this.result1 = new Result();
        this.result1.setId(50L);
        this.result2 = new Result();
        this.result2.setId(51L);
        ScheduledExercise exercise1 = new ScheduledExercise();
        exercise1.setResults(result1);
        ScheduledExercise exercise2 = new ScheduledExercise();
        exercise2.setResults(result2);
        this.trainingDay.getScheduledExercises().put(1, exercise1);
        this.trainingDay.getScheduledExercises().put(2, exercise2);

        this.user = new User();
        user.setId(100L);
        this.workout = new Workout();
        workout.addTrainingDay(trainingDay);
        workout.setUser(user);
    }

    private void initializeWeeklyResults() {
        this.weeklyResultDto1 = new WeeklyResultDto();
        this.weeklyResultDto1.setExerciseNumber(1);
        this.weeklyResultDto1.setWeekNumber(1);
        this.weeklyResultDto1.setWeightsLifted(Arrays.asList(100D, 110D, 120D));
        this.weeklyResultDto1.setRepetitionsPerformed(Arrays.asList(10, 9, 8));
        this.weeklyResultDto1.setRpe(Arrays.asList(8D, 9D, 10D));
        this.weeklyResultDto1.setExerciseId(1000L);

        this.weeklyResultDto2 = new WeeklyResultDto();
        this.weeklyResultDto2.setExerciseNumber(2);
        this.weeklyResultDto2.setWeekNumber(1);
        this.weeklyResultDto2.setWeightsLifted(Arrays.asList(10D, 11D, 12D));
        this.weeklyResultDto2.setRepetitionsPerformed(Arrays.asList(10, 9, 8));
        this.weeklyResultDto2.setRpe(Arrays.asList(8D, 9D, 10D));
        this.weeklyResultDto2.setExerciseId(2000L);

        this.weeklyResultDtos.add(weeklyResultDto1);
        this.weeklyResultDtos.add(weeklyResultDto2);
    }

    @Test
    void addWeeklyResult() {
        when(this.trainingDayRepository.findById(1L)).thenReturn(ofNullable(this.trainingDay));

        this.trainingDayService.addWeeklyResult(1L, weeklyResultDtos);

        verify(this.trainingDayRepository).save(this.trainingDay);
        assertThat(this.trainingDay.getScheduledExercises().get(1).getResults().getWeeklyResults()).hasSize(1).containsValues(weeklyResultDto1.transform());
        assertThat(this.trainingDay.getScheduledExercises().get(2).getResults().getWeeklyResults()).hasSize(1).containsValues(weeklyResultDto2.transform());
        assertThat(this.trainingDay.getCurrentWeek()).isEqualTo(2);
    }

    @Test
    void addNewInstantTrainingDayWhenInstantWorkoutIsPresentAndRelevantResultsArePresent() {
        when(this.userRepository.findById(100L)).thenReturn(Optional.of(this.user));
        when(this.workoutRepository.getInstantWorkoutByUserId(100L)).thenReturn(Optional.of(this.workout));
        when(this.trainingDayDto.transform()).thenReturn(this.trainingDay);
        when(this.resultRepository.findInstantTrainingResultsByExerciseIdAndUserId(100L, 1000L)).thenReturn(Optional.of(this.result1));
        when(this.resultRepository.findInstantTrainingResultsByExerciseIdAndUserId(100L, 2000L)).thenReturn(Optional.of(this.result2));

        TrainingDay returnedTrainingDay = this.trainingDayService.addNewInstantTrainingDay(100L, this.trainingDayDto, this.weeklyResultDtos);

        assertThat(this.trainingDay.getDayNumber()).isEqualTo(2);
        assertThat(this.workout.getTrainingDays()).hasSize(2);
        assertThat(this.workout.getTrainingDays().get(2)).isEqualTo(this.trainingDay);
        verify(this.workoutRepository).save(this.workout);
        verify(this.resultRepository).save(this.result1);
        verify(this.resultRepository).save(this.result2);
        assertThat(this.result1.getWeeklyResults()).hasSize(1).containsValues(this.weeklyResultDto1.transform());
        assertThat(this.result2.getWeeklyResults()).hasSize(1).containsValues(this.weeklyResultDto2.transform());
        assertThat(this.weeklyResultDto1.getWeekNumber()).isEqualTo(1);
    }

    @Test
    void addNewInstantTrainingDayWhenNoInstantWorkoutIsPresentAndNoRelevantResultsArePresent() {
        when(this.userRepository.findById(100L)).thenReturn(Optional.of(this.user));
        when(this.trainingDayDto.transform()).thenReturn(this.trainingDay);
        Exercise exercise1 = new Exercise();
        exercise1.setId(1000L);
        Exercise exercise2 = new Exercise();
        exercise2.setId(2000L);
        when(this.exerciseRepository.findById(1000L)).thenReturn(Optional.of(exercise1));
        when(this.exerciseRepository.findById(2000L)).thenReturn(Optional.of(exercise2));

        this.result1 = new Result(this.user, exercise1, new HashMap<>());
        this.result1.setInstantTrainingResult(true);
        this.result1.addResult(this.weeklyResultDto1.transform());

        this.result2 = new Result(this.user, exercise2, new HashMap<>());
        this.result2.setInstantTrainingResult(true);
        this.result2.addResult(this.weeklyResultDto2.transform());

        this.workout = new Workout(this.user, "Instant training day holder",
                1, 1, true);
        this.workout.addTrainingDay(this.trainingDay);

        TrainingDay returnedTrainingDay = this.trainingDayService.addNewInstantTrainingDay(100L, this.trainingDayDto, this.weeklyResultDtos);


        assertThat(this.trainingDay.getDayNumber()).isEqualTo(1);
        assertThat(this.workout.getTrainingDays()).as("Number of training days").hasSize(1);
        assertThat(this.workout.getTrainingDays().get(1)).isEqualTo(this.trainingDay);
        verify(this.workoutRepository).save(this.workout);
        verify(this.resultRepository).findInstantTrainingResultsByExerciseIdAndUserId(100L, 1000L);
        verify(this.resultRepository).findInstantTrainingResultsByExerciseIdAndUserId(100L, 2000L);


        assertThat(this.result1.getWeeklyResults()).as("Result1 number of weekly results").hasSize(1).containsValues(this.weeklyResultDto1.transform());
        assertThat(this.result2.getWeeklyResults()).hasSize(1).as("Result2 number of weekly results").containsValues(this.weeklyResultDto2.transform());
        assertThat(this.weeklyResultDto1.getWeekNumber()).as("WeeklyResultDto1 weeknumber").isEqualTo(1);
        verify(this.resultRepository).save(this.result1);
        verify(this.resultRepository).save(this.result2);
    }
}