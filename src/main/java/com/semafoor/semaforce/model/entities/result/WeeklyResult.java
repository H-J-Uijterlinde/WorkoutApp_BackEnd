package com.semafoor.semaforce.model.entities.result;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semafoor.semaforce.model.entities.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

// todo: extend abstract entity. Database needs to be recreated after that.

/**
 * Defines the WeeklyResult entity.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@NamedQueries({
        @NamedQuery(
                name = "WeeklyResult.findByUserAndExerciseSortByDateAscending",
                query = "select W from WeeklyResult W " +
                        "join W.result as R join R.exercise as E join R.user as U " +
                        "where U.id = :userId and E.id = :exerciseId " +
                        "order by W.createdDateTime asc"
        )
})
public class WeeklyResult extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull(message = "Week number must be set")
    private int weekNumber;

    @NotNull(message = "Result where this weekly result belongs to, must be set")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Result result;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "SET_NUMBER")
    private Map<Integer, Score> numbersLifted = new HashMap<>();

    public WeeklyResult(){}

    public WeeklyResult(int weekNumber, Map<Integer, Score> numbersLifted) {
        this.weekNumber = weekNumber;
        this.numbersLifted = numbersLifted;
    }
}
