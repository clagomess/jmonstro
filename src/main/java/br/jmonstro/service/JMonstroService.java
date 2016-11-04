package br.jmonstro.service;

import javafx.scene.control.TreeItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JMonstroService {
    private static final Logger logger = LoggerFactory.getLogger(JMonstroService.class);
    private static TreeItem<String> root;

    private JSONObject loadResource(String src) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) parser.parse(new FileReader(src));
        } catch (IOException | ParseException e) {
            logger.warn(JMonstroService.class.getName(), e);
        }

        return jsonObject;
    }

    public TreeItem<String> getTree(String jsonName, String src){
        JSONObject jsonObject = loadResource(src);

        if(jsonObject != null) {
            root = getNode(jsonName, jsonObject);
            root.setExpanded(false);
        }

        return root;
    }

    private TreeItem<String> getNode(String nodeName, Object node){
        TreeItem<String> treeNode = new TreeItem<>(nodeName);

        if(node instanceof JSONObject){
            Map<String, Object> nodeMap = (Map<String, Object>) node;

            for (Map.Entry<String, Object> entry : nodeMap.entrySet()) {
                treeNode.getChildren().add(getNode(entry.getKey(), entry.getValue()));
            }
        }

        if(node instanceof JSONArray){
            JSONArray nodeArray = (JSONArray) node;
            Iterator<String> iterator = nodeArray.iterator();

            while (iterator.hasNext()) {
                treeNode.getChildren().add(new TreeItem<>(iterator.next()));
            }
        }

        if(node instanceof String){
            treeNode.getChildren().add(new TreeItem<>((String) node));
        }

        return treeNode;
    }
}
