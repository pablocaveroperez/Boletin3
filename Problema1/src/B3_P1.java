import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class B3_P1 {
    public static class Control {
        private static final byte bNUM_PASAJEROS = 10;
        private static final byte bNUM_COCHES = 2;
        private static Semaphore oSemaforoCoches;
        private static Semaphore oSemaforoPasajeros;
        private static ArrayList<HiloPasajero> listaPasajeros = new ArrayList<HiloPasajero>();
        private static ArrayList<HiloCoche> listaCoches = new ArrayList<HiloCoche>();
        private volatile static boolean bEstaLibre = true;
        private Queue<HiloPasajero> colaPasajeros = new LinkedList<HiloPasajero>();

        public Queue<HiloPasajero> getColaPasajeros() {
            return colaPasajeros;
        }

        public void setColaPasajeros(Queue<HiloPasajero> colaPasajeros) {
            this.colaPasajeros = colaPasajeros;
        }

        public static boolean isbEstaLibre() {
            return bEstaLibre;
        }

        public static void setbEstaLibre(boolean bEstaLibre) {
            Control.bEstaLibre = bEstaLibre;
        }

        public ArrayList<HiloCoche> getListaCoches() {
            return listaCoches;
        }

        public ArrayList<HiloPasajero> getListaPasajeros() {
            return listaPasajeros;
        }

        public void setListaCoches(ArrayList<HiloCoche> listaCoches) {
            Control.listaCoches = listaCoches;
        }

        public void setListaPasajeros(ArrayList<HiloPasajero> listaPasajeros) {
            Control.listaPasajeros = listaPasajeros;
        }

        public Semaphore getoSemaforoCoches() {
            return oSemaforoCoches;
        }

        public void setoSemaforoCoches(Semaphore oSemaforoCoches) {
            Control.oSemaforoCoches = oSemaforoCoches;
        }

        public Semaphore getoSemaforoPasajeros() {
            return oSemaforoPasajeros;
        }

        public void setoSemaforoPasajeros(Semaphore oSemaforoPasajeros) {
            Control.oSemaforoPasajeros = oSemaforoPasajeros;
        }

        public byte getbNUM_PASAJEROS() {
            return bNUM_PASAJEROS;
        }

        public byte getbNUM_COCHES() {
            return bNUM_COCHES;
        }

        public byte rand() {
            Random r = new Random();
            return (byte) (1 + r.nextInt(5 - 1 + 1));
        }

        public static boolean hayCochesLibres() {
            boolean bResultado = false;
            byte bContador = 0;

            while (!bResultado && bContador < bNUM_COCHES) {
                if (isbEstaLibre())
                    bResultado = true;
                else
                    bContador++;
            }
            return bResultado;
        }

        public static void ocuparCoche() {
            boolean bResultado = false;
            byte bContador = 0;

            while (!bResultado && bContador < bNUM_COCHES) {
                if (isbEstaLibre())
                    bResultado = true;
                else
                    bContador++;
            }

            if (bResultado)
                setbEstaLibre(bResultado);
        }
    }

    final Control control = new Control();

    public class HiloCoche implements Runnable {
        private byte bId;

        public HiloCoche(byte bId) {
            setbId(bId);
        }

        public byte getbId() {
            return bId;
        }

        public void setbId(byte bId) {
            this.bId = bId;
        }

        @Override
        public synchronized void run() {
            do {
                if (control.colaPasajeros.size() > 0) {
                    try {
                        Control.oSemaforoPasajeros.acquire();
                        System.out.println("Coche " + getbId() + " comienza a realizar el recorrido.");
                        Thread.sleep(5000);
                        System.out.println("Coche " + getbId() + " ha finalizado el recorrido.");
                        Control.oSemaforoPasajeros.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }while(true);
        }
    }

    public class HiloPasajero implements Runnable {
        private byte bId;
        private byte bTimeToRollerCoaster;

        public HiloPasajero(byte bId) {
            setbId(bId);
        }

        public byte getbId() {
            return bId;
        }

        public void setbId(byte bId) {
            this.bId = bId;
        }

        public byte getbTimeToRollerCoaster() {
            return bTimeToRollerCoaster;
        }

        public void setbTimeToRollerCoaster(byte bTimeToRollerCoaster) {
            this.bTimeToRollerCoaster = bTimeToRollerCoaster;
        }

        @Override
        public synchronized void run() {
            setbTimeToRollerCoaster(control.rand());

            do {
                try {
                    System.out.println("Visitante " + getbId() + " llegando a la atracción en " + getbTimeToRollerCoaster() + " segundos.");
                    Thread.sleep(getbTimeToRollerCoaster());
                    Control.oSemaforoCoches.acquire();
                    control.colaPasajeros.add(this);
                    if (Control.hayCochesLibres()){
                        System.out.println("Visitante " + getbId() + " se ha montado en un coche de la atraccion.");
                        Thread.sleep(6000);
                        System.out.println("Visitante " + getbId() + " sale de la atracción.");
                        control.getoSemaforoCoches().release();
                        control.colaPasajeros.poll();
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }

    private void executeMultiThreading() {
        ArrayList<Thread> threadsPasajeros = new ArrayList<Thread>();
        ArrayList<Thread> threadsCoches = new ArrayList<Thread>();
        Control.oSemaforoCoches = new Semaphore(2);
        Control.oSemaforoPasajeros = new Semaphore(2);

        // CREANDO LOS HILOS DE LOS PASAJEROS
        for (byte i = 0; i < control.getbNUM_PASAJEROS(); i++) {
            Control.listaPasajeros.add(new HiloPasajero(i));
        }

        // AÑADIENDO LOS HILOS DE PASAJEROS A THREADS PASAJEROS
        for (int i = 0; i < control.getbNUM_PASAJEROS(); i++) {
            threadsPasajeros.add(new Thread(Control.listaPasajeros.get(i)));
        }

        // CREANDO LOS HILOS DE LOS COCHES
        for (byte i = 0; i < control.getbNUM_COCHES(); i++) {
            Control.listaCoches.add(new HiloCoche(i));
        }

        // AÑADIENDO LOS HILOS DE COCHES A THREADS COCHES
        for (int i = 0; i < control.getbNUM_COCHES(); i++) {
            threadsCoches.add(new Thread(Control.listaCoches.get(i)));
        }

        // START HILOS PASAJEROS
        for (int i = 0; i < control.getbNUM_PASAJEROS(); i++) {
            threadsPasajeros.get(i).start();
        }

        // START HILOS COCHES
        for (int i = 0; i < control.getbNUM_COCHES(); i++) {
            threadsCoches.get(i).start();
        }
    }

    public static void main(String[] args) {

        try {
            B3_P1 oB3E8 = new B3_P1();
            oB3E8.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
