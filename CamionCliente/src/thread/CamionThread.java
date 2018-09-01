package thread;


import almacen.AlmacenSemaforo;
import controlador.CamionClienteController;
import datosalmacen.DatosAlmacen;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Carlos
 */
public class CamionThread extends Thread {

    private Socket cliente;
    private String ip = "localhost";
    private final int PUERTO = 5555;

    private TextField txtCliente;
    private ComboBox cmbFruta;
    private TextField txtCantitdad;
    private TextArea txaRecepcion;
    private TextArea txaDatosEnvio;
    private int identificador;
    private int cantidadFruta;
    private String nombreFruta;

    DatosAlmacen datos = new DatosAlmacen();
    AlmacenSemaforo almacen = new AlmacenSemaforo();
    CamionClienteController controller;
    ObservableList<String> listaFrutas = FXCollections.observableArrayList();

    boolean finalizado = false;

    public CamionThread(Socket cliente, int identificador, TextField txtCliente, ComboBox cmbFruta, TextField txtCantitdad,
            TextArea txaRecepcion, TextArea txaDatosEnvio, AlmacenSemaforo almacen, CamionClienteController controller) {
        this.cliente = cliente;
        this.identificador = identificador;
        this.txtCliente = txtCliente;
        this.cmbFruta = cmbFruta;
        this.txtCantitdad = txtCantitdad;
        this.txaRecepcion = txaRecepcion;
        this.txaDatosEnvio = txaDatosEnvio;
        this.almacen = almacen;
        this.controller = controller;
    }

    public void enviarDatos() {

//        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try {

//            cliente = (SSLSocket)factory.createSocket(ip, PUERTO);
//            cliente.setEnabledCipherSuites(factory.getSupportedCipherSuites());
            cliente = new Socket(ip, PUERTO);
        } catch (IOException ex) {
            System.out.println("No se ha podido acceder al socket." + ex.getMessage());
        }

        System.out.println("#### ENVIANDO CAMION ####");

        try {
//            Creamos el flujo de salida.
            ObjectOutputStream enviar = new ObjectOutputStream(cliente.getOutputStream());
//            Almacenamos los datos a enviar.

            System.out.println(controller.nombreFruta());
            datos.setNombreFruta(controller.nombreFruta());
            datos.setCantidad(Integer.parseInt(txtCantitdad.getText()));
            System.out.println("Camión llegando a su destino.");
            System.out.println("E/" + datos.getNombreFruta() + "/" + datos.getCantidad());
            txaDatosEnvio.appendText("E/" + datos.getNombreFruta() + "/" + datos.getCantidad());

            enviar.writeObject(datos);
//            controller.limpiarCampos();
            System.out.println("Camión en destino.");

        } catch (IOException ex) {
            System.out.println("Error al crear el flujo de salida. " + ex.getMessage());
        }
    }

    public void recibirMensajes() {

        try {
            ObjectInputStream recibir = null;

            System.out.println(cliente.toString());
            System.out.println("Recibiendo camión.");
            recibir = new ObjectInputStream(cliente.getInputStream());

            try {
                datos = (DatosAlmacen) recibir.readObject();
                cantidadFruta = datos.getCantidad();
                nombreFruta = datos.getNombreFruta();
                System.out.println("R/" + cantidadFruta + "/" + nombreFruta);
                txaRecepcion.appendText("R/" + cantidadFruta + "/" + nombreFruta + ".\n");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CamionThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(CamionThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        enviarDatos();
        recibirMensajes();
        almacen.terminarCamion(identificador);
    }
}
