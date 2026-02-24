package org.songeun.petdongne_server.survey.domain;

public enum AnswerOption {

    GOOD("아주 좋아요"), AVERAGE("평범해요"), BAD("별로예요");

    public final String description;

    AnswerOption(String description) {
        this.description = description;
    }
}
