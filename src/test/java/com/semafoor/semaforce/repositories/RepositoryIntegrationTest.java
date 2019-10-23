package com.semafoor.semaforce.repositories;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.entities.exercise.Category;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.exercise.Muscle;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.result.Score;
import com.semafoor.semaforce.model.entities.user.AccountInfo;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.ExerciseConfiguration;
import com.semafoor.semaforce.model.entities.workout.ScheduledExercise;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.NotNull;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void saveUser() {
        User testUser = createTestUser();
        testUser.setUserName("Sema");
        testUser.getInfo().setEmail("something@test.nl");
        userRepository.save(testUser);

        List<User> users = Lists.newArrayList(userRepository.findAll());
        assertEquals(2, users.size());

        User returnedUser = users.get(0);
        assertEquals("Dommel", returnedUser.getUserName());
        assertEquals("Henk-Jan", returnedUser.getInfo().getFirstName());
    }

    @Test
    public void saveMuscle() {
        Muscle muscle = getTestMuscle();
        muscle.setName("Testing");
        muscle.setScientificName("anotherTest");
        muscleRepository.save(muscle);

        List<Muscle> muscles = Lists.newArrayList(muscleRepository.findAll());
        assertEquals(2, muscles.size());
    }

    @Test
    public void saveExercise() {
        Exercise exercise = getTestExercise();
        exercise.setName("Double B");
        exercise.getMusclesTrained().(0).setScientificName("trapezius");
        exercise.getMusclesTrained().get(0).setName("traps");
        exerciseRepository.save(exercise);

        List<Exercise> exercises = Lists.newArrayList(exerciseRepository.findAllWithMuscles());
        assertEquals(1, exercises.size());

        assertEquals(1, exercises.get(0).getMusclesTrained().size());

        System.out.println(exercises.get(0).getCategory().toString());
    }

    @Test
    public void saveResult() {
        Result result = getTestResult();
        result.getUser().setUserName("Balthazar");
        result.getUser().getInfo().setEmail("Nudan@DE.nl");
        resultRepository.save(result);

        List<Result> results = Lists.newArrayList(resultRepository.findAll());
        assertEquals(1, results.size());

        assertEquals(100, results.get(0).getNumbersLifted().get(1).getWeightLifted());

        assertNotNull(results.get(0).getExercise());
        assertNotNull(results.get(0).getUser());
    }

    @Test
    public void saveWorkout() {
        Result result = getTestResult();
        resultRepository.save(result);

        Exercise exercise = result.getExercise();
        ExerciseConfiguration configuration = new ExerciseConfiguration(3, 6, 8, 180);
        ScheduledExercise scheduledExercise = new ScheduledExercise(exercise, configuration);

        TrainingDay trainingDay = new TrainingDay(1, currentWeek);
        trainingDay.getScheduledExercises().put(1, scheduledExercise);

        Workout workout = new Workout(result.getUser(), "MyWorkout", 4, 4);
        workout.addTrainingDay(trainingDay);
        workoutRepository.save(workout);

        List<Workout> workouts = Lists.newArrayList(workoutRepository.findAll());
        assertEquals(1, workouts.size());
        assertNotNull(workouts.get(0).getTrainingDays().get(1));

        assertNotNull(workouts.get(0).getTrainingDays().get(1).getWorkout());

        assertEquals("Bench Press", workouts.get(0).getTrainingDays().get(1).getScheduledExercises().get(1).getExercise().getName());
    }

    private Result getTestResult() {
        User user = createTestUser();
        user.setUserName("Dommel");
        user.getInfo().setEmail("Dedikste@test.nl");
        userRepository.save(user);

        Exercise exercise = getTestExercise();
        exerciseRepository.save(exercise);

        Score score1 = new Score(100, 8, 9);
        Score score2 = new Score(100, 7, 9);
        Score score3 = new Score(100, 7, 10);
        Map<Integer, Score> weightsLifted = new HashMap<>();
        weightsLifted.put(1, score1);
        weightsLifted.put(2, score2);
        weightsLifted.put(3, score3);

        return new Result(user, exercise, weightsLifted);
    }

    private User createTestUser() {
        AccountInfo info = new AccountInfo("Henk-Jan", "Uijterlinde", "test@user.nl");
        return new User("Semafoor", "testpass", info);
    }

    private Exercise getTestExercise() {
        Muscle muscle = getTestMuscle();
        muscle.setName("Lats");
        muscle.setScientificName("Latisimus");
        muscleRepository.save(muscle);

        @NotNull(message = "The muscles trained with this exercise must be set") Set<Muscle> muscles = new ArrayList<>();
        muscles.add(muscle);
        return new Exercise("Bench Press", muscles, Category.MAIN);
    }


    private Muscle getTestMuscle() {
        return new Muscle("Chest", "Pectoralis");
    }

    @AfterEach
    void tearDown() {
    }
}