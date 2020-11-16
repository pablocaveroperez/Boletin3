import validaciones.ValidaLibrary;

import java.util.concurrent.Semaphore;

public class B3_EJ3 {

    public class Control {
        private Double dX;
        private Double dY;
        private Double dN;
        private Semaphore semaforoFactorial = new Semaphore(0);

        public Semaphore getSemaforoFactorial() {
            return semaforoFactorial;
        }

        public void setSemaforoFactorial(Semaphore semaforoFactorial) {
            this.semaforoFactorial = semaforoFactorial;
        }

        public Double getdX() {
            return dX;
        }

        public void setdX(Double dX) {
            this.dX = dX;
        }

        public Double getdY() {
            return dY;
        }

        public void setdY(Double dY) {
            this.dY = dY;
        }

        public Double getdN() {
            return dN;
        }

        public void setdN(Double dN) {
            this.dN = dN;
        }

        public Double factorial(Double dNum) {
            if (dNum == 0 || dNum == 1)
                return 1d;
            else
                return dNum * factorial(dNum -1);
        }
    }

    private Control control = new Control();

    public class P1 implements Runnable {
        @Override
        public void run() {
            control.setdX(control.factorial(control.getdN()));
            control.semaforoFactorial.release();
        }
    }

    public class P2 implements Runnable{
        private Double dK;

        public Double getdK() {
            return dK;
        }

        public void setdK(Double dK) {
            this.dK = dK;
        }

        public P2(Double dK) {
            setdK(dK);
        }

        @Override
        public void run() {
            try {
                control.semaforoFactorial.acquire();
                control.setdY(control.factorial(getdK()) * control.factorial(control.getdN() - getdK()));
                Double dResultado = (control.getdX() / control.getdY());
                System.out.println("El resultado final de la operacion es: " + dResultado);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        Double dK;
        do {
            control.setdN((Double) ValidaLibrary.valida("Introduce el valor de N: ", 0, -1,2));
            dK = (Double) ValidaLibrary.valida("Introduce el valor de K: ", 0, control.getdN(), 2);
        }while (control.getdN() < dK);

        new Thread(new P1()).start();

        new Thread(new P2(dK)).start();
    }

    public static void main(String[] args) {

        try {
            System.out.println("Urelio. Voy a usar la formula de la wikipedia: n!/(k! * (n - k)!)");
            B3_EJ3 b3Ej3 = new B3_EJ3();
            b3Ej3.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
