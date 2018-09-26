package service;

import br.jmonstro.service.JMonstroService;
import javafx.scene.control.TreeItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;

@Slf4j
public class JMonstroTest {
    @Test
    public void sample01() throws Throwable {
        URL sample01 = Thread.currentThread().getContextClassLoader().getResource("sample_01.json");

        JMonstroService jms = new JMonstroService();
        TreeItem<String> root = jms.getTree(new File(sample01.getPath()));

        Assert.assertEquals("[15] : sample_01.json", root.getValue());
        Assert.assertEquals(15, root.getChildren().size());
        Assert.assertEquals("[0] : 0", root.getChildren().get(0).getValue());
        Assert.assertEquals("[1] : 999", root.getChildren().get(1).getValue());
        Assert.assertEquals("[2] : 999.99", root.getChildren().get(2).getValue());
        Assert.assertEquals("[3] : true", root.getChildren().get(3).getValue());
        Assert.assertEquals("[4] : false", root.getChildren().get(4).getValue());
        Assert.assertEquals("[5] : foo", root.getChildren().get(5).getValue());
        Assert.assertEquals("[6] : -99", root.getChildren().get(6).getValue());
        Assert.assertEquals("[7] : -999.99", root.getChildren().get(7).getValue());
        Assert.assertEquals("[8] : QcOnw6NvIEHDp3VjYXI=", root.getChildren().get(8).getValue());
        Assert.assertEquals(3, root.getChildren().get(9).getChildren().size());
        Assert.assertEquals("[2] : 3", root.getChildren().get(9).getChildren().get(2).getValue());
        Assert.assertEquals(2, root.getChildren().get(10).getChildren().size());
        Assert.assertEquals("b : 2", root.getChildren().get(10).getChildren().get(1).getValue());
        Assert.assertEquals("[11] : null", root.getChildren().get(11).getValue());
        Assert.assertEquals("[12] : ", root.getChildren().get(12).getValue());
        Assert.assertEquals("[13] :  ", root.getChildren().get(13).getValue());
        Assert.assertEquals("[14] : Ação Açucar", root.getChildren().get(14).getValue());
    }

    @Test
    public void sample02() throws Throwable {
        URL sample02 = Thread.currentThread().getContextClassLoader().getResource("sample_02.json");

        JMonstroService jms = new JMonstroService();
        TreeItem<String> root = jms.getTree(new File(sample02.getPath()));

        Assert.assertEquals("sample_02.json", root.getValue());
        Assert.assertEquals(15, root.getChildren().size());
        Assert.assertEquals("a : 0", root.getChildren().get(0).getValue());
        Assert.assertEquals("b : 999", root.getChildren().get(1).getValue());
        Assert.assertEquals("c : 999.99", root.getChildren().get(2).getValue());
        Assert.assertEquals("d : true", root.getChildren().get(3).getValue());
        Assert.assertEquals("e : false", root.getChildren().get(4).getValue());
        Assert.assertEquals("f : foo", root.getChildren().get(5).getValue());
        Assert.assertEquals("g : -99", root.getChildren().get(6).getValue());
        Assert.assertEquals("h : -999.99", root.getChildren().get(7).getValue());
        Assert.assertEquals("i : QcOnw6NvIEHDp3VjYXI=", root.getChildren().get(8).getValue());
        Assert.assertEquals(3, root.getChildren().get(9).getChildren().size());
        Assert.assertEquals("[2] : 3", root.getChildren().get(9).getChildren().get(2).getValue());
        Assert.assertEquals(2, root.getChildren().get(10).getChildren().size());
        Assert.assertEquals("b : 2", root.getChildren().get(10).getChildren().get(1).getValue());
        Assert.assertEquals("l : null", root.getChildren().get(11).getValue());
        Assert.assertEquals("m : ", root.getChildren().get(12).getValue());
        Assert.assertEquals("n :  ", root.getChildren().get(13).getValue());
        Assert.assertEquals("o : Ação Açucar", root.getChildren().get(14).getValue());
    }

    @Test
    public void sample03() throws Throwable {
        URL sample03 = Thread.currentThread().getContextClassLoader().getResource("sample_03.json");

        JMonstroService jms = new JMonstroService();
        TreeItem<String> root = jms.getTree(new File(sample03.getPath()));

        Assert.assertEquals("sample_03.json", root.getValue());
        Assert.assertEquals(22, root.getChildren().size());
        Assert.assertEquals(3, root.getChildren().get(19).getChildren().size());
        Assert.assertEquals(2, root.getChildren().get(19).getChildren().get(0).getChildren().size());
        Assert.assertEquals("id : 0", root.getChildren().get(19).getChildren().get(0).getChildren().get(0).getValue());
    }

    @Test
    public void sample04() throws Throwable {
        // Arquivo charset ANSI
        URL sample03 = Thread.currentThread().getContextClassLoader().getResource("sample_04.json");

        JMonstroService jms = new JMonstroService();
        TreeItem<String> root = jms.getTree(new File(sample03.getPath()));

        Assert.assertEquals(1, root.getChildren().size());
        Assert.assertEquals("a : Ação Açucar", root.getChildren().get(0).getValue());
    }
}
