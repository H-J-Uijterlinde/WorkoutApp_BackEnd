package com.semafoor.semaforce.model.entities.result;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


public class ScoreTest {

    @ParameterizedTest
    @CsvSource({
            "100, 10, 9",
            "1000, 100, 10"
    })
    void scoreConstructorTest(int weightLifted, int repetitionsPerformed, int rpe) {

        Score score = new Score(weightLifted, repetitionsPerformed, rpe);

        assertEquals(weightLifted, score.getWeightLifted());
        assertEquals(repetitionsPerformed, score.getRepetitionsPerformed());
        assertEquals(rpe, score.getRpe());
    }

    @Test
    public void testGettersAndSetters() {
        Score score = new Score();
        score.setRepetitionsPerformed(10);
        score.setWeightLifted(100);
        score.setRpe(-9);
        assertEquals(100, score.getWeightLifted());
        assertEquals(10, score.getRepetitionsPerformed());
        assertEquals(-9, score.getRpe());
    }
}