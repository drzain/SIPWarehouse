package com.sip.warehouse;

import java.util.ArrayList;

public class DataCategoryInformation {

    private ArrayList<DataCategory> categoryList = new ArrayList<>();

    public ArrayList<DataCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<DataCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
