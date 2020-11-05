public class B3_EJ1 {

    public class Control {
        public final byte bPLATOS_MAX = 5;
        public volatile int idCocinero = 0;
        public volatile int idComensal = 0;

        public int getIdCocinero() {
            return idCocinero;
        }

        public void setIdCocinero(int idCocinero) {
            this.idCocinero = idCocinero;
        }

        public int getIdComensal() {
            return idComensal;
        }

        public void setIdComensal(int idComensal) {
            this.idComensal = idComensal;
        }
    }

    Control control = new Control();

    public class Cocinero implements Runnable {

        @Override
        public void run() {

        }
    }

    public class Comensal implements Runnable {

        @Override
        public void run() {

        }
    }

    /**
     * Este metodo es el que vamos a utilizar para todos los programas de MultiThreading.
     * Sirve para lanzar los hilos correcpondientes.
     */
    private void executeMultiThreading() {
        int iContador = 0;

        while (true) {
            iContador++;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            B3_EJ1 b3EJ1 = new B3_EJ1();
            b3EJ1.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
