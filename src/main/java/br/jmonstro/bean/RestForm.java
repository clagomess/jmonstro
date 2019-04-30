package br.jmonstro.bean;

import br.jmonstro.bean.postman.Environment;
import br.jmonstro.bean.postman.collection.Item;
import br.jmonstro.bean.postman.collection.Request;
import br.jmonstro.bean.postman.collection.request.Param;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
public class RestForm {
    @FXML
    protected SplitPane mainPane;

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

    public void setFormValue(Request request){
        Platform.runLater(() -> {
            this.cbxMetodo.setValue(request.getMethod());
            this.txtUrl.setText(request.getUrl().getRaw());

            // FORM DATA
            tblFormData.getItems().remove(0, tblFormData.getItems().size());
            if(request.getBody().getFormdata() != null) {
                for (Param param : request.getBody().getFormdata()) {
                    this.tblFormData.getItems().add(new KeyValueTable(param.getKey(), param.getValue()));
                }
            }

            // HEADER
            tblHeader.getItems().remove(0, tblHeader.getItems().size());
            if(request.getHeader() != null) {
                for (Param param : request.getHeader()) {
                    this.tblHeader.getItems().add(new KeyValueTable(param.getKey(), param.getValue()));
                }
            }

            // BODY
            txtBodyJson.setText(request.getBody().getRaw());
        });
    }

    // POSTMAN PANE
    @FXML
    protected TreeView<Item> postmanCollection;

    @FXML
    protected ChoiceBox<Environment> cbxEnviroment;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblEnviroment;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblGlobal;

    @FXML
    protected Text txtResponseMethod;

    @FXML
    protected Text txtResponseUrl;

    @FXML
    protected Text txtResponseTime;

    @FXML
    protected Text txtResponseStatus;

    @FXML
    protected Text txtResponseSize;

    @FXML
    protected TableView<RestForm.KeyValueTable> tblResponseHeader;
}
