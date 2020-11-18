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

        public Semaphore getSemaforoCocinero() {
            return semaforoCocinero;
        }

        public void setSemaforoCocinero(Semaphore semaforoCocinero) {
            this.semaforoCocinero = semaforoCocinero;
        }

        public Semaphore getSemaforeEstudiante() {
            return semaforeEstudiante;
        }

        public void setSemaforeEstudiante(Semaphore semaforeEstudiante) {
            this.semaforeEstudiante = semaforeEstudiante;
        }

        public Queue<Estudiante> getColaEstudiantes() {
            return colaEstudiantes;
        }

        public void setColaEstudiantes(Queue<Estudiante> colaEstudiantes) {
            this.colaEstudiantes = colaEstudiantes;
        }

        public int getSerranitosActuales() {
            return serranitosActuales;
        }

        public void setSerranitosActuales(int serranitosActuales) {
            this.serranitosActuales = serranitosActuales;
        }
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
                    control.colaEstudiantes.add(this);
                    control.semaforoCocinero.acquire();
                    control.semaforeEstudiante.release();
                    Thread.sleep(10000);
                    System.out.println("El estudiante " + getiId() + " ya ha terminado de dar una vuelta por el aparcamiento.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(true);
        }
    }

    public class Cocinero implements Runnable {
        private void cogerSerranito(Estudiante estudiante) throws InterruptedException {
            if (control.serranitosActuales != 0) {
                if (estudiante != null) {
                    System.out.println("El estudiante " + estudiante.getiId() + " ha cogido un serranito ");
                    control.serranitosActuales--;
                }else
                    System.out.println("No hay estudiantes en la cola.");
            }else{
                System.out.println("El estudiante " + estudiante.getiId() + " iba a coger un serranito pero no quedan. Asi que va a despertar al cocinero");
                reponerSerranitos(estudiante);
            }
        }

        private void reponerSerranitos(Estudiante estudiante) throws InterruptedException {
            System.out.println("El Cocinero ha despertado");
            System.out.println("No quedan mas serranitos, por lo que el cocinero va a reponerlos.");
            Thread.sleep(5000);
            control.serranitosActuales = MAX_SERRANITOS;
            System.out.println("El Cocinero ha repuesto todos los Serranitos.");
            System.out.println("El estudiante " + estudiante.getiId() + " ha cogido un serranito.");
            control.serranitosActuales--;
        }

        @Override
        public void run() {
            System.out.println("El cocinero está preparado.");
            do {
                try {
                    control.semaforeEstudiante.release();
                    Estudiante estudiante = control.colaEstudiantes.poll();
                    cogerSerranito(estudiante);
                    if (estudiante != null) {
                        System.out.println("El estudiante " + estudiante.getiId() + " ya se ha comido el Serranito y va a dar una vuelta por el aparcamiento.");
                        System.out.println("El Estudiante " +  estudiante.getiId() + " tiene hambre de Serranito otra vez");
                        control.semaforoCocinero.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(true);
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        for (int i = 0; i < ESTUDIANTES; i++) {
            new Thread(new Estudiante(i)).start();
        }

        new Thread(new Cocinero()).start();
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
