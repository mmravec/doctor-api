package com.bunch.of.ideas.doctorapi.entity;

import java.util.List;

public class Text {
    private String value;
    private List<Object> annotations;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Object> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Object> annotations) {
        this.annotations = annotations;
    }
}
