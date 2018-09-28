package br.jmonstro.bean;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML
    protected TextField txtBusca;

    @FXML
    protected Label lblItensEncontrado;

    protected int posicaoBusca = 0;
}
