package com.semafoor.semaforce.model.dto.goals;

import com.semafoor.semaforce.model.entities.goals.TotalWorkouts;
import com.semafoor.semaforce.model.entities.user.User;
import lombok.Data;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Data
public class TotalWorkoutsDto {

    User user;
    int totalWorkouts;
    Date endDate;

    public TotalWorkoutsDto() {}

    public TotalWorkouts transform() {
        String title = "Total Workouts";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String subTitle = this.totalWorkouts + " workouts before " + sdf.format(this.endDate);

        return new TotalWorkouts(this.user, true, this.totalWorkouts,
                this.endDate, 0, title, subTitle);
    }
}
