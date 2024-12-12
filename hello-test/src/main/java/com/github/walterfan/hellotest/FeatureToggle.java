package com.github.walterfan.hellotest;

import lombok.Data;

/**
 * Created by yafan on 20/4/2018.
 */
@Data
public class FeatureToggle {
    private String feature;
    private boolean toggle;

    public FeatureToggle(String feature, boolean flag) {
        this.feature = feature;
        this.toggle = flag;
    }
}
