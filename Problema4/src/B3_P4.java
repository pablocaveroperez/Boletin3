import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_P4 {
    private final byte PERSONAS_A_LA_VEZ = 10;
    private final byte SUBE = 1;
    private final byte BAJA = 0;

    public class Control {
        private Semaphore semaforoBaja = new Semaphore(10);
        private Semaphore semaforoSube = new Semaphore(10);
        private Queue<Estudiante> colaSube = new LinkedList<Estudiante>();
        private Queue<Estudiante> colaBaja = new LinkedList<Estudiante>();

        public Queue<Estudiante> getColaSube() {
            return colaSube;
        }

        public void setColaSube(Queue<Estudiante> colaSube) {
            this.colaSube = colaSube;
        }

        public Queue<Estudiante> getColaBaja() {
            return colaBaja;
        }

        public void setColaBaja(Queue<Estudiante> colaBaja) {
            this.colaBaja = colaBaja;
        }

        public Semaphore getSemaforoBaja() {
            return semaforoBaja;
        }

        public void setSemaforoBaja(Semaphore semaforoBaja) {
            this.semaforoBaja = semaforoBaja;
        }

        public Semaphore getSemaforoSube() {
            return semaforoSube;
        }

        public void setSemaforoSube(Semaphore semaforoSube) {
            this.semaforoSube = semaforoSube;
        }
    }

    private final Control control = new Control();

    public class Estudiante implements Runnable {
        private int iId;
        private byte bTrambolico;

        public Estudiante(int iId) {
            setiId(iId);
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        public byte getbTrambolico() {
            return bTrambolico;
        }

        public void setbTrambolico(byte bTrambolico) {
            this.bTrambolico = bTrambolico;
        }

        @Override
        public synchronized void run() {
            try {
                setbTrambolico((byte) (Math.random() * 2));
                if (getbTrambolico() == SUBE) {
                    System.out.println("El estudiante " + getiId() + " esta esperando para subir.");
                    control.colaSube.add(this);
                    control.semaforoSube.acquire();
                }else {
                    System.out.println("El estudiante " + getiId() + " esta esperando para bajar.");
                    control.colaBaja.add(this);
                    control.semaforoBaja.acquire();
                }
                System.out.println("El estudiante " + getiId() + " ya ha salido de la escalera.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Escalera implements Runnable {

        @Override
        public synchronized void run() {

        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;

        new Thread(new Escalera()).start();

        while (true) {
            new Thread(new Estudiante(iContador)).start();
            Thread.sleep(150);
            iContador++;
        }
    }

    public static void main(String[] args) {
        try {
            B3_P4 b3Ep4 = new B3_P4();
            b3Ep4.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
