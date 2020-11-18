import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_P2 {
    private final int ESTUDIANTES = 10;
    private final int MAX_SERRANITOS = 15;
    public class Control {
        private Semaphore semaforoCocinero = new Semaphore(1);
        private Semaphore semaforeEstudiante = new Semaphore(0);
        private Queue<Estudiante> colaEstudiantes = new LinkedList<Estudiante>();
        private int serranitosActuales = MAX_SERRANITOS;
    }

    private Control control = new Control();

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
            do {
                try {
                    System.out.println("El Estudiante " +  getiId() + " tiene hambre de Serranito otra vez");
                    control.colaEstudiantes.add(this);
                    control.semaforoCocinero.release();
                    control.semaforeEstudiante.acquire();





                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(true);
        }
    }

    public class Cocinero implements Runnable {
        private int iId;

        public Cocinero(int iId) {
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
            System.out.println("El cocinero está preparado.");
            do {
                try {
                    control.semaforoCocinero.acquire();
                    if (control.serranitosActuales != 0) {
                        control.serranitosActuales--;
                        Estudiante estudiante = control.colaEstudiantes.poll();
                        if (estudiante != null) {
                            System.out.println("El estudiante " + estudiante.getiId() + " ha cogido el serranito " + getiId());

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(true);
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        for (int i = 0; i < MAX_SERRANITOS; i++) {
            new Thread(new Cocinero(i)).start();
        }

        for (int i = 0; i < ESTUDIANTES; i++) {
            new Thread(new Estudiante(i)).start();
        }
    }

    public static void main(String[] args) {
        try {
            B3_P2 b3Ep2 = new B3_P2();
            b3Ep2.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
