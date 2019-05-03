package br.jmonstro.bean.restparam;

import br.jmonstro.bean.RestForm;
import br.jmonstro.bean.restform.KeyValueTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
public class RestParam {
    private String url = null;
    private Method method = Method.GET;
    private MultivaluedMap<String, Object> header = new MultivaluedHashMap<>();
    private Map<String, String> cookie = new HashMap<>();
    private Proxy proxy = null;
    private Body body = new Body();

    public RestParam(String url){
        this.url = url;
    }

    public RestParam(RestForm form) throws Exception {
        this.method = Method.valueOf(form.cbxMetodo.getValue());

        if(StringUtils.isEmpty(form.txtUrl.getText())){
            throw new Exception("É necessário informar a URL de requisição");
        }

        this.url = injectVar(form, form.txtUrl.getText());

        if(this.method == Method.POST || this.method == Method.PUT){
            this.body.setType(BodyType.valueOf((String) form.tipBodyType.getSelectedToggle().getUserData()));

            switch (this.body.getType()){
                case FORM_DATA:
                    for(KeyValueTable item : form.tblFormData.getItems()){
                        if(!item.isEmpty()) {
                            this.body.getFormData().field(item.getKey(), injectVar(form, item.getValue()));
                        }
                    }
                    break;
                case FORM_URLENCODED:
                    for(KeyValueTable item : form.tblFormData.getItems()){
                        if(!item.isEmpty()) {
                            this.body.getFormUrlencoded().add(item.getKey(), injectVar(form, item.getValue()));
                        }
                    }
                    break;
                case RAW:
                    this.body.setRaw(injectVar(form, form.txtBody.getText()));
                    break;
                case BINARY:
                    this.body.setBinaryContentType(form.cbxBinaryContentType.getValue());
                    this.body.setBinary(new File(form.txtBinaryPath.getText()));
                    break;
            }
        }

        for(KeyValueTable item : form.tblHeader.getItems()){
            if(!item.isEmpty()) {
                this.header.add(item.getKey(), injectVar(form, item.getValue()));
            }
        }

        for(KeyValueTable item : form.tblCookie.getItems()){
            if(!item.isEmpty()) {
                this.cookie.put(item.getKey(), injectVar(form, item.getValue()));
            }
        }

        if(form.chxProxy.isSelected()){
            if(StringUtils.isEmpty(form.txtProxyUrl.getText())){
                throw new Exception("É necessário informar a URL do Proxy");
            }

            this.proxy = new Proxy(form);
        }
    }

    private String injectVar(RestForm form, String input){
        if(StringUtils.isEmpty(input)){
            return input;
        }

        for(KeyValueTable item : form.tblEnviroment.getItems()){
            input = input.replace(String.format("{{%s}}", item.getKey()), item.getValue());
        }

        for(KeyValueTable item : form.tblGlobal.getItems()){
            input = input.replace(String.format("{{%s}}", item.getKey()), item.getValue());
        }

        return input;
    }
}
