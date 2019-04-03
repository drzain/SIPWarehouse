package com.sip.warehouse;

public class DataDetailApprove {

    String asset_receive_id,part_id,value,notes,part_name;

    public DataDetailApprove(){

    }

    public DataDetailApprove(String asset_receive_id, String part_id, String value, String notes, String part_name){

        this.asset_receive_id = asset_receive_id;
        this.part_id = part_id;
        this.value = value;
        this.notes = notes;
        this.part_name = part_name;

    }

    public String getAsset_receive_id() {
        return asset_receive_id;
    }

    public void setAsset_receive_id(String asset_receive_id) {
        this.asset_receive_id = asset_receive_id;
    }

    public String getPart_id() {
        return part_id;
    }

    public void setPart_id(String part_id) {
        this.part_id = part_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }
}
