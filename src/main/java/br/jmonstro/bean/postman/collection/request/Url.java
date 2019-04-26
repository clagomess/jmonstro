package br.jmonstro.bean.postman.collection.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"host", "path", "query"})
public class Url {
    private String raw;
}
