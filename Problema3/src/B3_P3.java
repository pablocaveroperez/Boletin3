public class B3_P3 {
    public class Control {

    }

    private final Control control = new Control();

    private void executeMultiThreading() throws InterruptedException {
        for (int i = 0; i < ESTUDIANTES; i++) {
            new Thread(new Estudiante(i)).start();
        }

        new Thread(new Cocinero()).start();
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
