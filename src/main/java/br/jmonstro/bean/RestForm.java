package br.jmonstro.bean;

import br.jmonstro.bean.postman.Environment;
import br.jmonstro.bean.postman.collection.Item;
import br.jmonstro.bean.postman.collection.Request;
import br.jmonstro.bean.postman.collection.request.Param;
import br.jmonstro.bean.restform.KeyValueTable;
import br.jmonstro.bean.restparam.Method;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Data;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Base64;

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
    @FXML public TextArea txtRaw;
    @FXML public CheckBox chxProxy;
    @FXML public TextField txtProxyUrl;
    @FXML public TextField txtProxyUsername;
    @FXML public PasswordField txtProxyPassword;
    @FXML public ToggleGroup tipBodyType = new ToggleGroup();
    @FXML public RadioButton rbBodyTypeNone;
    @FXML public RadioButton rbBodyTypeFormData;
    @FXML public RadioButton rbBodyTypeFormUrlencoded;
    @FXML public RadioButton rbBodyTypeRaw;
    @FXML public RadioButton rbBodyTypeBinary;
    @FXML public Tab tabBody;
    @FXML public GridPane grpFormDataBtn;
    @FXML public GridPane grpBinary;
    @FXML public ChoiceBox<MediaType> cbxBinaryContentType;
    @FXML public TextField txtBinaryPath;
    @FXML public ChoiceBox<MediaType> cbxRawContentType;

    public void setFormValue(Request request){
        Platform.runLater(() -> {
            this.cbxMetodo.setValue(request.getMethod());
            this.txtUrl.setText(request.getUrl().getRaw());

            // BODY MODE
            if(
                    Arrays.asList(Method.PUT.getValue(), Method.POST.getValue()).contains(request.getMethod()) &&
                    request.getBody() != null &&
                    request.getBody().getMode() != null
            ){
                switch (request.getBody().getMode()){
                    case "raw":
                        this.rbBodyTypeRaw.fire();
                        break;
                    case "urlencoded":
                        this.rbBodyTypeFormUrlencoded.fire();
                        break;
                    case "formdata":
                        this.rbBodyTypeFormData.fire();
                        break;
                    default:
                        this.rbBodyTypeNone.fire();
                }
            }else{
                this.rbBodyTypeNone.fire();
            }

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

            // BODY RAW
            txtRaw.setText(request.getBody().getRaw());

            // AUTH - BEARER
            if(request.getAuth() != null && "bearer".equals(request.getAuth().getType())){
                this.tblHeader.getItems().add(new KeyValueTable(
                   "Authorization",
                   "Bearer " + request.getAuth().getBearer().get(0).getValue()
                ));
            }

            // AUTH - BASIC
            if(request.getAuth() != null && "basic".equals(request.getAuth().getType())){
                String username = "";
                String password = "";

                for(Param param : request.getAuth().getBasic()){
                    if("username".equals(param.getKey())){
                        username = param.getValue();
                    }

                    if("password".equals(param.getKey())){
                        password = param.getValue();
                    }
                }

                this.tblHeader.getItems().add(new KeyValueTable(
                    "Authorization",
                    "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes())
                ));
            }
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
