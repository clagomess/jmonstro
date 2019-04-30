package br.jmonstro.bean.restparam;

import lombok.Getter;

public enum Method {
    POST("POST"), GET("GET"), PUT("PUT"), DELETE("DELETE");

    @Getter
    private final String value;

    Method(String value){
        this.value = value;
    }
}
