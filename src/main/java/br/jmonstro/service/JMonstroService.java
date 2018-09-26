package br.jmonstro.service;

import javafx.scene.control.TreeItem;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

public class JMonstroService {
    private static TreeItem<String> root;

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
