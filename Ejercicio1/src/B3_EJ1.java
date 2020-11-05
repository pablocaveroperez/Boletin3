public class B3_EJ1 {

    public class Control {

    }

    Control control = new Control();

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
            B3_EJ1 b3E1 = new B3_EJ1();
            b3E1.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
