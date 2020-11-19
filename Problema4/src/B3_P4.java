import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_P4 {
    private final byte PERSONAS_A_LA_VEZ = 10;
    private final byte SUBE = 1;
    private final byte BAJA = 0;

    public class Control {
        private Semaphore semaforoBaja = new Semaphore(0);
        private Semaphore semaforoSube = new Semaphore(0);

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
                    control.semaforoSube.acquire();
                }else {
                    System.out.println("El estudiante " + getiId() + " esta esperando para bajar.");
                    control.semaforoBaja.acquire();
                }
                System.out.println("El estudiante " + getiId() + " ha salido de la escalera.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Escalera implements Runnable {

        @Override
        public synchronized void run() {
            do {
                try {
                    Thread.sleep(250);
                    if (control.semaforoSube.getQueueLength() != 0 && control.semaforoBaja.getQueueLength() != 0) {
                        if (control.semaforoBaja.getQueueLength() >= PERSONAS_A_LA_VEZ || control.semaforoSube.getQueueLength() == 0) {
                            int iTamanio = control.semaforoBaja.getQueueLength();
                            int i;
                            if (control.semaforoBaja.getQueueLength() > PERSONAS_A_LA_VEZ)
                                iTamanio = PERSONAS_A_LA_VEZ;
                            System.out.println("Han bajado " + iTamanio + " estudiantes.");
                            control.semaforoBaja.release(iTamanio);
                        } else if (control.semaforoSube.getQueueLength() >= PERSONAS_A_LA_VEZ || control.semaforoBaja.getQueueLength() == 0) {
                            int iTamanio = control.semaforoSube.getQueueLength();
                            int i;
                            if (control.semaforoSube.getQueueLength() > PERSONAS_A_LA_VEZ)
                                iTamanio = PERSONAS_A_LA_VEZ;
                            System.out.println("Han subido " + iTamanio + " estudiantes.");
                            control.semaforoSube.release(iTamanio);
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

        new Thread(new Escalera()).start();

        while (true) {
            new Thread(new Estudiante(iContador)).start();
            Thread.sleep(750);
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
