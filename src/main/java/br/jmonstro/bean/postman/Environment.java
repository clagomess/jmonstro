package br.jmonstro.bean.postman;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"_postman_exported_at", "_postman_exported_using"})
public class Environment {
    private String id;
    private String name;
    private List<Value> values;

    @JsonProperty(value = "_postman_variable_scope")
    private String scope;

    @Data
    @JsonIgnoreProperties({"description", "type"})
    public static class Value {
        private String key;
        private String value;
        private Boolean enabled;
    }

    @Override
    public String toString() {
        return name;
    }
}
