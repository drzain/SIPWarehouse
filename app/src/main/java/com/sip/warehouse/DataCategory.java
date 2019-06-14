package com.sip.warehouse;

import java.util.ArrayList;

public class DataCategory {

    String category;
    private ArrayList<DataQuestionGrading> questionArrayList;

    public DataCategory(){
    }

    public DataCategory(String category){
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<DataQuestionGrading> getQuestionList() {
        return questionArrayList;
    }

    public void setEventsArrayList(ArrayList<DataQuestionGrading> questionArrayList) {
        this.questionArrayList = questionArrayList;
    }
}
