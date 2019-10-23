package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.view.UserView;
import com.semafoor.semaforce.model.view.WorkoutView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository responsible for CRUD database operations on the User entity.
 * Queries for the function which are not defined through the CrudRepository interface are defined as named queries on
 * the corresponding class.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserName(String username);

    UserView findLoggedInUserByUserName(@Param("username") String username);

    UserView findLoggedInUserNoWorkoutByUserName(@Param("username") String username);

    List<UserView> getLoggedInUsers();

    WorkoutView getCurrentWorkoutView(@Param("id") Long id);

    @Modifying
    void deleteWorkouts(@Param("user") User user);

    @Modifying
    void deleteResults(@Param("user") User user);
}
