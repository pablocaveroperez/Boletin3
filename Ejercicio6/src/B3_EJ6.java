import java.util.*;
import java.util.concurrent.Semaphore;

public class B3_EJ6 {
    private final int PREMIUM = 3;
    public class Control {
        private Semaphore semaforoDespegues = new Semaphore(0);
        private Semaphore semaforoAvion = new Semaphore(0);
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

        public boolean hayNormalEnCola() {
            boolean bExito = false;
            int iContador = 0;
            List<DamAir> aviones = new ArrayList<DamAir>();
            Object[] objects = colaAviones.toArray();

            for (Object object : objects) {
                aviones.add((DamAir) object);
            }

            while (iContador < aviones.size() && !bExito) {
                if (aviones.get(iContador).getiTipo() != PREMIUM)
                    bExito = true;
                iContador++;
            }

            return bExito;
        }
    }

    private Control control = new Control();

    /**
     * La variable iTipo que esta en la clase {@link #DamAir}, indica si el Avion es normal o si ees premium
     * Los aviones que sean normales seran todos aquellos que no sean el numero 10, el valor de esta variable se
     * genera aleatoriamente entre 1 y 10.
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
            if (getiTipo() == PREMIUM) {
                System.out.println("El avion Premium " + getiId() + " ha llegado a la pista.");
                Deque<DamAir> damAirDeque = (Deque<DamAir>) control.colaAviones;
                damAirDeque.offerFirst(this);
                control.colaAviones = damAirDeque;
            }
            else {
                System.out.println("El avion Normal " + getiId() + " ha llegado a la pista.");
                control.colaAviones.add(this);
            }
            try {

                control.semaforoDespegues.release();
                control.semaforoAvion.acquire();
                if (getiTipo() == PREMIUM)
                    System.out.println("El avion Premium " + getiId() + " ha terminado el despegue y ha dejado la pista libre.");
                else
                    System.out.println("El avion Normal " + getiId() + " ha terminado el despegue y ha dejado la pista libre.");

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
            System.out.println("El aeropuerto esta preparado para que los aviones empiezen a despegar");
            do {
                try {
                    control.semaforoDespegues.acquire();
                    DamAir iDespegue = control.colaAviones.poll();

                    assert iDespegue != null;
                    if (iDespegue.getiTipo() == PREMIUM)
                        System.out.println("El avion Premium " + iDespegue.getiId() + " se dispone a depegar.");
                    else
                        System.out.println("El avion Normal " + iDespegue.getiId() + " se dispone va a depegar.");

                    Thread.sleep(15000);
                    control.semaforoAvion.release();

                    DamAir iNextInQueue = null;
                    if (control.colaAviones.peek() != null) {
                        iNextInQueue = control.colaAviones.peek();
                    }

                    if (iDespegue.getiTipo() == PREMIUM && (iNextInQueue != null && iNextInQueue.getiTipo() == PREMIUM) ) {
                        if (control.hayNormalEnCola()) {
                            control.colaAviones.add(control.colaAviones.poll());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(true);
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;
        new Thread(new Aurelinex()).start();

        while (true) {
            Thread.sleep(5000);
            int i = (int) (1 + Math.random() * PREMIUM);
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
