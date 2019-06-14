package com.sip.warehouse;

public class DataQuestionGrading2 {

    String idQue;
    String part_name;
    String part_code;
    String amount;
    String percentase;

    public DataQuestionGrading2(){

    }

    public DataQuestionGrading2(String idQue, String part_name, String part_code, String amount, String percentase){

        this.idQue = idQue;
        this.part_name = part_name;
        this.part_code = part_code;
        this.amount = amount;
        this.percentase = percentase;

    }

    public String getIdQue2() {
        return idQue;
    }

    public void setIdQue2(String idQue) {
        this.idQue = idQue;
    }

    public String getPart_name2() {
        return part_name;
    }

    public void setPart_name2(String part_name) {
        this.part_name = part_name;
    }

    public String getPart_code2() {
        return part_code;
    }

    public void setPart_code2(String part_code) {
        this.part_code = part_code;
    }

    public String getAmount2() {
        return amount;
    }

    public void setAmount2(String amount) {
        this.amount = amount;
    }

    public String getPercentase2() {
        return percentase;
    }

    public void setPercentase2(String percentase) {
        this.percentase = percentase;
    }

}
