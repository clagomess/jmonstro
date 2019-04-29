package br.jmonstro.bean.postman.collection.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"urlencoded"})
public class Body {
    private String mode;
    private String raw;
    private List<Param> formdata;
}
