package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service class serves as the bridge between the data access layer and the controller layer for the Result
 * entity. This class is where the business logic can be implemented.
 */
@Service
@Transactional
public class ResultService {

    private ResultRepository repository;

    @Autowired
    public ResultService(ResultRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Result> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public Result findById(Long id) {
        return repository.findById(id).get();
    }

    public Result save(Result result) {
        return repository.save(result);
    }

    public void delete(Long id) {
        Result result = repository.findById(id).get();
        repository.delete(result);
    }
}
