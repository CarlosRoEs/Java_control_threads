
package almacen;

import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Carlos Rodríguez Escudero.
 * Esta clase va a ser el semaforo que sincronice los hilos.
 */
public class AlmacenSemaforo {

    Semaphore semaforo;
//    Cola con prioridad que almacena el identificador númerico de cada camión.
    PriorityQueue<Integer> listaCamiones;

    public AlmacenSemaforo() {
        semaforo = new Semaphore(1);
        listaCamiones = new PriorityQueue();
    }

    /**
     * Metódo que ejecuta el camión cuando intente llegar al almacen.
     * @param numeroCamion 
     */
    public void entraEnAnden(Integer numeroCamion) {
        try {
//            Solicita entrar.
            semaforo.acquire();
//            Entra en la lista de camiones.
            listaCamiones.add(numeroCamion);
//            Libera el recurso compartido, el semaforo.
            semaforo.release();
        } catch (InterruptedException ex) {
            System.out.println("No se ha podido interrumpir el hilo. " + ex.getMessage());
        }
    }

    public int saleDelAnden() {
        int camion = 0;
        try {
//            Sección critica.
//            Preguntamos si hay descargas pendientes
            if (isDescargasPendientes()) {
                semaforo.acquire();
//                Saca de la cola al primero que este situado en ella.
                camion = listaCamiones.poll();
                semaforo.release();
            }
        } catch (InterruptedException ex) {
            System.out.println("No se ha podido interrumpir el hilo. " + ex.getMessage());
        }
        return camion;
    }

    public boolean isDescargasPendientes() {
        return listaCamiones.size() > 0;
    }
}
