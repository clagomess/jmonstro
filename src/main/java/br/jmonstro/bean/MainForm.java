package br.jmonstro.bean;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import lombok.Data;

@Data
public class MainForm {
    @FXML
    protected TextField txtPathJson;

    @FXML
    protected TreeView<String> tree;

    @FXML
    protected WebView webView;

    @FXML
    protected TextArea txtValor;

    @FXML
    protected ProgressBar progress;

    @FXML
    protected CheckBox chkBuscaRegex;

    @FXML
    protected CheckBox chkBuscaCaseSensitive;

    @FXML
    protected TextField txtBusca;

    @FXML
    protected Button btnBuscar;

    @FXML
    protected Button btnCollapse;

    @FXML
    protected Button btnBuscarPrev;

    @FXML
    protected Button btnBuscarNext;

    @FXML
    protected Label lblItensEncontrado;
}
