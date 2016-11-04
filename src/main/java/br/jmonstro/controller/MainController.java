package br.jmonstro.controller;

import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends MainForm {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public void processarJsonAction(){
        String[] rootItems = new String[]{"a", "b", "c"};

        TreeItem<String> root = new TreeItem<String>("Root Node");
        root.setExpanded(true);
        for (String itemString: rootItems) {
            root.getChildren().add(new TreeItem<String>(itemString));
        }

        tree.setRoot(root);
    }
}
