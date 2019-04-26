package br.jmonstro.bean;

import br.jmonstro.bean.postman.Collection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.URL;

@Slf4j
public class PostmanTest {
    @Test
    public void collection() throws Throwable {
        URL json = Thread.currentThread().getContextClassLoader().getResource("sample.postman_collection.json");

        ObjectMapper mapper = new ObjectMapper();
        Collection collection = mapper.readValue(json, Collection.class);

        log.info("{}", collection);
    }
}
