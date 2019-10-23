package com.semafoor.semaforce.model.view;

import lombok.Data;

/**
 * Class that presents a summary of an Muscle entity to API consumers.
 * This view can be created using JPQL constructor queries.
 */
@Data
public class MuscleView {

    private Long id;
    private String name;
    private String scientificName;

    public MuscleView(Long id, String name, String scientificName) {
        this.id = id;
        this.name = name;
        this.scientificName = scientificName;
    }
}
