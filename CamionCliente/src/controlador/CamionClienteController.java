/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import almacen.AlmacenSemaforo;
import java.net.Socket;
import thread.CamionThread;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Carlos
 */
public class CamionClienteController implements Initializable {
    
     @FXML
    private ComboBox<String> cmbEnvio;

    @FXML
    private Button btnEnviar;

    @FXML
    private Label lblFruta;

    @FXML
    private TextField txtEnvioPeso;

    @FXML
    private TextArea txaDatosEnvio;

    @FXML
    private TextArea txaDatosRecepcion;
    
    private Socket socket;
    private int identificador;
    CamionThread enviarCamion;
    

    @FXML
    void handleEnvioCamion(ActionEvent event) {
        int camionRandom = (int)(Math.random() *30)+20;
        AlmacenSemaforo almacen = new AlmacenSemaforo();
        enviarCamion = new CamionThread(socket, identificador, txtEnvioPeso, cmbEnvio, txtEnvioPeso, txaDatosRecepcion, txaDatosEnvio, almacen, this);
        
    }

    @FXML
    void handleSoloNumeros(KeyEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> tipoFrutas = FXCollections.observableArrayList("Manzanas", "Peras", "Naranjas");
        cmbEnvio.setItems(tipoFrutas);

        seleccionarTipoFruta(cmbEnvio, tipoFrutas);

    }

    public void seleccionarTipoFruta(ComboBox fruta, ObservableList<String> listaFrutas) {

        fruta.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue observable, Object oldValue, Object newValue) -> {
                    lblFruta.setText((String) newValue);
                });
    }

    public String nombreFruta() {
        String tipoFruta;
        tipoFruta = lblFruta.getText();
        return tipoFruta;
    }

    public void limpiarCampos() {
//        lblFruta.setText("");
        txtEnvioPeso.setText("");
    }
    
}
