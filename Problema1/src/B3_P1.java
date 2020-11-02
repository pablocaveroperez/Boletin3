import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class B3_P1 {
    public class Control {
        private final byte bNUM_PASAJEROS = 15;
        private final byte bNUM_COCHES = 3;
        private Semaphore oSemaforoCoches;
        private Semaphore oSemaforoPasajeros;

        public Semaphore getoSemaforoCoches() {
            return oSemaforoCoches;
        }

        public void setoSemaforoCoches(Semaphore oSemaforoCoches) {
            this.oSemaforoCoches = oSemaforoCoches;
        }

        public Semaphore getoSemaforoPasajeros() {
            return oSemaforoPasajeros;
        }

        public void setoSemaforoPasajeros(Semaphore oSemaforoPasajeros) {
            this.oSemaforoPasajeros = oSemaforoPasajeros;
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
        public void run() {

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
            System.out.println("Visitante " + getbId() + " llegando a la atracción en " + getbTimeToRollerCoaster());
        }
    }

    private void executeMultiThreading() {
        ArrayList<HiloPasajero> listaPasajeros = new ArrayList<HiloPasajero>();
        ArrayList<HiloCoche> listaCoches = new ArrayList<HiloCoche>();

        ArrayList<Thread> threadsPasajeros = new ArrayList<Thread>();
        ArrayList<Thread> threadsCoches = new ArrayList<Thread>();

        // CREANDO LOS HILOS DE LOS PASAJEROS
        for (byte i = 0; i < control.getbNUM_PASAJEROS(); i++) {
            listaPasajeros.add(new HiloPasajero(i));
        }

        // AÑADIENDO LOS HILOS DE PASAJEROS A THREADS PASAJEROS
        for (int i = 0; i < control.getbNUM_PASAJEROS(); i++) {
            threadsPasajeros.add(new Thread(listaPasajeros.get(i)));
        }

        // CREANDO LOS HILOS DE LOS COCHES
        for (byte i = 0; i < control.getbNUM_COCHES(); i++) {
            listaCoches.add(new HiloCoche(i));
        }

        // AÑADIENDO LOS HILOS DE COCHES A THREADS COCHES
        for (int i = 0; i < control.getbNUM_COCHES(); i++) {
            threadsCoches.add(new Thread(listaCoches.get(i)));
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
