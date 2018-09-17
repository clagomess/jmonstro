package service;

import br.jmonstro.service.HexViewerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class HexViewerTest {
    @Test
    public void getDimension() {
        log.info("{}", HexViewerService.getDimension(104576));
    }

    @Test
    public void parse(){
        Assert.assertEquals("arquivo.json", HexViewerService.parse("[4] : arquivo.json"));
        Assert.assertEquals("", HexViewerService.parse("[0] : "));
        Assert.assertEquals("arquivo.json", HexViewerService.parse("arquivo.json"));
        Assert.assertEquals("VEVTVEU=", HexViewerService.parse("[1] : VEVTVEU="));
        Assert.assertEquals("VEVTVEU=", HexViewerService.parse("desArquivoFoto : VEVTVEU="));
        Assert.assertEquals("true", HexViewerService.parse("status : true"));
        Assert.assertEquals("equivalenttome", HexViewerService.parse("[13] : equivalenttome"));
        Assert.assertEquals("null", HexViewerService.parse("dispositivos : null"));
        Assert.assertEquals(" ", HexViewerService.parse(null));
    }
}
