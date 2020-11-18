import java.util.*;
import java.util.concurrent.Semaphore;

public class B3_EJ6 {
    private final int PREMIUM = 3;
    public class Control {
        private Semaphore semaforoDespegues = new Semaphore(0);
        private Queue<DamAir> colaAviones = new LinkedList<DamAir>();
        private List<DamAir> aviones;

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

        public int hayNormalEnCola() {
            int bExito = -1;
            int iContador = 0;
            Object[] objects = colaAviones.toArray();
            aviones =  new ArrayList<DamAir>();
            for (Object object : objects) {
                aviones.add((DamAir) object);
            }

            while (iContador < aviones.size() && bExito == -1) {
                if (aviones.get(iContador).getiTipo() != PREMIUM)
                    bExito = iContador;
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
            Deque<DamAir> damAirDeque = (Deque<DamAir>) control.colaAviones;
            if (getiTipo() == PREMIUM) {
                System.out.println("El avion Premium " + getiId() + " ha llegado a la pista.");
                if (control.colaAviones.peek() != null && control.colaAviones.peek().getiTipo() == PREMIUM)
                    control.colaAviones.add(this);
                else
                    damAirDeque.offerFirst(this);
            }
            else {
                System.out.println("El avion Normal " + getiId() + " ha llegado a la pista.");
                control.colaAviones.add(this);
            }
            control.colaAviones = damAirDeque;
            control.semaforoDespegues.release();
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
                    DamAir oAvion = control.colaAviones.poll();

                    if (oAvion != null && oAvion.getiTipo() == PREMIUM)
                        System.out.println("El avion Premium " + oAvion.getiId() + " se dispone a depegar.");
                    else
                        System.out.println("El avion Normal " + oAvion.getiId() + " se dispone va a depegar.");

                    Thread.sleep(15000);

                    if (oAvion.getiTipo() == PREMIUM)
                        System.out.println("El avion Premium " + oAvion.getiId() + " ha terminado el despegue y ha dejado la pista libre.");
                    else
                        System.out.println("El avion Normal " + oAvion.getiId() + " ha terminado el despegue y ha dejado la pista libre.");

                    DamAir oSiguienteEnCola = null;
                    if (control.colaAviones.peek() != null) {
                        oSiguienteEnCola = control.colaAviones.peek();
                        if (oAvion.getiTipo() == PREMIUM && (oSiguienteEnCola.getiTipo() == PREMIUM) ) {
                            int iPosicion = control.hayNormalEnCola();
                            if (iPosicion != -1) {
                                Deque<DamAir> damAirDeque = (Deque<DamAir>) control.colaAviones;
                                damAirDeque.offerFirst(control.colaAviones.poll());
                                damAirDeque.remove(control.aviones.get(iPosicion));
                                damAirDeque.offerFirst(control.aviones.get(iPosicion));
                                control.colaAviones = damAirDeque;
                            }
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
