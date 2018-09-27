package br.jmonstro.bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Data;

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

    @Data
    public static class KeyValueTable {
        private SimpleStringProperty key;
        private SimpleStringProperty value;
    }
}
