import java.util.concurrent.Semaphore;

public class B3_P3 {
    private final int ESTUDIANTES = 3;

    public class Control {
        private Semaphore semaforo = new Semaphore(0);


        public Semaphore getSemaforo() {
            return semaforo;
        }

        public void setSemaforo(Semaphore semaforo) {
            this.semaforo = semaforo;
        }
    }

    private final Control control = new Control();

    public class Estudiante implements Runnable {
        private int iId;

        public Estudiante(int iId) {
            setiId(iId);
        }

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

    public class Malboro implements Runnable {

        @Override
        public void run() {

        }
    }

    private void executeMultiThreading() throws InterruptedException {
        for (int i = 0; i < ESTUDIANTES; i++) {
            new Thread(new Estudiante(i)).start();
        }

        new Thread(new Malboro()).start();
    }

    public static void main(String[] args) {
        try {
            B3_P3 b3Ep3 = new B3_P3();
            b3Ep3.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
