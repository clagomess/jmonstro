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
    public SplitPane mainPane;

    @FXML
    public ChoiceBox<String> cbxMetodo;

    @FXML
    public TextField txtUrl;

    @FXML
    public Button btnExecutar;

    @FXML
    public TableView<RestForm.KeyValueTable> tblFormData;

    @FXML
    public TextField tblFormDataKey;

    @FXML
    public TextField tblFormDataValue;

    @FXML
    public Button tblFormDataBtnAdd;

    @FXML
    public Button tblFormDataBtnRemove;

    @FXML
    public TableView<RestForm.KeyValueTable> tblHeader;

    @FXML
    public TextField tblHeaderKey;

    @FXML
    public TextField tblHeaderValue;

    @FXML
    public TableView<RestForm.KeyValueTable> tblCookie;

    @FXML
    public TextField tblCookieKey;

    @FXML
    public TextField tblCookieValue;

    @FXML
    public TextArea txtBodyJson;

    @FXML
    public CheckBox chxProxy;

    @FXML
    public TextField txtProxyUrl;

    @FXML
    public TextField txtProxyUsername;

    @FXML
    public PasswordField txtProxyPassword;

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
    public TreeView<Item> postmanCollection;

    @FXML
    public ChoiceBox<Environment> cbxEnviroment;

    @FXML
    public TableView<RestForm.KeyValueTable> tblEnviroment;

    @FXML
    public TableView<RestForm.KeyValueTable> tblGlobal;

    @FXML
    public Text txtResponseMethod;

    @FXML
    public Text txtResponseUrl;

    @FXML
    public Text txtResponseTime;

    @FXML
    public Text txtResponseStatus;

    @FXML
    public Text txtResponseSize;

    @FXML
    public TableView<RestForm.KeyValueTable> tblResponseHeader;
}
