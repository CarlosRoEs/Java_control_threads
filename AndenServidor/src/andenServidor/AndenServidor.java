/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andenServidor;

import almacen.AlmacenSemaforo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import thread.AndenThread;

/**
 *
 * @author Carlos
 */
public class AndenServidor {

    boolean ejecutar = false;
    private Socket socket;
    private ServerSocket servidor;
    private static final int PUERTO = 5555;

    int identificador;
    AlmacenSemaforo almacen;

    /**
     * Realiza la conexión del servidor.
     */
    public void iniciarServidor() {

        System.out.println("El servidor usa el puerto 5555.");
        System.out.println("Iniciando servidor...");

        try {

            servidor = new ServerSocket(PUERTO);
        } catch (IOException ex) {
            System.out.println("No se ha podido conectar con el socket. " + ex.getMessage());
        }
        while (!ejecutar) {

            System.out.println("Esperando cliente...");

            try {
                socket = servidor.accept();

//                Creamos los hilos para la entrada de los clientes.            
                AndenThread nuevoCliente = new AndenThread(socket, identificador, almacen);
                nuevoCliente.start();

            } catch (IOException ex) {
                System.out.println("Error en el puerto de entrada." + ex.getMessage());
            }
        }
    }

    public void cerrarServidor() throws IOException {
        servidor.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AndenServidor servidor = new AndenServidor();
        servidor.iniciarServidor();
    }

}
