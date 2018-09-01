/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import almacen.AlmacenSemaforo;
import datosalmacen.DatosAlmacen;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class AndenThread extends Thread {
    
    int identificador;
    AlmacenSemaforo almacen;
    int tiempoPuesto;
    
    private Socket socket;
    private int cantidadFruta;
    private String nombreFruta;
    private DatosAlmacen datos = new DatosAlmacen();

    private static int cantidadNaranjas = 5000;
    private static int cantidadManzanas = 5000;
    private static int cantidadPeras = 5000;

    public AndenThread() {
    }

    public AndenThread(Socket socket,int identificador, AlmacenSemaforo almacen) {
        this.socket = socket;
        this.identificador = identificador;
        this.almacen = almacen;
        this.tiempoPuesto = 0;
    }
    
    public void mensajes() {

        try {

            ObjectInputStream recibir = new ObjectInputStream(socket.getInputStream());

            System.out.println("#### RECIBIENDO CAMIÓN ####");
            try {

                datos = (DatosAlmacen) recibir.readObject();
                cantidadFruta = datos.getCantidad();
                nombreFruta = datos.getNombreFruta();

                System.out.println("R/" + nombreFruta + "/" + cantidadFruta);
                cantidadDeFrutasAlmacen(nombreFruta);

                enviarMensaje();
            } catch (ClassNotFoundException ex) {
                System.out.println("No se ha encontrado la clase. " + ex.getMessage());
            }

        } catch (IOException ex) {
            System.out.println("Error al crear los flujos. " + ex.getMessage());
        }
    }

    public void enviarMensaje() {

        try {
            ObjectOutputStream enviar = new ObjectOutputStream(socket.getOutputStream());
            envioFrutas(nombreFruta, enviar, datos);

        } catch (IOException ex) {
            System.out.println("No se ha podido crear el flujo. " + ex.getMessage());
        }
    }

    public void cantidadDeFrutasAlmacen(String fruta) {
        switch (fruta) {
            case "Manzanas":
                cantidadManzanas += cantidadFruta;
                System.out.println("En stock " + cantidadManzanas + " " + datos.getNombreFruta());
                break;
            case "Naranjas":
                cantidadNaranjas += cantidadFruta;
                System.out.println("En stock " + cantidadNaranjas + " " + datos.getNombreFruta());
                break;
            case "Peras":
                cantidadPeras += cantidadFruta;
                System.out.println("En stock " + cantidadPeras + " " + datos.getNombreFruta());
                break;
            default:
        }
    }

    public void envioFrutas(String fruta, ObjectOutputStream enviar, DatosAlmacen datos) throws IOException {
        switch (nombreFruta) {
            case "Manzanas":
                datos.setNombreFruta("Peras");
                datos.setCantidad(1000);
                enviar.writeObject(datos);
                System.out.println("E/" + datos.getNombreFruta() + "/" + datos.getCantidad());
                cantidadPeras -= cantidadFruta;
                System.out.println("En stock " + cantidadPeras + " " + datos.getNombreFruta());
                break;
            case "Naranjas":
                datos.setNombreFruta("Manzanas");
                datos.setCantidad(1000);
                enviar.writeObject(datos);
                System.out.println("E/" + datos.getNombreFruta() + "/" + datos.getCantidad());
                cantidadManzanas -= cantidadFruta;
                System.out.println("En stock " + cantidadManzanas + " " + datos.getNombreFruta());
                break;
            case "Peras":
                datos.setNombreFruta("Naranjas");
                datos.setCantidad(1000);
                enviar.writeObject(datos);
                System.out.println("E/" + datos.getNombreFruta() + "/" + datos.getCantidad());
                cantidadNaranjas -= cantidadFruta;
                System.out.println("En stock " + cantidadNaranjas + " " + datos.getNombreFruta());
                break;
        }
    }
    
    @Override
    public void run(){
        int retardo;
        int numeroCamion;
        while(almacen.isCamionesPendientes()){
            try {
                retardo = (int)(Math.random() * 90 + 10);
                tiempoPuesto += retardo;
                mensajes();
                numeroCamion = almacen.terminarCamion(retardo);
                sleep(retardo);
                System.out.println("El camión " + identificador + " ha sido descargado.");
            } catch (InterruptedException ex) {
                System.out.println("No se ha podido interrumpir el hilo. " + ex.getMessage());
            }
            System.out.println("Descarga terminada " + identificador + ", descargado en un tiempo de " + tiempoPuesto);
        }
    }
}
