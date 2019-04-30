package br.jmonstro.bean.restparam;

import lombok.Getter;

public enum BodyType {
    NONE("NONE"),
    FORM_DATA("FORM_DATA"),
    FORM_URLENCODED("FORM_URLENCODED"),
    RAW("RAW"),
    BINARY("BINARY");

    @Getter
    private final String value;

    BodyType(String value){
        this.value = value;
    }
}
