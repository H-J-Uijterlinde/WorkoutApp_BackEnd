package com.semafoor.semaforce.model.entities.result;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semafoor.semaforce.model.entities.AbstractEntity;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the Result entity.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Result extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull(message = "The user for this result must be set")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @NotNull(message = "The exercise for this result must be set")
    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    @OneToMany(mappedBy = "result",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @MapKey(name = "weekNumber")
    private Map<Integer, WeeklyResult> weeklyResults = new HashMap<>();

    Result() {
    }

    public Result(User user, Exercise exercise, Map<Integer, WeeklyResult> weeklyResults) {
        this.user = user;
        this.exercise = exercise;
        this.weeklyResults = weeklyResults;
    }

    public void addResult(WeeklyResult weeklyResult) {
        weeklyResult.setResult(this);
        this.weeklyResults.put(weeklyResult.getWeekNumber(), weeklyResult);
    }
}
