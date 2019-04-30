package br.jmonstro.service;

import br.jmonstro.bean.RestParam;
import br.jmonstro.bean.RestResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

@Slf4j
public class RestTest {
    @BeforeClass
    public static void startServer() {
        ClientAndServer.startClientAndServer(8888);
    }

    @Test
    public void get() throws Throwable {
        URL sample_01 = Thread.currentThread().getContextClassLoader().getResource("sample_01.json");
        File json = new File(sample_01.getPath());

        new MockServerClient("localhost", 8888)
                .when(HttpRequest.request().withMethod("GET").withPath("/sample_01.json"))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                                .withBody(new String(Files.readAllBytes(json.toPath())))
                );

        RestService.perform(new RestParam("http://127.0.0.1:8888/sample_01.json"));
    }

    @Test
    public void contentExtension() {
        Assert.assertEquals("html", RestService.contentExtension("text/html"));
        Assert.assertEquals("xml", RestService.contentExtension("text/xml"));
        Assert.assertEquals("json", RestService.contentExtension("application/json"));
        Assert.assertEquals("bin", RestService.contentExtension("text/plain"));
    }

    @Test
    public void post_form() throws Throwable {
        RestParam rp = new RestParam("https://postman-echo.com/post");
        rp.setMetodo(RestParam.Metodo.POST);
        rp.getFormData().add("param_123", "value_123");

        // FORM_URLENCODED
        rp.setBodyType(RestParam.BodyType.FORM_URLENCODED);
        RestResponseDto dto = RestService.perform(rp);
        String content = new String(Files.readAllBytes(dto.getFile().toPath()));

        Assert.assertTrue("not contains 'param_123'", content.contains("param_123"));
        Assert.assertTrue("not contains 'application/x-www-form-urlencoded'", content.contains("param_123"));

        // FORM_DATA
        rp.setBodyType(RestParam.BodyType.FORM_DATA);
        dto = RestService.perform(rp);
        content = new String(Files.readAllBytes(dto.getFile().toPath()));

        Assert.assertTrue("not contains 'param_123'", content.contains("param_123"));
        Assert.assertTrue("not contains 'multipart/form-data'", content.contains("multipart/form-data"));
    }
}
