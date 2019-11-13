package com.semafoor.semaforce.model.entities.goals;

import com.semafoor.semaforce.model.entities.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@DiscriminatorValue("TW")
public class TotalWorkouts extends Goals {

    @NotNull(message = "The total number of required workouts for this goals must be set")
    private int totalWorkouts;

    @NotNull(message = "The end date for the total workouts goal must be set")
    private Date endDate;

    TotalWorkouts() {

    }

    public TotalWorkouts(User user, boolean active, int totalWorkouts, Date endDate,
                         int completionPercentage, String title, String subTitle) {
        this.user = user;
        this.active = active;
        this.totalWorkouts = totalWorkouts;
        this.endDate = endDate;
        this.completionPercentage = completionPercentage;
        this.title = title;
        this.subTitle = subTitle;
    }

    public void updateCompletionPercentage() {
        this.completionPercentage += (100 / this.totalWorkouts);
    }
}
