import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_EJ1 {
    private final int NUM_PLATOS = 5;

    public class Control {
        private Semaphore semaforoPlato = new Semaphore(NUM_PLATOS);
        private Semaphore semaforoCliente = new Semaphore(0);
        private Queue<Comensales> colaComensales = new LinkedList<Comensales>();

        public Semaphore getSemaforoPlato() {
            return semaforoPlato;
        }

        public void setSemaforoPlato(Semaphore semaforoPlato) {
            this.semaforoPlato = semaforoPlato;
        }

        public Semaphore getSemaforoCliente() {
            return semaforoCliente;
        }

        public void setSemaforoCliente(Semaphore semaforoCliente) {
            this.semaforoCliente = semaforoCliente;
        }

        public Queue<Comensales> getColaComensales() {
            return colaComensales;
        }

        public void setColaComensales(Queue<Comensales> colaComensales) {
            this.colaComensales = colaComensales;
        }
    }

    final Control control = new Control();

    public class Cocinero implements Runnable {
        private int iId;

        public Cocinero(int iId) {
            this.iId = iId;
        }

        @Override
        public synchronized void run() {

            while (true) {
                System.out.println("El plato " + iId + " está listo.");

                try {
                    control.semaforoPlato.acquire();
                    control.semaforoCliente.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int iCliente = Objects.requireNonNull(control.colaComensales.poll()).iId;

                System.out.println("El comensal " + iCliente + " ha cogido el plato " + iId);
                System.out.println("El plato " + iId + " está vacío");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("El cocinero ha repuesto el plato " + iId);

                control.semaforoPlato.release();
            }
        }
    }

    public class Comensales implements Runnable {

        private int iId = 0;

        public Comensales(int iId) {
            this.iId = iId;
        }

        @Override
        public void run() {
            System.out.println("El comensal " + iId + " acaba de llegar a la cola");

            control.colaComensales.add(this);

            control.semaforoCliente.release();
        }
    }

    /**
     * Este metodo es el que vamos a utilizar para todos los programas de MultiThreading.
     * Sirve para lanzar los hilos correcpondientes.
     */
    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;

        for (int i = 1; i <= NUM_PLATOS; i++) {
            new Thread(new Cocinero(i)).start();
        }

        while (true) {
            Thread.sleep(250);
            new Thread(new Comensales(iContador)).start();
            iContador++;
        }
    }

    public static void main(String[] args) {
        try {
            B3_EJ1 b3Ej1 = new B3_EJ1();
            b3Ej1.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
