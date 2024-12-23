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
                return dNum * factorial(dNum - 1);
        }
    }

    private final Control control = new Control();

    public class P1 implements Runnable {
        @Override
        public void run() {
            control.setdX(control.factorial(control.getdN()));
            control.semaforoFactorial.release();
        }
    }

    public class P2 implements Runnable {
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
                System.out.println("El resultado final de la operacion es: " + dResultado + "\t Ay mi madre el \"Bicho\".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        Double dK, dN;
        do {
            dN = (Double) ValidaLibrary.valida("Introduce el valor de N(Debe ser mayor o igual que k): ", 0, -1,2);
            dK = (Double) ValidaLibrary.valida("Introduce el valor de K(Debe ser mayor o igual que 0 y menor o igual que n): ", 0, dN, 2);
        }while (dN < dK);

        control.setdN(dN);

        Thread t1 = new Thread(new P1());
        t1.start();

        Thread t2 = new Thread(new P2(dK));
        t2.start();

        t2.join();

    }

    public static void main(String[] args) {
        try {
            System.out.println("Urelio. Voy a usar la formula de la wikipedia: n!/(k! * (n - k)!). I want my ten");
            B3_EJ3 b3Ej3 = new B3_EJ3();
            b3Ej3.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
