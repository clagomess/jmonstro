package br.jmonstro.service;

import br.jmonstro.bean.MainForm;
import br.jmonstro.main.Ui;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class JMonstroService {
    private TreeItem<String> root;

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
                TreeItem<String> root = jMonstroService.getTree(file);

                Platform.runLater(() -> {
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
                Platform.runLater(() -> mainForm.getProgress().setProgress(0));
                Ui.alert(Alert.AlertType.ERROR, e.toString());
            }
        }).start();
    }

    public TreeItem<String> getTree(File file) throws Throwable {
        String jsonName = file.getName();
        JsonParser parser = Json.createParser(new StringReader(new String(Files.readAllBytes(file.toPath()))));

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

        if(node.getValue().toLowerCase().contains(mainForm.getTxtBusca().getText().toLowerCase())){
            busca.add(node);
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
}
