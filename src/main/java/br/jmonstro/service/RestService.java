package br.jmonstro.service;

import br.jmonstro.bean.RestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContexts;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
public class RestService {
    public static File get(RestParam restParam) throws Throwable {
        ClientConfig config = new ClientConfig();

        if(restParam.getProxy() != null){
            config.connectorProvider(new ApacheConnectorProvider());
            config.property(ClientProperties.PROXY_URI, restParam.getProxy().getUri());
            config.property(ClientProperties.PROXY_USERNAME, restParam.getProxy().getUsername());
            config.property(ClientProperties.PROXY_PASSWORD, restParam.getProxy().getPassword());
        }

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        clientBuilder.withConfig(config);
        clientBuilder.sslContext(sslContext());
        Client client = clientBuilder.build();

        client.property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE);
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
        if(contentType == null){
            return "bin";
        }

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

    private static SSLContext sslContext() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return SSLContexts.custom().loadTrustMaterial(null, (TrustStrategy) (x509Certificates, authType) -> true).build();
    }
}
