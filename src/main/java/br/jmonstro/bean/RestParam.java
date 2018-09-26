package br.jmonstro.bean;

import lombok.Data;
import lombok.Getter;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

@Data
public class RestParam {
    private Metodo metodo = Metodo.GET;
    private MultivaluedMap<String, Object> header = new MultivaluedHashMap<>();
    private Map<String, String> cookie = new HashMap<>();
    private MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
    private String body = null;
    private Proxy proxy = null;


    public enum Metodo {
        POST("POST"), GET("GET");

        @Getter
        private final String value;

        Metodo(String value){
            this.value = value;
        }
    }

    @Data
    public static class Proxy {
        private String uri;
        private String username;
        private String password;
    }
}
