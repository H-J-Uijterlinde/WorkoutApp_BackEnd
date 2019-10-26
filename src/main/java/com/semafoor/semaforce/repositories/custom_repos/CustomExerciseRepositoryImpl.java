package com.semafoor.semaforce.repositories.custom_repos;

import com.semafoor.semaforce.model.entities.exercise.Category;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.exercise.Muscle;
import com.semafoor.semaforce.model.view.ExerciseView;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the implementation of the methods defined in the CustomExerciseRepository interface. This construction
 * allows the addition of methods to the ExerciseRepository interface.
 */
@Service
public class CustomExerciseRepositoryImpl implements CustomExerciseRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Method that dynamically creates a query based on the arguments provided. This method makes use of the JPA criteria
     * query API. Criteria are added if the argument provided for a parameter has a value that is not null. The resulting
     * query is then executed.
     *
     * @param name name or part of the exercise name to be used in a where clause.
     * @param category category enum value to be used in the where clause.
     * @param muscleId id of a Muscle entity to be used in the where clause.
     *
     * @return list of ExerciseView objects.
     */
    @Override
    public List<ExerciseView> findExercisesByCriteriaQuery(String name, Category category, Long muscleId) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExerciseView> c = cb.createQuery(ExerciseView.class);
        Root<Exercise> exerciseRoot = c.from(Exercise.class);
        Join<Exercise, Muscle> muscle = exerciseRoot.join("primaryMuscleTrained", JoinType.LEFT);
        c.multiselect(exerciseRoot.get("id"), exerciseRoot.get("name"), muscle.get("name"), exerciseRoot.get("category"));

        // Each where clause is represented by a predicate. A list of predicates is created based on the arguments provided.
        List<Predicate> criteria = new ArrayList<>();
        if (name != null) {
            ParameterExpression<String> p = cb.parameter(String.class, "name");
            criteria.add(cb.like(exerciseRoot.<String>get("name"), p));
        }

        if (category != null) {
            ParameterExpression<Category> p = cb.parameter(Category.class, "category");
            criteria.add(cb.equal(exerciseRoot.<Category>get("category"), p));
        }

        if (muscleId != null) {
            ParameterExpression<Long> p = cb.parameter(Long.class, "muscleId");
            criteria.add(cb.equal(muscle.<Long>get("id"), p));
        }

        // this method should not be used if no criteria are provided.
        if (criteria.size() == 0) {
            throw new RuntimeException("No criteria!");
        }else if (criteria.size() == 1) {
            c.where(criteria.get(0));
        }else {
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }

        // for every criteria the parameter value must be set.
        TypedQuery<ExerciseView> q = em.createQuery(c);
        if (name != null) q.setParameter("name", "%" + name + "%");
        if (category != null) q.setParameter("category", category);
        if (muscleId != null) q.setParameter("muscleId", muscleId);
        return q.getResultList();
    }
}
