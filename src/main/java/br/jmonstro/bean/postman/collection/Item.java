package br.jmonstro.bean.postman.collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"response", "event"})
public class Item {
    private String name = "item - name";
    private List<Item> item;
    private Request request;
}
