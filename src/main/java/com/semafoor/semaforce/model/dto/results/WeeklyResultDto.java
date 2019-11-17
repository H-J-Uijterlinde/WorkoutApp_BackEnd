package com.semafoor.semaforce.model.dto.results;

import com.semafoor.semaforce.model.entities.result.Score;
import com.semafoor.semaforce.model.entities.result.WeeklyResult;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * Class used to receive JSON objects which can be transformed into a WeeklyResult entity.
 */
@Data
public class WeeklyResultDto {

    private int exerciseNumber;
    private int weekNumber;

    private List<Integer> weightsLifted;
    private List<Integer> repetitionsPerformed;
    private List<Integer> rpe;

    private Long exerciseId;

    /**
     * Method that can be called on a WeeklyResultDto object, to transform it into a WeeklyResult entity
     * @return  WeeklyResult entity
     */
    public WeeklyResult transform() {

        WeeklyResult weeklyResult = new WeeklyResult(this.weekNumber, new HashMap<>());

        // Make sure result list sizes are equal to protect data integrity
        if (this.weightsLifted.size() == this.repetitionsPerformed.size() &&
                this.repetitionsPerformed.size() == this.rpe.size()) {

            for (int i = 0; i < this.weightsLifted.size(); i++) {
                weeklyResult.getNumbersLifted().put(i + 1, new Score(this.weightsLifted.get(i),
                        this.repetitionsPerformed.get(i), this.rpe.get(i)));
            }

        } else {
            throw new RuntimeException("Result lists sizes do not match");
        }
        return weeklyResult;
    }
}
