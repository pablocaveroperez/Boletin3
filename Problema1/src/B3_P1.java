import java.util.concurrent.Semaphore;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Pablo Cavero Perez
 * @version 1.0
 */
public class B3_P1 {
    private final int NUM_COCHES = 3;
    /**
     * La clase Control nos sirve para poder acceder a variables desde cualquier punto de la ejecucion del programa.
     * Toda variable que necesite ser accedida desde multiples puntos de la ejecucion deberia estar dentro de esta clase.
     */
    public class Control {
        private Semaphore sCoche = new Semaphore(NUM_COCHES);
        private Semaphore sPasajeros = new Semaphore(0);
        private Queue<Pasajero> colaPasajero = new LinkedList<Pasajero>();

        public Semaphore getsCoche() {
            return sCoche;
        }

        public void setsCoche(Semaphore sCoche) {
            this.sCoche = sCoche;
        }

        public Semaphore getsPasajeros() {
            return sPasajeros;
        }

        public void setsPasajeros(Semaphore sPasajeros) {
            this.sPasajeros = sPasajeros;
        }

        public Queue<Pasajero> getColaPasajero() {
            return colaPasajero;
        }

        public void setColaPasajero(Queue<Pasajero> colaPasajero) {
            this.colaPasajero = colaPasajero;
        }
    }

    private Control control = new Control();

    public class Coche implements Runnable {
        private int iId = 0;

        public Coche(int iId) {
            this.iId = iId;
        }

        public void run() {

            while (true) {

                System.out.println("El coche " + iId + " esta listo");

                try {
                    control.sCoche.acquire();
                    control.sPasajeros.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int iPas = control.colaPasajero.poll().iId;

                System.out.println("El pasajero " + iPas + " se monta en el coche " + iId);

                System.out.println("El coche " + iId + " inicia el recorrido");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }

                System.out.println("El coche " + iId + " ha vuelto");

                System.out.println("El pasajero " + iPas + " se baja de la atracción");

                control.sCoche.release();
            }
        }

    }

    public class Pasajero implements Runnable {
        private int iId = 0;

        public Pasajero(int iId) {
            this.iId = iId;
        }

        public void run() {
            int iLlegada = (int) (Math.random() * 5) + 1;

            System.out.println("El pasajero " + iId + " llega a la cola de la atracción en " + iLlegada + " segundos");

            try {
                Thread.sleep(iLlegada * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            control.colaPasajero.add(this);

            control.sPasajeros.release();

        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int iPasajero = 0, i;

        for (i = 0; i < NUM_COCHES; i++) {
            new Thread(new Coche(i)).start();
        }

        while (true) {
            Thread.sleep(300);
            new Thread(new Pasajero(iPasajero)).start();
            iPasajero++;
        }

    }

    public static void main(String[] args) {
        try {
            B3_P1 b3P1 = new B3_P1();
            b3P1.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}