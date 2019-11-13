package com.semafoor.semaforce.model.view;

import lombok.Data;

@Data
public class GoalsView {

    private String title;
    private String subTitle;
    private int completionPercentage;

    public GoalsView(String title, String subTitle, int completionPercentage) {
        this.title = title;
        this.subTitle = subTitle;
        this.completionPercentage = completionPercentage;
    }
}
