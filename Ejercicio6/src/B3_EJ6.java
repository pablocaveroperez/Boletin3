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

    /**
     * La variable iTipo que esta en la clase {@link #DamAir(int)}, indica si el Avion es normal o si ees premium
     * Los aviones que sean normales seran todos aquellos que no sean el numero 3, el valor de esta variable se
     * genera aleatoriamente entre 0 y 3.
     */
    public class DamAir implements Runnable {
        private int iTipo;

        public DamAir (int iTipo) {
            setiTipo(iTipo);
        }

        public int getiTipo() {
            return iTipo;
        }

        public void setiTipo(int iTipo) {
            this.iTipo = iTipo;
        }

        @Override
        public void run() {

        }
    }

    public class Aurelinex implements Runnable {

        @Override
        public void run() {

        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;

        new Thread(new Aurelinex()).start();

        while (true) {
            Thread.sleep(500);
            new Thread(new DamAir(iContador)).start();
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
