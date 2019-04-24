package com.sip.warehouse;

public class DataNote {

    String id;
    String branch;
    String assetcode;
    String customer;
    String lisenceplate;
    String typekendaraan;

    public DataNote(String id, String branch, String assetcode, String customer, String lisenceplate, String typekendaraan)
    {
        this.id = id;
        this.branch = branch;
        this.assetcode = assetcode;
        this.customer = customer;
        this.lisenceplate = lisenceplate;
        this.typekendaraan = typekendaraan;
    }

    public String getId(){
        return id;
    }

    public String getBranch() {
        return branch;
    }


    public String getAssetcode() {
        return assetcode;
    }


    public String getCustomer() {
        return customer;
    }


    public String getLisenceplate() {
        return lisenceplate;
    }

    public String getTypekendaraan(){
        return typekendaraan;
    }



}
