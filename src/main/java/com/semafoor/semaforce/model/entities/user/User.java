package com.semafoor.semaforce.model.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.semafoor.semaforce.model.entities.AbstractEntity;
import com.semafoor.semaforce.model.entities.workout.Workout;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Defines the User entity.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "USERS")
@NamedQueries({
        @NamedQuery(
                name = "User.getCurrentWorkout",
                query = "SELECT U.currentWorkout FROM User U where U.id = :id"
        ),
        @NamedQuery(
                name = "User.deleteWorkouts",
                query = "DELETE from Workout W where W.user = :user"
        ),
        @NamedQuery(
                name = "User.deleteResults",
                query = "DELETE from Result R where R.user = :user"
        ),
        @NamedQuery(
                name = "User.getLoggedInUsers",
                query = "select new com.semafoor.semaforce.model.view.UserView(U.id, U.userName, U.info.email) from User U"
        ),
        @NamedQuery(
                name = "User.findLoggedInUserByUserName",
                query = "select new com.semafoor.semaforce.model.view.UserView(U.id, U.userName, U.info.email, " +
                        "W.id, W.referenceName, W.numWeeks, W.daysPerWeek, W.currentDay, W.createdDateTime) from User U " +
                        "join U.currentWorkout as W " +
                        "where U.userName = :username"
        ),
        @NamedQuery(
                name = "User.getCurrentWorkoutView",
                query = "select new com.semafoor.semaforce.model.view.WorkoutView(W.id, W.referenceName, W.numWeeks, " +
                        "W.daysPerWeek, W.currentDay, W.createdDateTime) from User U " +
                        "join U.currentWorkout as W " +
                        "where U.id = :id"
        ),
        @NamedQuery(
                name = "User.findLoggedInUserNoWorkoutByUserName",
                query = "select new com.semafoor.semaforce.model.view.UserView(U.id, U.userName, U.info.email) from User U "
                        + "where U.userName = :username"
        )}
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Username is required. Minimum is 2, maximum is 255 characters"
    )
    @Column(nullable = false, unique = true)
    private String userName;

    @NotNull
    @Size(
            min = 8,
            max = 255,
            message = "Password is required. Minimum is 8, maximum is 255 characters"
    )
    private String password;

    private AccountInfo info;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENT_WORKOUT")
    private Workout currentWorkout;

    public User() {
    }

    public User(String userName, String password, AccountInfo info) {
        this.userName = userName;
        this.password = password;
        this.info = info;
    }
}
