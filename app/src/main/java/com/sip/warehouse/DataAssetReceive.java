package com.sip.warehouse;

public class DataAssetReceive {

    String warehouse_order_id, agreement_no, customer_name, branch_name, asset_description, license_plate, manufacturing_year, asset_type;

    public DataAssetReceive(){

    }

    public DataAssetReceive( String warehouse_order_id, String agreement_no, String customer_name, String branch_name, String asset_description, String license_plate, String manufacturing_year, String asset_type){

        this.warehouse_order_id = warehouse_order_id;
        this.agreement_no = agreement_no;
        this.customer_name = customer_name;
        this.branch_name = branch_name;
        this.asset_description = asset_description;
        this.license_plate = license_plate;
        this.manufacturing_year = manufacturing_year;
        this.asset_type = asset_type;

    }

    public String getWarehouse_order_id() {
        return warehouse_order_id;
    }

    public void setWarehouse_order_id(String warehouse_order_id) {
        this.warehouse_order_id = warehouse_order_id;
    }

    public String getAgreement_no() {
        return agreement_no;
    }

    public void setAgreement_no(String agreement_no) {
        this.agreement_no = agreement_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getAsset_description() {
        return asset_description;
    }

    public void setAsset_description(String asset_description) {
        this.asset_description = asset_description;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getManufacturing_year() {
        return manufacturing_year;
    }

    public void setManufacturing_year(String manufacturing_year) {
        this.manufacturing_year = manufacturing_year;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }
}
