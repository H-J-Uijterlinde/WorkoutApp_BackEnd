package com.semafoor.semaforce.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semafoor.semaforce.model.dto.results.WeeklyResultDto;
import com.semafoor.semaforce.model.dto.workout.InstantTrainingWrapperDto;
import com.semafoor.semaforce.model.dto.workout.TrainingDayDto;
import com.semafoor.semaforce.services.GoalsService;
import com.semafoor.semaforce.services.TrainingDayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class TrainingDayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingDayService trainingDayService;

    @Mock
    private GoalsService goalsService;

    private TrainingDayController trainingDayController;

    private List<WeeklyResultDto> weeklyResultDtos = new ArrayList<>();
    private WeeklyResultDto weeklyResultDto1;
    private WeeklyResultDto weeklyResultDto2;
    private TrainingDayDto trainingDayDto;
    private InstantTrainingWrapperDto instantTrainingWrapperDto;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.trainingDayController = new TrainingDayController(this.trainingDayService, this.goalsService);
        this.initializeWeeklyResults();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.trainingDayController).build();
        initializeInstantTrainingWrapperDto();
    }

    private void initializeInstantTrainingWrapperDto() {
        this.trainingDayDto = new TrainingDayDto();
        this.instantTrainingWrapperDto = new InstantTrainingWrapperDto();
        this.instantTrainingWrapperDto.setTrainingDayDto(this.trainingDayDto);
        this.instantTrainingWrapperDto.setWeeklyResultDtos(this.weeklyResultDtos);
        this.instantTrainingWrapperDto.setUserId(1L);
    }

    private void initializeWeeklyResults() {
        this.weeklyResultDto1 = new WeeklyResultDto();

        this.weeklyResultDto2 = new WeeklyResultDto();

        this.weeklyResultDtos.add(weeklyResultDto1);
        this.weeklyResultDtos.add(weeklyResultDto2);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void addResults() throws Exception{

        System.out.println(this.mapToJson(this.weeklyResultDtos));

        this.mockMvc.perform(post("/training_days/1000/add_results/user=2000")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.mapToJson(this.weeklyResultDtos)))
                .andExpect(status().isOk());

        verify(this.goalsService).updateActiveGoals(2000L, this.weeklyResultDtos);
        verify(this.trainingDayService).addWeeklyResult(1000L, this.weeklyResultDtos);
    }

    @Test
    void addNewInstantTrainingDay() throws Exception {

        this.mockMvc.perform(post("/training_days/instant_training")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.mapToJson(this.instantTrainingWrapperDto)))
                .andExpect(status().isOk());

        verify(this.goalsService).updateActiveGoals(1L, this.weeklyResultDtos);
        verify(this.trainingDayService).addNewInstantTrainingDay(1L, this.trainingDayDto, this.weeklyResultDtos);
    }
}