package br.jmonstro.bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RestForm {
    @FXML
    protected ChoiceBox<String> cbxMetodo;

    @FXML
    protected TextField txtUrl;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblFormData;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblHeader;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblCookie;

    @FXML
    protected TextArea txtBodyJson;

    @FXML
    protected TextField txtProxyUrl;

    @FXML
    protected TextField txtProxyUsername;

    @FXML
    protected PasswordField txtProxyPassword;

    @NoArgsConstructor
    public static class KeyValueTable {
        private SimpleStringProperty key;
        private SimpleStringProperty value;

        public KeyValueTable(String key, String value){
            this.key = new SimpleStringProperty(key);
            this.value = new SimpleStringProperty(value);
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
}
