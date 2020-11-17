import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_EJ6 {
    public class Control {
        private Semaphore semaforoDespegues = new Semaphore(0);
        private Queue<DamAir> colaAviones = new LinkedList<DamAir>();

        public Semaphore getSemaforoDespegues() {
            return semaforoDespegues;
        }

        public void setSemaforoDespegues(Semaphore semaforoDespegues) {
            this.semaforoDespegues = semaforoDespegues;
        }

        public Queue<DamAir> getColaAviones() {
            return colaAviones;
        }

        public void setColaAviones(Queue<DamAir> colaAviones) {
            this.colaAviones = colaAviones;
        }

        public boolean hayPremiunEnCola() {
            boolean bExito = false;
            int iContador = 0;
            DamAir[] aviones = (DamAir[]) colaAviones.toArray();

            while (iContador < aviones.length && !bExito) {
                if (aviones[iContador].getiTipo() == 3)
                    bExito = true;
                iContador++;
            }

            return bExito;
        }
    }

    private Control control = new Control();

    /**
     * La variable iTipo que esta en la clase {@link #DamAir}, indica si el Avion es normal o si ees premium
     * Los aviones que sean normales seran todos aquellos que no sean el numero 3, el valor de esta variable se
     * genera aleatoriamente entre 0 y 3.
     */
    public class DamAir implements Runnable {
        private int iTipo;
        private int iId;

        public DamAir (int iTipo, int iId) {
            setiTipo(iTipo);
            setiId(iId);
        }

        public int getiTipo() {
            return iTipo;
        }

        public void setiTipo(int iTipo) {
            this.iTipo = iTipo;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public synchronized void run() {
            if (getiTipo() == 3)
                System.out.println("El avion Premium " + getiId() + " ha llegado a la pista.");
            else
                System.out.println("El avion Normal " + getiId() + " ha llegado a la pista.");
            try {
                control.colaAviones.add(this);
                control.semaforoDespegues.acquire();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * la clase {@link #Aurelinex} es el aeropuerto, y se encarga de controlar los despegues de los aviones
     */
    public class Aurelinex implements Runnable {

        public Aurelinex() {

        }

        @Override
        public synchronized void run() {
            do {
                System.out.println("El aeropuerto esta preparado para que los aviones empiezen a despegar");
                control.semaforoDespegues.release();
                DamAir iDespegue = control.colaAviones.poll();
                Integer iNextInQueue = null;
                if (control.colaAviones.peek() != null) {
                    iNextInQueue = control.colaAviones.peek().getiTipo();
                }

                if (iDespegue.getiTipo() == 3 && !control.hayPremiunEnCola()) {

                }
            }while(true);
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;
        new Thread(new Aurelinex()).start();

        while (true) {
            Thread.sleep(5000);
            int i = (int) (Math.random() * 3);
            new Thread(new DamAir(i, iContador)).start();
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
