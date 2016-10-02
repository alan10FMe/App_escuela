package com.escuelapp.model;

import java.util.List;

/**
 * Created by alan on 10/4/16.
 */
public class Teachers extends BaseModel {

    private List<Teacher> teacherList;

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

}
