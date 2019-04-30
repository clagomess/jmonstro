package br.jmonstro.bean.postman.collection.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"name"})
public class Param {
    private String key;
    private String value;
    private String type;
    private Boolean disabled;
    private String src;
}
