package br.jmonstro.bean.postman.collection.request;

import lombok.Data;

import java.util.List;

@Data
public class Body {
    private String mode;
    private String raw;
    private List<Param> formdata;
    private List<Param> urlencoded;
}
