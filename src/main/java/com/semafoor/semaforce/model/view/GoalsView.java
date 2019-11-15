package com.semafoor.semaforce.model.view;

import lombok.Data;

@Data
public class GoalsView {

    private Long id;
    private String title;
    private String subTitle;
    private int completionPercentage;

    public GoalsView(Long id, String title, String subTitle, int completionPercentage) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.completionPercentage = completionPercentage;
    }
}
