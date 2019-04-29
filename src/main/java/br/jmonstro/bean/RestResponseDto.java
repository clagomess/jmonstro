package br.jmonstro.bean;

import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Map;

@Data
public class RestResponseDto {
    private File file;

    // HTTP
    private String url;
    private String method;
    private Long time;
    private Integer status;
    private Integer size;
    private Map<String, List<String>> headers;
}
