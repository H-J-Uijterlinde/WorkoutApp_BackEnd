package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.entities.result.Result;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository responsible for CRUD database operations on the Result entity.
 * Queries for the function which are not defined through the CrudRepository interface are defined as named queries on
 * the corresponding class.
 */
public interface ResultRepository extends CrudRepository<Result, Long> {
}
