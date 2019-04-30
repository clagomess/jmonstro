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
    private MultivaluedMap<String, Object> header = new MultivaluedHashMap<>();
    private Map<String, String> cookie = new HashMap<>();
    private Proxy proxy = null;

    // body
    private MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
    private String body = null;
    private BodyType bodyType = BodyType.NONE;

    public RestParam(String url){
        this.url = url;
    }

    public RestParam(RestForm form) throws Exception {
        this.metodo = Metodo.valueOf(form.cbxMetodo.getValue());

        if(StringUtils.isEmpty(form.txtUrl.getText())){
            throw new Exception("É necessário informar a URL de requisição");
        }

        this.url = injectVar(form, form.txtUrl.getText());

        for(RestForm.KeyValueTable item : form.tblFormData.getItems()){
            if(!item.isEmpty()) {
                this.formData.add(item.getKey(), injectVar(form, item.getValue()));
            }
        }

        for(RestForm.KeyValueTable item : form.tblHeader.getItems()){
            if(!item.isEmpty()) {
                this.header.add(item.getKey(), injectVar(form, item.getValue()));
            }
        }

        for(RestForm.KeyValueTable item : form.tblCookie.getItems()){
            if(!item.isEmpty()) {
                this.cookie.put(item.getKey(), injectVar(form, item.getValue()));
            }
        }

        this.body = injectVar(form, form.txtBodyJson.getText());

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

    private String injectVar(RestForm form, String input){
        if(StringUtils.isEmpty(input)){
            return input;
        }

        for(RestForm.KeyValueTable item : form.tblEnviroment.getItems()){
            input = input.replace(String.format("{{%s}}", item.getKey()), item.getValue());
        }

        for(RestForm.KeyValueTable item : form.tblGlobal.getItems()){
            input = input.replace(String.format("{{%s}}", item.getKey()), item.getValue());
        }

        return input;
    }

    public enum Metodo {
        POST("POST"), GET("GET"), PUT("PUT"), DELETE("DELETE");

        @Getter
        private final String value;

        Metodo(String value){
            this.value = value;
        }
    }

    public enum BodyType {
        NONE("NONE"),
        FORM_DATA("FORM_DATA"),
        FORM_URLENCODED("FORM_URLENCODED"),
        RAW("RAW"),
        BINARY("BINARY");

        @Getter
        private final String value;

        BodyType(String value){
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
