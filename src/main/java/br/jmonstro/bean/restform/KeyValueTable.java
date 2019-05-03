package br.jmonstro.bean.restform;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@NoArgsConstructor
public class KeyValueTable {
    private SimpleStringProperty key;
    private SimpleStringProperty value;

    public KeyValueTable(TextField key, TextField value){
        this.key = new SimpleStringProperty(key.getText());
        this.value = new SimpleStringProperty(value.getText());
    }

    public KeyValueTable(String key, String value){
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public boolean isEmpty(){
        return StringUtils.isEmpty(getKey()) || StringUtils.isEmpty(getValue());
    }

    public String getKey() {
        return key.get();
    }

    public SimpleStringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
