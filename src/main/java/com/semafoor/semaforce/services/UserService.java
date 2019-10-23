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

    @Transactional(readOnly = true)
    public UserView findUserByUsername(String username) {
        User user = this.userRepository.findByUserName(username);
        if (user.getCurrentWorkout() == null) {
            return this.userRepository.findLoggedInUserNoWorkoutByUserName(username);
        } else {
            return userRepository.findLoggedInUserByUserName(username);
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public List<UserView> getLoggedInUsers() {
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
        User user = userRepository.findById(id).get();
        List<Workout> workouts = workoutRepository.findAllByUser(user);
        for (Workout workout: workouts) {
            workoutRepository.delete(workout);
        }
        userRepository.deleteResults(user);
        userRepository.delete(user);
    }
}
