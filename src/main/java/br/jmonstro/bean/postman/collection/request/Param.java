package br.jmonstro.bean.postman.collection.request;

import lombok.Data;

@Data
public class Param {
    private String key;
    private String value;
    private String type;
    private Boolean disabled;
    private String src;
}
