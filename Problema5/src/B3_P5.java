public class B3_P5 {
    public class Control {

    }

    private final Control control = new Control();

    public class Urelio {

    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;
        new Thread(new Aurelinex()).start();

        while (true) {
            Thread.sleep(5000);
            int i = (int) (1 + Math.random() * PREMIUM);
            new Thread(new DamAir(i, iContador)).start();
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
