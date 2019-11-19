package com.semafoor.semaforce.controllers;

import com.semafoor.semaforce.model.dto.results.ChartRequest;
import com.semafoor.semaforce.model.dto.results.ChartResponse;
import com.semafoor.semaforce.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class responsible for exposing endpoints for receiving and sending http requests containing the Result
 * entity or its derivatives.
 */
@CrossOrigin
@Controller
@RequestMapping("/results")
public class ResultController {

    private ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping("/graphical")
    public ResponseEntity<ChartResponse> getChartData(@RequestBody ChartRequest requestData) {
        return ResponseEntity.ok(this.resultService.
                getChartData(requestData.getUserId(), requestData.getExerciseId(), requestData.getNumReps()));
    }
}
