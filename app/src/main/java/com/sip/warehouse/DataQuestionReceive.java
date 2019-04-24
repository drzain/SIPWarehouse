package com.sip.warehouse;

public class DataQuestionReceive {

    String idQue;
    String part_name;
    String category_id;

    public DataQuestionReceive() {
    }

    public DataQuestionReceive(String idQue, String category_id, String part_name) {
        this.idQue = idQue;
        this.category_id = category_id;
        this.part_name = part_name;
    }

    public String getId() {
        return idQue;
    }

    public void setId(String idQue) {
        this.idQue = idQue;
    }

    public String getQuestion() {
        return category_id;
    }

    public void setQuestion(String category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return part_name;
    }

    public void setDescription(String part_name) {
        this.part_name = part_name;
    }

}
