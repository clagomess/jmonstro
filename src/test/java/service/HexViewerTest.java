package service;

import br.jmonstro.service.HexViewerService;
import org.junit.Assert;
import org.junit.Test;

public class HexViewerTest {
    @Test
    public void print() {

    }

    @Test
    public void parse(){
        Assert.assertEquals("arquivo.json", new String(HexViewerService.parse("[4] : arquivo.json")));
        Assert.assertEquals("", new String(HexViewerService.parse("[0] : ")));
        Assert.assertEquals("arquivo.json", new String(HexViewerService.parse("arquivo.json")));
        Assert.assertEquals("VEVTVEU=", new String(HexViewerService.parse("[1] : VEVTVEU=")));
        Assert.assertEquals("VEVTVEU=", new String(HexViewerService.parse("desArquivoFoto : VEVTVEU=")));
        Assert.assertEquals("true", new String(HexViewerService.parse("status : true")));
        Assert.assertEquals("equivalenttome", new String(HexViewerService.parse("[13] : equivalenttome")));
        Assert.assertEquals("null", new String(HexViewerService.parse("dispositivos : null")));
        Assert.assertEquals(" ", new String(HexViewerService.parse(null)));
    }
}
