package br.jmonstro.bean;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
public class RestParam {
    private String url = null;
    private Metodo metodo = Metodo.GET;
    private MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
    private MultivaluedMap<String, Object> header = new MultivaluedHashMap<>();
    private Map<String, String> cookie = new HashMap<>();
    private String body = null;
    private Proxy proxy = null;

    public RestParam(String url){
        this.url = url;
    }

    public RestParam(RestForm form){
        this.metodo = Metodo.valueOf(form.cbxMetodo.getValue());
        this.url = form.txtUrl.getText();

        for(RestForm.KeyValueTable item : form.tblFormData.getItems()){
            this.formData.add(item.getKey().getValue(), item.getValue().getValue());
        }

        for(RestForm.KeyValueTable item : form.tblHeader.getItems()){
            this.header.add(item.getKey().getValue(), item.getValue().getValue());
        }

        for(RestForm.KeyValueTable item : form.tblCookie.getItems()){
            this.cookie.put(item.getKey().getValue(), item.getValue().getValue());
        }

        this.body = form.txtBodyJson.getText();

        if(form.txtProxyUrl.getText() != null && !"".equals(form.txtProxyUrl.getText().trim())){
            this.proxy = new Proxy(
                    form.txtProxyUrl.getText(),
                    form.txtProxyUsername.getText(),
                    form.txtProxyPassword.getText()
            );
        }
    }

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

        public Proxy(String uri, String username, String password){
            this.uri = uri;
            this.username = username;
            this.password = password;
        }
    }
}
