package br.jmonstro.bean.postman.collection.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"digest"})
public class Auth {
    private String type;
    private List<Param> basic;
    private List<Param> bearer;
}
