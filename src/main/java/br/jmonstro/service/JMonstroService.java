package br.jmonstro.service;

import br.jmonstro.bean.MainForm;
import br.jmonstro.main.Ui;
import com.github.clagomess.charsetdetector.CharsetDetectorUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.XML;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class JMonstroService {
    private TreeItem<String> root;

    File parseXml(File xmlFile) throws Throwable {
        JSONObject xmlJSONObj = XML.toJSONObject(new String(Files.readAllBytes(xmlFile.toPath())));
        String json = xmlJSONObj.toString(4);
        return JMonstroService.writeFile(json, "json");
    }

    public void processar(File file, MainForm mainForm){
        if(file == null || !file.isFile()) {
            return;
        }

        Platform.runLater(() -> {
            mainForm.getProgress().setProgress(-1);
            mainForm.getTxtPathJson().setText(file.getAbsolutePath());
        });

        new Thread(() -> {
            try {
                JMonstroService jMonstroService = new JMonstroService();
                File nFile = file;

                if(file.getName().toLowerCase().contains(".xml")){
                    nFile = jMonstroService.parseXml(file);
                }

                if(!nFile.getName().toLowerCase().contains(".json")){
                    throw new Exception("O arquivo só pode ser um JSON ou um XML");
                }

                TreeItem<String> root = jMonstroService.getTree(nFile);

                Platform.runLater(() -> {
                    mainForm.getTree().setVisible(true);
                    mainForm.getWebView().setVisible(false);

                    mainForm.getTree().setShowRoot(false);
                    mainForm.getTree().setRoot(root);
                    mainForm.getTree().getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
                        if(nv != null) {
                            mainForm.getTxtValor().setText(HexViewerService.parse(nv.getValue()));
                        }
                    });

                    mainForm.getProgress().setProgress(0);
                });
            }catch (Throwable e){
                log.error(JMonstroService.class.getName(), e);

                Platform.runLater(() -> {
                    mainForm.getProgress().setProgress(0);

                    mainForm.getTree().setVisible(false);
                    mainForm.getWebView().setVisible(true);
                    mainForm.getWebView().getEngine().load("file://" + file.getAbsolutePath());
                });

                Ui.alert(Alert.AlertType.ERROR, e.toString());
            }
        }).start();
    }

    TreeItem<String> getTree(File file) throws Throwable {
        String jsonName = file.getName();

        Charset charset = CharsetDetectorUtil.detect(new FileInputStream(file));

        JsonParser parser = Json.createParser(new StringReader(new String(Files.readAllBytes(file.toPath()), charset)));

        if(parser.hasNext()) {
            parser.next();
            JsonValue jsonObject = parser.getValue();

            if(jsonObject instanceof JsonArray){
                jsonName = "[" + ((JsonArray) jsonObject).size() + "] : " + file.getName();
            }

            root = getNode(jsonName, jsonObject);
            root.setExpanded(true);
        }

        return root;
    }

    public List<TreeItem<String>> buscar(MainForm mainForm, TreeItem<String> node) {
        List<TreeItem<String>> busca = new ArrayList<>();

        if(node == null){
            return busca;
        }

        String txtValue = mainForm.getChkBuscaCaseSensitive().isSelected() ? node.getValue().trim() : node.getValue().toLowerCase().trim();
        String txtBusca = mainForm.getChkBuscaCaseSensitive().isSelected() ? mainForm.getTxtBusca().getText().trim() : mainForm.getTxtBusca().getText().toLowerCase().trim();

        if(mainForm.getChkBuscaRegex().isSelected()) {
            Pattern p = Pattern.compile(txtBusca, Pattern.DOTALL);
            Matcher m = p.matcher(txtValue);

            if (m.find()) {
                busca.add(node);
            }
        }else{
            if(txtValue.contains(txtBusca)){
                busca.add(node);
            }
        }

        if(!node.getChildren().isEmpty()){
            for(TreeItem<String> item: node.getChildren()){
                busca.addAll(buscar(mainForm, item));
            }
        }

        return busca;
    }

    private TreeItem<String> getNode(String nodeName, Object node){
        TreeItem<String> treeNode = new TreeItem<>(nodeName);

        if(node instanceof JsonObject){
            Map<String, Object> nodeMap = (Map<String, Object>) node;

            for (Map.Entry<String, Object> entry : nodeMap.entrySet()) {
                String keyName = entry.getKey();

                if(entry.getValue() instanceof JsonArray){
                    keyName = "[" + ((JsonArray) entry.getValue()).size() + "] : " + keyName;
                }

                if(entry.getValue() instanceof JsonObject || entry.getValue() instanceof JsonArray) {
                    treeNode.getChildren().add(getNode(keyName, entry.getValue()));
                }else if (entry.getValue() instanceof JsonString) {
                    treeNode.getChildren().add(new TreeItem<>(String.format("%s : %s", keyName, ((JsonString) entry.getValue()).getString())));
                }else {
                    treeNode.getChildren().add(new TreeItem<>(String.format("%s : %s", keyName, (entry.getValue() == null ? "null" : entry.getValue().toString()))));
                }
            }
        }

        if(node instanceof JsonArray){
            JsonArray nodeArray = (JsonArray) node;
            Iterator<JsonValue> iterator = nodeArray.iterator();

            int idx = 0;
            while (iterator.hasNext()) {
                Object item = iterator.next();

                if(item instanceof JsonObject || item instanceof JsonArray) {
                    treeNode.getChildren().add(getNode("[" + idx + "] : ", item));
                }else if (item instanceof JsonString) {
                    treeNode.getChildren().add(new TreeItem<>("[" + idx + "] : " + ((JsonString) item).getString()));
                }else {
                    treeNode.getChildren().add(new TreeItem<>("[" + idx + "] : " + (item == null ? "null" : item.toString())));
                }

                idx++;
            }
        }

        return treeNode;
    }

    public void treeExpanded(TreeItem<String> node, Boolean expanded) {
        if(node != null && !node.isLeaf()){
            node.setExpanded(expanded);

            for(TreeItem<String> item : node.getChildren()){
                treeExpanded(item, expanded);
            }
        }
    }

    static File writeFile(String fileContent, String fileExtension) throws IOException {
        // mkdir
        File dir = new File("temp");
        if(!dir.isDirectory()){
            dir.mkdir();
        }

        final String filename = String.format(
            "temp%s%s.%s",
            File.separator,
            UUID.randomUUID().toString(),
            fileExtension
        );

        File file = new File(filename);
        Writer bw = new BufferedWriter(new FileWriter(file));
        bw.write(fileContent);
        bw.flush();
        bw.close();

        return file;
    }
}
