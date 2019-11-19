package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.dto.results.ChartResponse;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.result.WeeklyResult;
import com.semafoor.semaforce.repositories.ResultRepository;
import com.semafoor.semaforce.repositories.WeeklyResultRepository;
import com.semafoor.semaforce.services.utilities.ResultUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This service class serves as the bridge between the data access layer and the controller layer for the Result
 * entity. This class is where the business logic can be implemented.
 */
@Service
@Transactional
public class ResultService {

    private ResultRepository resultRepository;
    private WeeklyResultRepository weeklyResultRepository;
    private ResultUtilities resultUtils;

    @Autowired
    public ResultService(ResultRepository repository, WeeklyResultRepository weeklyResultRepository, ResultUtilities resultUtils) {
        this.resultRepository = repository;
        this.weeklyResultRepository = weeklyResultRepository;
        this.resultUtils = resultUtils;
    }

    @Transactional(readOnly = true)
    public List<Result> findAll() {
        return Lists.newArrayList(resultRepository.findAll());
    }

    public Result save(Result result) {
        return resultRepository.save(result);
    }

    public void delete(Long id) {
        Result result = resultRepository.findById(id).get();
        resultRepository.delete(result);
    }

    public ChartResponse getChartData(Long userId, Long exerciseId, int numReps) {
        ChartResponse response = new ChartResponse();
        response.setDates(new ArrayList<>());
        response.setEstimatedWeight(new ArrayList<>());

        List<WeeklyResult> results = this.weeklyResultRepository.
                findByUserAndExerciseSortByDateAscending(userId, exerciseId);

        results.stream().filter(
                result -> (this.resultUtils.getAverageRepsPerformed(result) >= numReps - 2
                        && this.resultUtils.getAverageRepsPerformed(result) <= numReps + 2)
        ).forEach(
                result -> {
            response.addDataPoint(result.getCreatedDateTime(), this.resultUtils.getWeightForDesiredRepNumber(numReps, result));
        });

        return response;
    }
}
