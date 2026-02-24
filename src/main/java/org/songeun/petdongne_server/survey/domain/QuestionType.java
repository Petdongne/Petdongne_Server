package org.songeun.petdongne_server.survey.domain;

public enum QuestionType {
    WALKING_ENV("산책 환경"),
    SOUND_PROOF("방음 상태"),
    NEIGHBOR_REACTION("이웃 반응");

    private final String description;

    QuestionType(String description) {
        this.description = description;
    }
}
