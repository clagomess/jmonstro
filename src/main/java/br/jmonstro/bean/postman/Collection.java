package br.jmonstro.bean.postman;

import br.jmonstro.bean.postman.collection.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Collection {
    private Info info = new Info();
    private List<Item> item;

    @Data
    public static class Info {
        @JsonProperty(value = "_postman_id")
        private String id;
        private String name = "Collection";
        private String schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";
        private String description;
    }
}
