import java.util.concurrent.Semaphore;

public class B3_P3 {
    private final int ESTUDIANTES = 3;

    public class Control {
        private Semaphore semaforoMalboro = new Semaphore(0);
        private Semaphore semaforoEstudiante = new Semaphore(0);
        private volatile int iIngrediente;
        private volatile int iTabaco;
        private volatile int iPapel;
        private volatile int iCerillas;

        public Semaphore getSemaforoMalboro() {
            return semaforoMalboro;
        }

        public void setSemaforoMalboro(Semaphore semaforoMalboro) {
            this.semaforoMalboro = semaforoMalboro;
        }

        public Semaphore getSemaforoEstudiante() {
            return semaforoEstudiante;
        }

        public void setSemaforoEstudiante(Semaphore semaforoEstudiante) {
            this.semaforoEstudiante = semaforoEstudiante;
        }

        public int getiIngrediente() {
            return iIngrediente;
        }

        public void setiIngrediente(int iIngrediente) {
            this.iIngrediente = iIngrediente;
        }

        public int getiTabaco() {
            return iTabaco;
        }

        public void setiTabaco(int iTabaco) {
            this.iTabaco = iTabaco;
        }

        public int getiPapel() {
            return iPapel;
        }

        public void setiPapel(int iPapel) {
            this.iPapel = iPapel;
        }

        public int getiCerillas() {
            return iCerillas;
        }

        public void setiCerillas(int iCerillas) {
            this.iCerillas = iCerillas;
        }
    }

    private final Control control = new Control();

    public class Estudiante implements Runnable {
        private int iIngrediente;
        private int iId;

        public Estudiante(int iId) {
            setiId(iId);
        }

        public int getiIngrediente() {
            return iIngrediente;
        }

        public void setiIngrediente(int iIngrediente) {
            this.iIngrediente = iIngrediente;
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
