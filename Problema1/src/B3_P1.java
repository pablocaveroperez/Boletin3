import java.util.ArrayList;
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
    }

    final Control control = new Control();

    public class HiloCoche implements Runnable {
        private int iId;

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public void run() {

        }
    }

    public class HiloPasajero implements Runnable {
        private int iId;

        public HiloPasajero(int iId) {
            setiId(iId);
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public synchronized void run() {

        }
    }

    private void executeMultiThreading() {
        ArrayList<Thread> listaThreads = new ArrayList<Thread>();
        ArrayList<>
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
