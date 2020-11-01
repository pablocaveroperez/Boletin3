import java.util.concurrent.Semaphore;

public class B3_P1 {
    public class Control {
        private final byte bNUM_PASAJEROS = 15;
        private final byte bNUM_COCHES = 3;

        public byte getbNUM_PASAJEROS() {
            return bNUM_PASAJEROS;
        }

        public byte getbNUM_COCHES() {
            return bNUM_COCHES;
        }
    }

    final Control control = new Control();

    public class Hilo implements Runnable {
        private int iId;
        private Semaphore oSemaforo;

        public Hilo(Semaphore oSemaforo, int iId) {
            setoSemaforo(oSemaforo);
            setiId(iId);
        }

        public Semaphore getoSemaforo() {
            return oSemaforo;
        }

        public void setoSemaforo(Semaphore oSemaforo) {
            this.oSemaforo = oSemaforo;
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
