import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_P4 {
    private final byte PERSONAS_A_LA_VEZ = 10;
    private final byte SUBE = 1;
    private final byte BAJA = 0;

    public class Control {
        private Semaphore semaforoArriba = new Semaphore(10);
        private Semaphore semaforoAbajo = new Semaphore(10);
        private Queue<Sevilla> colaSube = new LinkedList<Sevilla>();
        private Queue<Betis> colaBaja = new LinkedList<Betis>();
        private byte genteBajando = 0;
        private byte genteSubiendo = 0;
        private boolean subirPosible = false;
        private boolean bajarPosible = false;

        public boolean isSubirPosible() {
            return subirPosible;
        }

        public void setSubirPosible(boolean subirPosible) {
            this.subirPosible = subirPosible;
        }

        public boolean isBajarPosible() {
            return bajarPosible;
        }

        public void setBajarPosible(boolean bajarPosible) {
            this.bajarPosible = bajarPosible;
        }

        public byte getGenteBajando() {
            return genteBajando;
        }

        public void setGenteBajando(byte genteBajando) {
            this.genteBajando = genteBajando;
        }

        public byte getGenteSubiendo() {
            return genteSubiendo;
        }

        public void setGenteSubiendo(byte genteSubiendo) {
            this.genteSubiendo = genteSubiendo;
        }

        public Queue<Sevilla> getColaSube() {
            return colaSube;
        }

        public void setColaSube(Queue<Sevilla> colaSube) {
            this.colaSube = colaSube;
        }

        public Queue<Betis> getColaBaja() {
            return colaBaja;
        }

        public void setColaBaja(Queue<Betis> colaBaja) {
            this.colaBaja = colaBaja;
        }

        public Semaphore getSemaforoArriba() {
            return semaforoArriba;
        }

        public void setSemaforoArriba(Semaphore semaforoArriba) {
            this.semaforoArriba = semaforoArriba;
        }

        public Semaphore getSemaforoAbajo() {
            return semaforoAbajo;
        }

        public void setSemaforoAbajo(Semaphore semaforoAbajo) {
            this.semaforoAbajo = semaforoAbajo;
        }
    }

    private final Control control = new Control();

    public class Sevilla implements Runnable {
        private int iId;

        public Sevilla(int iId) {
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
            try {
                control.semaforo.acquire();
                control.colaSube.add(this);
                if ((control.colaBaja.isEmpty() && control.genteSubiendo < 10) || !control.bajarPosible) {
                    control.subirPosible = true;
                    control.bajarPosible = false;
                    System.out.println("El Sevillista " + getiId() + " se dispone a subir");
                    control.genteSubiendo++;
                    Thread.sleep(2000);
                    System.out.println("El Sevillista " + getiId() + " ha subido");
                }

                if (control.genteSubiendo == 10) {
                    System.out.println("Ya han subido 10 sevillistas.");
                    control.genteBajando = 0;
                    for (int i = 0; i < 10; i++)
                        control.colaBaja.poll();
                    control.bajarPosible = true;
                    control.subirPosible = false;
                    control.semaforoAbajo.release(10);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Betis implements Runnable {
        private int iId;

        public Betis(int iId) {
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
            try {
                control.semaforo.acquire();
                control.colaBaja.add(this);
                if ((control.colaSube.isEmpty() && control.genteBajando < 10) || !control.subirPosible) {
                    control.bajarPosible = true;
                    control.subirPosible = false;
                    System.out.println("El Betico " + getiId() + " se dispone a bajar. Jiji");
                    control.genteBajando++;
                    Thread.sleep(2000);
                    System.out.println("El Betico " + getiId() + " ha bajado. Jiji");
                }
                if (control.genteBajando == 10) {
                    System.out.println("Ya han bajado 10 beticos.");
                    control.genteBajando = 0;
                    for (int i = 0; i < 10; i++)
                        control.colaBaja.poll();
                    control.subirPosible = true;
                    control.bajarPosible = false;
                    control.semaforoArriba.release(10);
                    Thread.sleep(2000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;
        while (true) {
            Thread.sleep(150);
            new Thread(new Sevilla(iContador)).start();
            iContador++;
            Thread.sleep(150);
            new Thread(new Betis(iContador)).start();
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
