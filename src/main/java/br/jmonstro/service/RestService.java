package br.jmonstro.service;

import br.jmonstro.bean.RestParam;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.UUID;

@Slf4j
public class RestService {
    public static File get(String url, RestParam restParam) throws Throwable {
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

        WebTarget webTarget = client.target(url);
        Invocation.Builder invocationBuilder = webTarget.request();
        invocationBuilder.headers(restParam.getHeader());
        Response response;

        if(restParam.getMetodo() == RestParam.Metodo.POST){
            response = invocationBuilder.post(restParam.getBody() != null ? Entity.json(restParam.getBody()) : Entity.form(restParam.getFormData()));
        }else{
            response = invocationBuilder.get();
        }

        String jsonContent = response.readEntity(String.class);

        File file = new File(UUID.randomUUID().toString() + ".json");
        Writer bw = new BufferedWriter(new FileWriter(file));
        bw.write(jsonContent);
        bw.flush();
        bw.close();

        return file;
    }
}
