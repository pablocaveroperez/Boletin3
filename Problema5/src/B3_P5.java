import java.util.concurrent.Semaphore;

public class B3_P5 {
    private final byte GOOGLEMEETS = 0;
    private final byte DISCORD = 1;

    public class Control {
        private Semaphore semaforoGoogleMeets = new Semaphore(0);
        private Semaphore semaforoDiscord = new Semaphore(0);

        public Semaphore getSemaforoGoogleMeets() {
            return semaforoGoogleMeets;
        }

        public void setSemaforoGoogleMeets(Semaphore semaforoGoogleMeets) {
            this.semaforoGoogleMeets = semaforoGoogleMeets;
        }

        public Semaphore getSemaforoDiscord() {
            return semaforoDiscord;
        }

        public void setSemaforoDiscord(Semaphore semaforoDiscord) {
            this.semaforoDiscord = semaforoDiscord;
        }
    }

    private final Control control = new Control();

    public class Urelio implements Runnable {

        @Override
        public synchronized void run() {
            do {

            }while (true);
        }
    }

    public class Estudiante implements Runnable {
        private int iId;
        private byte bPlataforma;

        public Estudiante(int iId) {
            setiId(iId);
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        public int getbPlataforma() {
            return bPlataforma;
        }

        public void setbPlataforma(byte bPlataforma) {
            this.bPlataforma = bPlataforma;
        }

        @Override
        public synchronized void run() {
            setbPlataforma((byte) (Math.random() * 2));
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;
        new Thread(new Urelio()).start();

        while (true) {
            Thread.sleep(5000);
            new Thread(new Estudiante(iContador)).start();
            iContador++;
            new Thread(new Estudiante(iContador)).start();
            iContador++;
            new Thread(new Estudiante(iContador)).start();
            iContador++;
        }
    }

    public static void main(String[] args) {
        try {
            B3_P5 b3P5 = new B3_P5();
            b3P5.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
