import java.util.Objects;
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

    public class Passenger implements Runnable {
        private int id = 0;

        public Passenger(int id) {
            this.id = id;
        }

        public void run() {
            int iTiempo = 1 + control.r.nextInt(1000 * control.bNUM_PASAJEROS);

            /*
            HACEMOS DORMIR A LOS HILOS SEGUN EL TIEMPO QUE TARDAN EN LLEGAR A LA ATRACCION
             */
            try {
                Thread.sleep(iTiempo);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }

            control.setIdPasajero(id);
            System.err.println("Pasajero: " + id + " ha llegado a la atraccion en " + iTiempo / 1000 + " segundos.");
            // LA SIGUIENTE LINEA AÑADE LOS PASAJEROS A LA COLA DE PASAJEROS POR ORDEN DE LLEGADA
            control.colaPasajeros.add(this);

            /*
            AQUI RESERVAMOS LOS COCHES LIBRES QUE HAYAN DISPONIBLES
             */
            try {
                control.oSemaforoCochesLibres.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            /*
            AQUI SE LIBERAN LOS COCHES QUE HAYA EN USO
             */
            control.oSemaforoCochesEnUso.release();

            /*
            AQUI RESERVAMOS LOS COCHES QUE ESTEN EN RECORRIENDO LA ATRACCION
             */
            try {
                control.oSemaforoCochesRestantes.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*
            AQUI RESERVAMOS LOS PASAJEROS QUE ESTEN EN RECORRIENDO LA ATRACCION
             */
            try {
                control.oSemaforoPasajerosRestantes.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Pasajero: " + id + " ha terminado el trayecto.");
        }
    }

    public class Car implements Runnable {
        private int id = 0;

        public Car(int id) {
            this.id = id;
        }

        public void run() {
            while (true) {
                System.out.println("Coche: " + id + " esta listo.");
                control.oSemaforoCochesLibres.release();
                try {
                    control.oSemaforoCochesEnUso.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Pasajero: " + Objects.requireNonNull(control.colaPasajeros.poll()).id + " se ha montado en el coche: " + id + ".");

                System.out.println("Coche: " + id + " comenzando el recorrido.");
                control.oSemaforoCochesRestantes.release();
                try {
                    Thread.sleep(1 + (int) (1000 * control.bTIEMPO_ATRACCION));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Coche: " + id + " ha vuelto.");
                control.oSemaforoPasajerosRestantes.release();
            }
        }
    }

    private void executeMultiThreading() {
        int iContador = 0;
        for (int i = 0; i < control.bNUM_COCHES; i++)
            new Thread(new Car(i)).start();
        while (true) {
            new Thread(new Passenger(iContador)).start();
            iContador++;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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