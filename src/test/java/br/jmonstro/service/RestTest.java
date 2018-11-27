package br.jmonstro.service;

import br.jmonstro.bean.RestParam;
import org.junit.Assert;
import org.junit.Test;

public class RestTest {
    @Test
    public void get() throws Throwable {
        RestService.get(new RestParam("http://127.0.0.1:8888/sample_01.json"));
    }

    @Test
    public void contentExtension() {
        Assert.assertEquals("html", RestService.contentExtension("text/html"));
        Assert.assertEquals("xml", RestService.contentExtension("text/xml"));
        Assert.assertEquals("json", RestService.contentExtension("application/json"));
        Assert.assertEquals("bin", RestService.contentExtension("text/plain"));
    }
}
