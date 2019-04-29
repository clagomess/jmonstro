package br.jmonstro.service;

import br.jmonstro.bean.postman.Collection;
import br.jmonstro.bean.postman.Environment;
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
    private File[] listDir(String name) throws IOException {
        File dir = new File(name);

        if(!dir.isDirectory()){
            if(!dir.mkdir()){
                throw new IOException(String.format("Falha ao criar diretorio \"%s\"", name));
            }
        }

        return dir.listFiles();
    }

    private Collection readCollectionFile(File json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Collection.class);
    }

    private Environment readEnvironmentFile(File json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Environment.class);
    }

    private List<Collection> readCollectionFolder() throws IOException {
        List<Collection> toReturn = new LinkedList<>();

        for(File file : listDir("postman_collections")){
            toReturn.add(readCollectionFile(file));
        }

        return toReturn;
    }

    public List<Environment> readEnvironmentFolder() throws IOException {
        List<Environment> toReturn = new LinkedList<>();

        for(File file : listDir("postman_environments")){
            toReturn.add(readEnvironmentFile(file));
        }

        return toReturn;
    }

    public TreeItem<Item> getTree() throws IOException {
        TreeItem<Item> root = new TreeItem<>(new Item("Postman Collections"));

        for(Collection collection : readCollectionFolder()){
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
