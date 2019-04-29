package br.jmonstro.bean.postman.collection;

import br.jmonstro.bean.postman.collection.request.Body;
import br.jmonstro.bean.postman.collection.request.Param;
import br.jmonstro.bean.postman.collection.request.Url;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"auth", "description"})
public class Request {
    private String method;
    private List<Param> header;
    private Body body;
    private Url url;
}
