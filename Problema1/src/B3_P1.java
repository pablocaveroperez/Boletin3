
import java.util.concurrent.Semaphore;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class B3_P1 {

    public class Control {
        public final byte bNUM_COCHES = 3;
        public final int bNUM_PASAJEROS = 5, bTIEMPO_ATRACCION = 2;
        public Semaphore oSemaforoCochesLibres = new Semaphore(0);
        public Semaphore oSemaforoCochesEnUso = new Semaphore(0);
        public Semaphore oSemaforoCochesRestantes = new Semaphore(0);
        public Semaphore oSemaforoPasajerosRestantes = new Semaphore(0);
        public Queue<Passenger> colaPasajeros = new LinkedList<Passenger>();
        public Random r = new Random();
        public volatile int idCoche = 0, idPasajero = 0;

        public synchronized int getIdCoche() {
            return idCoche;
        }

        public synchronized void setIdCoche(int idCoche) {
            this.idCoche = idCoche;
        }

        public int getIdPasajero() {
            return idPasajero;
        }

        public void setIdPasajero(int idPasajero) {
            this.idPasajero = idPasajero;
        }

    }

    Control control = new Control();

    class Passenger implements Runnable {
        private int id = 0;

        public Passenger(int id) {
            this.id = id;
        }

        public void run() {
            int iTiempo = 1 + (int) control.r.nextInt(1000 * control.bNUM_PASAJEROS);
            try {
                Thread.sleep(iTiempo);
            } catch (InterruptedException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            control.setIdPasajero(id);
            System.err.println(" Visitante " + id + " ha llegado a la atraccion en " + iTiempo / 1000 + " segundos");
            control.colaPasajeros.add(this);

            try {
                control.oSemaforoCochesLibres.acquire();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            control.oSemaforoCochesEnUso.release();
            try {
                control.oSemaforoCochesRestantes.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                control.oSemaforoPasajerosRestantes.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println(" Visitante " + id + " acaba el trayecto");

        }
    }

    class Car implements Runnable {
        private int id = 0;

        public Car(int id) {
            this.id = id;
        }

        public void run() {

            while (true) {

                System.out.println(" Coche " + id + " esta listo");
                control.oSemaforoCochesLibres.release();
                try {
                    control.oSemaforoCochesEnUso.acquire();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(" Visitante " + control.colaPasajeros.poll().id + " se monta en el coche " + id);

                System.out.println(" Coche " + id + " haciendo el recorrido");
                control.oSemaforoCochesRestantes.release();
                try {
                    Thread.sleep(1 + (int) (1000 * control.bTIEMPO_ATRACCION));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(" Coche " + id + " ha vuelto");
                control.oSemaforoPasajerosRestantes.release();
            }
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int x = 0;
        boolean b = true;
        for (int i = 0; i < control.bNUM_COCHES; i++)
            new Thread(new Car(i)).start();
        while (b) {
            new Thread(new Passenger(x)).start();
            x++;
            Thread.sleep(100);
        }

    }

    public static void main(String[] args) {
        try {
            B3_P1 b3P1 = new B3_P1();
            b3P1.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}