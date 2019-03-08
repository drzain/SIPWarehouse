package com.sip.warehouse;

public class DataQuestionReceive {

    String idQue;
    String question;
    String description;
    String type;

    public DataQuestionReceive() {
    }

    public DataQuestionReceive(String idQue, String question, String description, String type) {
        this.idQue = idQue;
        this.question = question;
        this.description = description;
        this.type = type;
    }

    public String getId() {
        return idQue;
    }

    public void setId(String idQue) {
        this.idQue = idQue;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
