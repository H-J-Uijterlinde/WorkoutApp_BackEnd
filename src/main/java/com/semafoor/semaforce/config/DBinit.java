package com.semafoor.semaforce.config;

import com.semafoor.semaforce.model.entities.exercise.Category;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.exercise.Muscle;
import com.semafoor.semaforce.model.entities.user.AccountInfo;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.ExerciseConfiguration;
import com.semafoor.semaforce.model.entities.workout.ScheduledExercise;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.repositories.ExerciseRepository;
import com.semafoor.semaforce.repositories.MuscleRepository;
import com.semafoor.semaforce.repositories.UserRepository;
import com.semafoor.semaforce.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DBinit {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

//    @PostConstruct
    public void initDB() {
        User user = createTestUser();
        userRepository.save(user);

        Exercise exercise = createTestExercise();
        exerciseRepository.save(exercise);

        ExerciseConfiguration configuration = new ExerciseConfiguration(3, 6, 8, 180);
        ScheduledExercise scheduledExercise = new ScheduledExercise(exercise, configuration);

        TrainingDay trainingDay = new TrainingDay(1);
        trainingDay.getScheduledExercises().put(1, scheduledExercise);

        Workout workout = new Workout(user, "MyWorkout", 4, 4);
        workout.addTrainingDay(trainingDay);
        workoutRepository.save(workout);

    }

    private Exercise createTestExercise() {
        Muscle muscle = new Muscle("test", "test");
        Muscle tricep = new Muscle("test2", "test2");
        muscleRepository.save(muscle);
        muscleRepository.save(tricep);
        Set<Muscle> muscles = new HashSet<>();
        muscles.add(muscle);
        muscles.add(tricep);
        return new Exercise("test exercise", muscles, muscle, Category.Main);
    }

    private User createTestUser() {
        AccountInfo info = new AccountInfo("Henk-Jan", "Uijterlinde", "testdb@user.nl");
        return new User("Semafoordb", "testpass", info);
    }
}
