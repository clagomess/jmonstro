package br.jmonstro.bean.postman.collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@JsonIgnoreProperties({"response", "event", "description"})
public class Item {
    private String name = "item - name";
    private List<Item> item;
    private Request request;

    public Item(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        if(request != null){
            return String.format("%s %s", request.getMethod(), name);
        }else{
            return name;
        }
    }
}
