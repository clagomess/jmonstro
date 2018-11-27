package br.jmonstro.service;

import br.jmonstro.bean.RestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Map;

@Slf4j
public class RestService {
    public static File get(RestParam restParam) throws Throwable {
        Client client;

        if(restParam.getProxy() != null){
            ClientConfig config = new ClientConfig();
            config.connectorProvider(new ApacheConnectorProvider());
            config.property(ClientProperties.PROXY_URI, restParam.getProxy().getUri());
            config.property(ClientProperties.PROXY_USERNAME, restParam.getProxy().getUsername());
            config.property(ClientProperties.PROXY_PASSWORD, restParam.getProxy().getPassword());
            client = ClientBuilder.newClient(config);
        }else{
            client = ClientBuilder.newClient();
        }

        client.property(ClientProperties.CONNECT_TIMEOUT, 1000 * 10);

        WebTarget webTarget = client.target(restParam.getUrl());
        Invocation.Builder invocationBuilder = webTarget.request();
        invocationBuilder.headers(restParam.getHeader());

        if(!restParam.getCookie().isEmpty()){
            for (Map.Entry<String, String> item : restParam.getCookie().entrySet()) {
                invocationBuilder.cookie(item.getKey(), item.getValue());
            }
        }

        Response response;

        if(restParam.getMetodo() == RestParam.Metodo.POST){
            response = invocationBuilder.post(!StringUtils.isEmpty(restParam.getBody()) ? Entity.json(restParam.getBody()) : Entity.form(restParam.getFormData()));
        }else{
            response = invocationBuilder.get();
        }

        String responseContent = response.readEntity(String.class);

        return JMonstroService.writeFile(responseContent, contentExtension(response.getHeaderString("content-type")));
    }

    static String contentExtension(String contentType){
        if(contentType.contains("text/html")){
            return "html";
        }

        if(contentType.contains("text/xml")){
            return "xml";
        }

        if(contentType.contains("application/json")){
            return "json";
        }

        return "bin";
    }
}
