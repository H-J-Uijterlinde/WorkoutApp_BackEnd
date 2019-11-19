package com.semafoor.semaforce.model.entities.goals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semafoor.semaforce.model.entities.AbstractEntity;
import com.semafoor.semaforce.model.entities.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "GOAL_TYPE")
@NamedQueries({
        @NamedQuery(
                name = "Goals.getAllGoalsViews",
                query = "select new com.semafoor.semaforce.model.view.GoalsView(G.id, G.title, G.subTitle, G.completionPercentage) " +
                        "from Goals G " +
                        "join G.user U " +
                        "where U.id = :userId and G.active = :isActive"
        )
})
public abstract class Goals extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    Long id;

    @NotNull(message = "The user for this goal must be set")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    User user;

    @Column(nullable = false)
    boolean active;

    @NotNull(message = "The completions percentage for this goal must be set")
    int completionPercentage;

    @NotNull(message = "The title for this goal must be set")
    String title;

    @NotNull(message = "The subtitle for this goal must be set")
    String subTitle;
}
