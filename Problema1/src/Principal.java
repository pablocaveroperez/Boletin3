
import java.util.concurrent.Semaphore;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Principal {

    public class Control {
        public int coches = 3;
        public int tiempoPasajero = 5, tiempoAtraccion = 2;
        public Semaphore cocheLibre = new Semaphore(0);
        public Semaphore cochesUsandose = new Semaphore(0);
        public Semaphore cochesRestantes = new Semaphore(0);
        public Semaphore pasajerosRestantes = new Semaphore(0);
        public Queue<Passenger> colaPasajero = new LinkedList<Passenger>();
        public Random random = new Random();
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
            int iTiempo = 1 + (int) control.random.nextInt(1000 * control.tiempoPasajero);
            try {
                Thread.sleep(iTiempo);
            } catch (InterruptedException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            control.setIdPasajero(id);
            System.err.println(" Visitante " + id + " ha llegado a la atraccion en " + iTiempo / 1000 + " segundos");
            control.colaPasajero.add(this);

            try {
                control.cocheLibre.acquire();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            control.cochesUsandose.release();
            try {
                control.cochesRestantes.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                control.pasajerosRestantes.acquire();
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
                control.cocheLibre.release();
                try {
                    control.cochesUsandose.acquire();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(" Visitante " + control.colaPasajero.poll().id + " se monta en el coche " + id);

                System.out.println(" Coche " + id + " haciendo el recorrido");
                control.cochesRestantes.release();
                try {
                    Thread.sleep(1 + (int) (1000 * control.tiempoAtraccion));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(" Coche " + id + " ha vuelto");
                control.pasajerosRestantes.release();
            }
        }

    }

    public static void main(String[] args) {

        try {
            Principal principal = new Principal();
            principal.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void executeMultiThreading() throws InterruptedException {
        int x = 0;
        boolean b = true;
        for (int i = 0; i < control.coches; i++)
            new Thread(new Car(i)).start();
        while (b) {
            new Thread(new Passenger(x)).start();
            x++;
            Thread.sleep(100);
        }

    }

}