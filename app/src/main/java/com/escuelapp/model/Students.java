package com.escuelapp.model;

import java.util.List;

/**
 * Created by alan on 10/4/16.
 */
public class Students extends BaseModel {

    List<Student> studentList;

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
