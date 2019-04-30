package br.jmonstro.bean.restparam;

import lombok.Data;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;

@Data
public class Body {
    private BodyType type = BodyType.NONE;
    private String raw;
    private File binary;

    // Form
    private FormDataMultiPart formData = new FormDataMultiPart();
    private MultivaluedMap<String, String> formUrlencoded = new MultivaluedHashMap<>();
}
