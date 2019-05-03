package br.jmonstro.service;

import br.jmonstro.bean.RestResponseDto;
import br.jmonstro.bean.restparam.RestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContexts;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
public class RestService {
    public static RestResponseDto perform(RestParam restParam) throws Throwable {
        ClientConfig config = new ClientConfig();
        config.register(MultiPartFeature.class);

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
        long requestTime = System.currentTimeMillis();

        // request
        switch (restParam.getMethod()){
            case POST:
                response = invocationBuilder.post(buildEntity(restParam));
                break;
            case PUT:
                response = invocationBuilder.put(buildEntity(restParam));
                break;
            case DELETE:
                response = invocationBuilder.delete();
                break;
            case GET:
            default:
                response = invocationBuilder.get();
                break;
        }

        requestTime = System.currentTimeMillis() - requestTime;

        String responseContent = response.readEntity(String.class);

        // build dto
        RestResponseDto dto = new RestResponseDto();
        dto.setUrl(restParam.getUrl());
        dto.setMethod(restParam.getMethod().getValue());
        dto.setSize(responseContent.length());
        dto.setHeaders(response.getStringHeaders());
        dto.setStatus(response.getStatus());
        dto.setFile(JMonstroService.writeFile(responseContent, response.getMediaType()));
        dto.setTime(requestTime);

        return dto;
    }

    private static Entity buildEntity(RestParam restParam){
        Entity toReturn = Entity.text(null);

        switch (restParam.getBody().getType()){
            case FORM_URLENCODED:
                toReturn = Entity.form(restParam.getBody().getFormUrlencoded());
                break;
            case FORM_DATA:
                toReturn = Entity.entity(restParam.getBody().getFormData(), restParam.getBody().getFormData().getMediaType());
                break;
            case RAW:
                toReturn = Entity.entity(restParam.getBody().getRaw(), restParam.getBody().getRawContentType());
                break;
            case BINARY:
                toReturn = Entity.entity(restParam.getBody().getBinary(), restParam.getBody().getBinaryContentType());
                break;
        }

        return toReturn;
    }

    private static SSLContext sslContext() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return SSLContexts.custom().loadTrustMaterial(null, (TrustStrategy) (x509Certificates, authType) -> true).build();
    }
}
