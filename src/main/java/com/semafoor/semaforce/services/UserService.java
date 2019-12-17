package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.model.view.UserView;
import com.semafoor.semaforce.model.view.WorkoutView;
import com.semafoor.semaforce.repositories.UserRepository;
import com.semafoor.semaforce.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * This service class serves as the bridge between the data access layer and the controller layer for the User
 * entity. This class is where the business logic can be implemented.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final PasswordEncoder bcryptEncoder;

    @Autowired
    public UserService(UserRepository repository, WorkoutRepository workoutRepository, PasswordEncoder bcryptEncoder) {
        this.userRepository = repository;
        this.workoutRepository = workoutRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    /**
     * Method that returns a UserView object based on whether or not the user already has a current workout.
     *
     * @param username users username.
     *
     * @return UserView object.
     */
    @Transactional(readOnly = true)
    public UserView findUserViewByUsername(String username) {
        return userRepository.findLoggedInUserByUserName(username);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<UserView> getUserViews() {
        return Lists.newArrayList(this.userRepository.getLoggedInUsers());
    }

    @Transactional(readOnly = true)
    public WorkoutView getCurrentWorkoutView(Long id) {
        return userRepository.getCurrentWorkoutView(id);
    }

    public User saveUser(@Valid User user){
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Workout> workouts = workoutRepository.findAllByUser(user);
            for (Workout workout: workouts) {
                workoutRepository.delete(workout);
            }
            userRepository.deleteResults(user);
            userRepository.delete(user);
        }
    }
}
