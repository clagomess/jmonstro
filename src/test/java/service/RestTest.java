package service;

import br.jmonstro.service.RestService;
import org.junit.Test;

public class RestTest {
    @Test
    public void get() throws Throwable {
        RestService.get("http://127.0.0.1:8888/sample_01.json");
    }
}
