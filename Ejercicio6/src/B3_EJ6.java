import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_EJ6 {
    public class Control {
        private Semaphore semaforoNormales = new Semaphore(0);
        private Semaphore semaforoPremium = new Semaphore(0);
        private Queue<Thread> colaAviones = new LinkedList<Thread>();

        public Semaphore getSemaforoNormales() {
            return semaforoNormales;
        }

        public void setSemaforoNormales(Semaphore semaforoNormales) {
            this.semaforoNormales = semaforoNormales;
        }

        public Semaphore getSemaforoPremium() {
            return semaforoPremium;
        }

        public void setSemaforoPremium(Semaphore semaforoPremium) {
            this.semaforoPremium = semaforoPremium;
        }

        public Queue<Thread> getColaAviones() {
            return colaAviones;
        }

        public void setColaAviones(Queue<Thread> colaAviones) {
            this.colaAviones = colaAviones;
        }
    }

    private Control control = new Control();

    public class Ryanair implements Runnable {

        @Override
        public void run() {

        }
    }

    public class QatarAirways implements Runnable {

        @Override
        public void run() {

        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;

        new Thread(new Dentista()).start();

        while (true) {
            Thread.sleep(500);
            new Thread(new Paciente(iContador)).start();
            iContador++;
        }
    }

    public static void main(String[] args) {
        try {
            B3_EJ6 b3Ej6 = new B3_EJ6();
            b3Ej6.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
