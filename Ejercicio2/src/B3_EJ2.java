import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_EJ2 {
    private final int NUM_PALILLOS = 5;
    private final int NUM_ESTUDIANTES = 5;

    public class Control {
        private Semaphore semaforoPalillos = new Semaphore(NUM_PALILLOS);
        private Semaphore semaphoreEstudiantes = new Semaphore(0);
        private Queue<Filosofo> colaFilosofos = new LinkedList<Filosofo>();
    }

    private Control control = new Control();

    public class Filosofo implements Runnable {
        private int iId;

        public Filosofo(int iId) {
            this.iId = iId;
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

    public class Palillo implements Runnable {
        private int iId;

        public Palillo(int iId) {
            this.iId = iId;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public void run() {
            do {
                try {
                    control.semaforoPalillos.acquire();
                    control.semaphoreEstudiantes.acquire();

                    ;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int idFilosofo = control.colaFilosofos.poll().getiId();
                System.out.println("El filósofo " + idFilosofo + " ha terminado de comer.");

                System.out.println("Los palillos " + getiId() + " y " + (getiId()+1) + " estan libres.");
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }

    /**
     * Este metodo es el que vamos a utilizar para todos los programas de MultiThreading.
     * Sirve para lanzar los hilos correcpondientes.
     */
    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;

        for (int i = 0; i < NUM_PALILLOS; i++) {
            new Thread(new Palillo(i)).start();
        }

        for (int i = 0; i < NUM_ESTUDIANTES; i++) {
            new Thread(new Filosofo(i)).start();
        }
    }

    public static void main(String[] args) {
        try {
            B3_EJ2 b3Ej2 = new B3_EJ2();
            b3Ej2.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
