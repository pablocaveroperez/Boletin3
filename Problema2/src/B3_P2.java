import java.util.concurrent.Semaphore;

public class B3_P2 {
    private final int ESTUDIANTES = 5;
    private final int MAX_SERRANITOS = 3;
    public class Control {
        private Semaphore semaforoCocinero = new Semaphore(0);
        private Semaphore semaforoEstudiante = new Semaphore(0);
        private int serranitosActuales = MAX_SERRANITOS;
        private volatile int iId;

        public Semaphore getSemaforoCocinero() {
            return semaforoCocinero;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        public void setSemaforoCocinero(Semaphore semaforoCocinero) {
            this.semaforoCocinero = semaforoCocinero;
        }

        public Semaphore getSemaforoEstudiante() {
            return semaforoEstudiante;
        }

        public void setSemaforoEstudiante(Semaphore semaforoEstudiante) {
            this.semaforoEstudiante = semaforoEstudiante;
        }

        public int getSerranitosActuales() {
            return serranitosActuales;
        }

        public void setSerranitosActuales(int serranitosActuales) {
            this.serranitosActuales = serranitosActuales;
        }

        private void cogerSerranito() throws InterruptedException {
            if (control.serranitosActuales != 0) {
                System.out.println("El estudiante " + getiId() + " ha cogido un serranito ");
                control.serranitosActuales--;
            }
        }

        private void reponerSerranitos() throws InterruptedException {
            System.out.println("El Cocinero ha despertado");
            System.out.println("No quedan mas serranitos, por lo que el cocinero va a reponerlos.");
            Thread.sleep(5000);
            control.serranitosActuales = MAX_SERRANITOS;
            System.out.println("El Cocinero ha repuesto todos los Serranitos.");
            System.out.println("El estudiante " + getiId() + " ha cogido un serranito.");
            control.serranitosActuales--;
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
                    control.semaforoCocinero.acquire();
                    control.setiId(getiId());
                    System.out.println("El estudiante " + control.getiId() + " quiere un serranito.");
                    if (control.serranitosActuales != 0) {
                        control.cogerSerranito();
                    }else {
                        System.out.println("El estudiante " + control.getiId() + " ha despertado al cocinero que es un floho.");
                        control.reponerSerranitos();
                    }
                    Thread.sleep(2000);
                    System.out.println("El estudiante " + control.getiId() + " ya se ha comido el Serranito y va a dar una vuelta por el aparcamiento.");
                    control.semaforoEstudiante.release();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(true);
        }
    }

    public class Cocinero implements Runnable {
        @Override
        public void run() {
            System.out.println("El cocinero está preparado.");
            do {
                try {
                    control.semaforoCocinero.release();

                    Thread.sleep(2000);
                    control.semaforoEstudiante.acquire();
                    Thread.sleep(1000);
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
