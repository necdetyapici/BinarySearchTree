package com.foreks.feed;

import com.foreks.feed.model.Student;

public class TreeRequest {
    private Integer treeId;
    private Student student;
    private Student newStudent;
    private Integer value;
    private Integer newValue;
    private String  valueString;
    private String  newValueString;
    private String  strategy;
    private String  comparator;

    public String getComparator() {
        return this.comparator;
    }

    public void setComparator(final String comparator) {
        this.comparator = comparator;
    }

    public String getValueString() {
        return this.valueString;
    }

    public void setValueString(final String valueString) {
        this.valueString = valueString;
    }

    public String getNewValueString() {
        return this.newValueString;
    }

    public void setNewValueString(final String newValueString) {
        this.newValueString = newValueString;
    }

    public Integer getNewValue() {
        return this.newValue;
    }

    public void setNewValue(final Integer newValue) {
        this.newValue = newValue;
    }

    public String getStrategy() {
        return this.strategy;
    }

    public void setStrategy(final String strategy) {
        this.strategy = strategy;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    public Student getNewStudent() {
        return this.newStudent;
    }

    public void setNewStudent(final Student newStudent) {
        this.newStudent = newStudent;
    }

    public Integer getTreeId() {
        return this.treeId;
    }

    public void setTreeId(final Integer treeId) {
        this.treeId = treeId;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(final Student student) {
        this.student = student;
    }

}
