package br.jmonstro.bean.restparam;

import br.jmonstro.bean.RestForm;
import lombok.Data;

@Data
public class Proxy {
    private String uri;
    private String username;
    private String password;

    public Proxy(RestForm form){
        this.uri = form.txtProxyUrl.getText();
        this.username = form.txtProxyUsername.getText();
        this.password = form.txtProxyPassword.getText();
    }
}
