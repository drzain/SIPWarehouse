package com.sip.warehouse;

public class DataQuestionGrading {

    String idQue;
    String part_name;
    String part_code;
    String amount;
    String percentase;

    public DataQuestionGrading(){

    }

    public DataQuestionGrading(String idQue, String part_name, String part_code, String amount, String percentase){

        this.idQue = idQue;
        this.part_name = part_name;
        this.part_code = part_code;
        this.amount = amount;
        this.percentase = percentase;

    }

    public String getIdQue() {
        return idQue;
    }

    public void setIdQue(String idQue) {
        this.idQue = idQue;
    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getPart_code() {
        return part_code;
    }

    public void setPart_code(String part_code) {
        this.part_code = part_code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPercentase() {
        return percentase;
    }

    public void setPercentase(String percentase) {
        this.percentase = percentase;
    }
}
