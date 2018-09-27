package br.jmonstro.bean;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

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

    public RestParam(RestForm form) throws Exception {
        this.metodo = Metodo.valueOf(form.cbxMetodo.getValue());

        if(StringUtils.isEmpty(form.txtUrl.getText())){
            throw new Exception("É necessário informar a URL de requisição");
        }

        this.url = form.txtUrl.getText();

        for(RestForm.KeyValueTable item : form.tblFormData.getItems()){
            if(!item.isEmpty()) {
                this.formData.add(item.getKey(), item.getValue());
            }
        }

        for(RestForm.KeyValueTable item : form.tblHeader.getItems()){
            if(!item.isEmpty()) {
                this.header.add(item.getKey(), item.getValue());
            }
        }

        for(RestForm.KeyValueTable item : form.tblCookie.getItems()){
            if(!item.isEmpty()) {
                this.cookie.put(item.getKey(), item.getValue());
            }
        }

        this.body = form.txtBodyJson.getText();

        if(form.chxProxy.isSelected()){
            if(StringUtils.isEmpty(form.txtProxyUrl.getText())){
                throw new Exception("É necessário informar a URL do Proxy");
            }

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
