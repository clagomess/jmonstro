package br.jmonstro.bean;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import lombok.Data;

@Data
public class MainForm {
    @FXML
    protected TextField txtPathJson;

    @FXML
    protected TreeView<String> tree;

    @FXML
    protected TextArea txtValor;

    @FXML
    protected ProgressBar progress;
}
