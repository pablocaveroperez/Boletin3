import java.util.*;
import java.util.concurrent.Semaphore;

public class B3_EJ5 {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public class Control {
        private int iPlantas = 15, iAscensores = 4, iContador = 0;;
        private double dResultado, dArriba, dAbajo;
        private volatile Semaphore seAscensores[] = new Semaphore[iAscensores];
        private volatile Semaphore sePlantas[] = new Semaphore[iPlantas];
        private volatile int[] vPlantas = new int[iAscensores];

        public void rellenarArraySemaforos() {
            for (int i = 0; i < seAscensores.length; i++) {
                seAscensores[i] = new Semaphore(1);
            }
            for (int i = 0; i < sePlantas.length; i++) {
                sePlantas[i] = new Semaphore(1);
            }
        }

        public int ascensorCercano(int iPulsador) {

            int iPlantas = 1000;
            int iAscensor = -1;

            for (int i = 0; i < vPlantas.length; i++) {
                if (c.seAscensores[i].availablePermits() > 0) {
                    iAscensor = i;

                }
            }

            for (int i = 3; i >= 0; i--) {
                if ((Math.abs(vPlantas[i] - iPulsador) <= iPlantas) && c.seAscensores[i].availablePermits() > 0) {
                    iPlantas = (Math.abs(vPlantas[i] - iPulsador));
                    iAscensor = i;

                }
            }

            return iAscensor;
        }

    }

    private final Control c = new Control();

    public class Pulsador implements Runnable {

        @Override
        public void run() {

            int iPulsador = 0;
            do {

                iPulsador = (int) (Math.random() * 15 + 0);
            } while (c.sePlantas[iPulsador].availablePermits() == 0);

            int iPlantaDeseada = 0;
            do {
                iPlantaDeseada = (int) (Math.random() * 15 + 0);
            } while (iPulsador == iPlantaDeseada);

            System.out.println("El pulsador de la planta " + iPulsador + " ha sido activado para ir a la planta "
                    + iPlantaDeseada);

            try {
                System.out.println("La persona de la planta " + iPulsador + " esta esperando al ascensor");
                int iAscensor = 0;
                do {
                    iAscensor = c.ascensorCercano(iPulsador);
                } while (iAscensor == -1);

                System.out.println("El ascensor " + iAscensor + " va a la planta " + iPulsador);
                c.seAscensores[iAscensor].acquire();
                Thread.sleep(100);
                System.err.println("El ascensor mas cercano ha ido a recogerlo (" + iAscensor + ") a la planta " + iPulsador + " para ir a la planta " + iPlantaDeseada);
                Thread.sleep((long) Math.abs(iPulsador - iPlantaDeseada) * 500);
//				Thread.sleep(2000);
                c.sePlantas[iPulsador].release();
                c.vPlantas[iAscensor] = iPlantaDeseada;
                c.seAscensores[iAscensor].release();
                System.out.println(ANSI_BLUE + "\tEl ascensor " + iAscensor + " ha quedado libre en la planta " + iPlantaDeseada + "  " + ANSI_RESET);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public class Ascensor implements Runnable {
        int idAscensor;

        public Ascensor(int idAscensor) {
            setIdAscensor(idAscensor);
        }

        public int getIdAscensor() {
            return idAscensor;
        }

        public void setIdAscensor(int idAscensor) {
            this.idAscensor = idAscensor;
        }

        @Override
        public void run() {
            c.iContador++;
            int iPlanta = (int) (Math.random() * 15 + 0);
            c.vPlantas[idAscensor] = iPlanta;
            System.out.println("El ascensor " + idAscensor + " esta listo en la planta " + iPlanta);

        }

    }

    public static void main(String[] args) {

        try {
            B3_EJ5 principal = new B3_EJ5();
            principal.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void executeMultiThreading() throws InterruptedException {
        List<Thread> listaThreadPersona = new ArrayList<Thread>();
        List<Thread> listaThreadPlato = new ArrayList<Thread>();
        int x = 0;
        c.rellenarArraySemaforos();

        for (int i = 0; i < c.iAscensores; i++) {
            listaThreadPlato.add(new Thread(new Ascensor(i)));
            listaThreadPlato.get(i).start();
        }
        while (true) {
            Thread.sleep(1000);
            listaThreadPersona.add(new Thread(new Pulsador()));
            listaThreadPersona.get(x).start();
            x++;

        }

    }
}
