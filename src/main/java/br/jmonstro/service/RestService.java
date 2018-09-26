package br.jmonstro.service;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.UUID;

@Slf4j
public class RestService {
    public static File get(String url) throws Throwable {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Invocation.Builder invocationBuilder = webTarget.request();
        Response response = invocationBuilder.get();

        String jsonContent = response.readEntity(String.class);

        File file = new File(UUID.randomUUID().toString() + ".json");
        Writer bw = new BufferedWriter(new FileWriter(file));
        bw.write(jsonContent);
        bw.flush();
        bw.close();

        return file;
    }
}
