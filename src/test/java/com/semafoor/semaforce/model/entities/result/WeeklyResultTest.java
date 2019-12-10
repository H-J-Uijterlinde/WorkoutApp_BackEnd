package com.semafoor.semaforce.model.entities.result;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class WeeklyResultTest {


    Score score1 = new Score(100, 10, 9);
    Score score2 = new Score(50, 6, 8);
    Map<Integer, Score> numbersLifted;

    @BeforeEach
    void setup() {
        numbersLifted = new HashMap<>();
        numbersLifted.put(1, score1);
        numbersLifted.put(2, score2);
    }

    @Test
    void testWeeklyResultConstructor() {

        WeeklyResult weeklyResult = new WeeklyResult(1, numbersLifted);
        assertThat(weeklyResult.getWeekNumber()).isEqualTo(1);
        assertThat(weeklyResult.getNumbersLifted()).hasSize(2).containsValues(score1, score2);
    }

    @Test
    void testGettersAndSetters() {
        WeeklyResult weeklyResult = new WeeklyResult();
        weeklyResult.setId(10000L);
        weeklyResult.setNumbersLifted(numbersLifted);
        weeklyResult.setWeekNumber(1);
        assertThat(weeklyResult.getId()).isEqualTo(10000L);
        assertThat(weeklyResult.getWeekNumber()).isEqualTo(1);
        assertThat(weeklyResult.getNumbersLifted()).hasSize(2).containsValues(score1, score2);
    }
}