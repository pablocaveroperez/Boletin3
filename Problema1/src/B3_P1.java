public class B3_P1 {
    public class Control {

    }

    final Control control = new Control();

    public class Hilo implements Runnable {
        private int iId;

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public synchronized void run() {

        }
    }

    private void executeMultiThreading() {

    }

    public static void main(String[] args) {

        try {
            B3_P1 oB3E8 = new B3_P1();
            oB3E8.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
