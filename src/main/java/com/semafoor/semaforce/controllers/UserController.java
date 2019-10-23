package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.view.UserView;
import com.semafoor.semaforce.model.view.WorkoutView;
import com.semafoor.semaforce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping("/logged_in_users")
    public ResponseEntity<List<UserView>> findAllLoggedInUsers() {
        return ResponseEntity.ok(this.userService.getLoggedInUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserView> findByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(this.userService.findUserByUsername(username));
    }

    @GetMapping("/{id}/current_workout")
    public ResponseEntity<WorkoutView> getUserCurrentWorkout(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(this.userService.getCurrentWorkoutView(userId));
    }


    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        user = this.userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
