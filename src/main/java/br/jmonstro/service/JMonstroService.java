package br.jmonstro.service;

import javafx.scene.control.TreeItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

public class JMonstroService {
    private static TreeItem<String> root;

    public TreeItem<String> getTree(File file) throws Throwable {
        JSONParser parser = new JSONParser();
        Object jsonObject  = parser.parse(new FileReader(file));
        String jsonName = file.getName();

        if(jsonObject != null) {
            if(jsonObject instanceof JSONArray){
                jsonName = "[" + ((JSONArray) jsonObject).size() + "] : " + file.getName();
            }

            root = getNode(jsonName, jsonObject);
            root.setExpanded(true);
        }

        return root;
    }

    private TreeItem<String> getNode(String nodeName, Object node){
        TreeItem<String> treeNode = new TreeItem<>(nodeName);

        if(node instanceof JSONObject){
            Map<String, Object> nodeMap = (Map<String, Object>) node;

            for (Map.Entry<String, Object> entry : nodeMap.entrySet()) {
                String keyName = entry.getKey();

                if(entry.getValue() instanceof JSONArray){
                    keyName = "[" + ((JSONArray) entry.getValue()).size() + "] : " + keyName;
                }

                if(
                        entry.getValue() instanceof String ||
                        entry.getValue() instanceof Long ||
                        entry.getValue() instanceof Boolean ||
                        entry.getValue() instanceof Double
                ){
                    treeNode.getChildren().add(new TreeItem<>(String.format("%s : %s", keyName, entry.getValue())));
                }else if(entry.getValue() == null){
                    treeNode.getChildren().add(new TreeItem<>(String.format("%s : null", keyName)));
                } else{
                    treeNode.getChildren().add(getNode(keyName, entry.getValue()));
                }
            }
        }

        if(node instanceof JSONArray){
            JSONArray nodeArray = (JSONArray) node;
            Iterator<Object> iterator = nodeArray.iterator();

            int idx = 0;
            while (iterator.hasNext()) {
                Object item = iterator.next();

                if(item instanceof JSONObject || item instanceof JSONArray) {
                    treeNode.getChildren().add(getNode("[" + idx + "] : ", item));
                }else{
                    treeNode.getChildren().add(new TreeItem<>("[" + idx + "] : " + (item == null ? "null" : item.toString())));
                }

                idx++;
            }
        }

        return treeNode;
    }
}
