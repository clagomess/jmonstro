package br.jmonstro.service;

import br.jmonstro.bean.postman.Collection;
import br.jmonstro.bean.postman.collection.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TreeItem;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class PostmanService {
    private Collection readFile(File json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Collection.class);
    }

    private List<Collection> readFolder() throws IOException {
        List<Collection> toReturn = new LinkedList<>();
        File dir = new File("postman_collections");

        if(!dir.isDirectory()){
            if(!dir.mkdir()){
                throw new IOException("Falha ao criar diretorio \"postman_collections\"");
            }

            return toReturn;
        }

        for(File file : dir.listFiles()){
            toReturn.add(readFile(file));
        }

        return toReturn;
    }

    public TreeItem<Item> getTree() throws IOException {
        TreeItem<Item> root = new TreeItem<>(new Item("Postman Collections"));

        for(Collection collection : readFolder()){
            TreeItem<Item> collItem = new TreeItem<>(new Item(collection.getInfo().getName()));

            if(collection.getItem() != null){
                for (Item item : collection.getItem()){
                    collItem.getChildren().add(getNode(item));
                }
            }

            root.getChildren().add(collItem);
        }

        return root;
    }

    private TreeItem<Item> getNode(Item item){
        TreeItem<Item> treeNode = new TreeItem<>(item);

        if(item.getItem() != null){
            for(Item cItem : item.getItem()){
                treeNode.getChildren().add(getNode(cItem));
            }
        }

        return treeNode;
    }
}
