package br.jmonstro.bean;

import br.jmonstro.bean.postman.Environment;
import br.jmonstro.bean.postman.collection.Item;
import br.jmonstro.bean.postman.collection.Request;
import br.jmonstro.bean.postman.collection.request.Param;
import br.jmonstro.bean.restform.KeyValueTable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Data;

@Data
public class RestForm {
    @FXML public SplitPane mainPane;
    @FXML public ChoiceBox<String> cbxMetodo;
    @FXML public TextField txtUrl;
    @FXML public Button btnExecutar;
    @FXML public TableView<KeyValueTable> tblFormData;
    @FXML public TextField tblFormDataKey;
    @FXML public TextField tblFormDataValue;
    @FXML public Button tblFormDataBtnAdd;
    @FXML public Button tblFormDataBtnRemove;
    @FXML public TableView<KeyValueTable> tblHeader;
    @FXML public TextField tblHeaderKey;
    @FXML public TextField tblHeaderValue;
    @FXML public TableView<KeyValueTable> tblCookie;
    @FXML public TextField tblCookieKey;
    @FXML public TextField tblCookieValue;
    @FXML public TextArea txtBody;
    @FXML public CheckBox chxProxy;
    @FXML public TextField txtProxyUrl;
    @FXML public TextField txtProxyUsername;
    @FXML public PasswordField txtProxyPassword;
    @FXML public ToggleGroup tipBodyType;
    @FXML public Tab tabBody;
    @FXML public GridPane grpFormDataBtn;

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
            txtBody.setText(request.getBody().getRaw());
        });
    }

    // POSTMAN PANE
    @FXML public TreeView<Item> postmanCollection;
    @FXML public ChoiceBox<Environment> cbxEnviroment;
    @FXML public TableView<KeyValueTable> tblEnviroment;
    @FXML public TableView<KeyValueTable> tblGlobal;
    @FXML public Text txtResponseMethod;
    @FXML public Text txtResponseUrl;
    @FXML public Text txtResponseTime;
    @FXML public Text txtResponseStatus;
    @FXML public Text txtResponseSize;
    @FXML public TableView<KeyValueTable> tblResponseHeader;
}
