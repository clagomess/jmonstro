package br.jmonstro.bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
public class RestForm {
    @FXML
    protected ChoiceBox<String> cbxMetodo;

    @FXML
    protected TextField txtUrl;

    @FXML
    protected Button btnExecutar;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblFormData;

    @FXML
    protected TextField tblFormDataKey;

    @FXML
    protected TextField tblFormDataValue;

    @FXML
    protected Button tblFormDataBtnAdd;

    @FXML
    protected Button tblFormDataBtnRemove;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblHeader;

    @FXML
    protected TextField tblHeaderKey;

    @FXML
    protected TextField tblHeaderValue;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblCookie;

    @FXML
    protected TextField tblCookieKey;

    @FXML
    protected TextField tblCookieValue;

    @FXML
    protected TextArea txtBodyJson;

    @FXML
    protected CheckBox chxProxy;

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
}
