import java.util.concurrent.Semaphore;

public class B3_EJ5 {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public class Control {
        private int iPlantas = 15, iAscensores = 4, iContador = 0;
        private volatile Semaphore semaforosAscensores[] = new Semaphore[iAscensores];
        private volatile Semaphore semaforosPlantas[] = new Semaphore[iPlantas];
        private volatile int[] plantas = new int[iAscensores];

        public void rellenarArraySemaforos() {
            for (int i = 0; i < semaforosAscensores.length; i++) {
                semaforosAscensores[i] = new Semaphore(1);
            }
            for (int i = 0; i < semaforosPlantas.length; i++) {
                semaforosPlantas[i] = new Semaphore(1);
            }
        }

        public int ascensorCercano(int iPulsador) {
            int iAscensor = -1;

            for (int i = 0; i < plantas.length; i++) {
                if (control.semaforosAscensores[i].availablePermits() > 0) {
                    iAscensor = i;
                }
            }

            for (int i = 3; i >= 0; i--) {
                if ((Math.abs(plantas[i] - iPulsador) <= iPlantas) && control.semaforosAscensores[i].availablePermits() > 0) {
                    iPlantas = (Math.abs(plantas[i] - iPulsador));
                    iAscensor = i;
                }
            }
            return iAscensor;
        }

    }

    private final Control control = new Control();

    public class Pulsador implements Runnable {

        @Override
        public void run() {

            int iPulsador = 0;
            do {

                iPulsador = (int) (Math.random() * 15 + 0);
            } while (control.semaforosPlantas[iPulsador].availablePermits() == 0);

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
                    iAscensor = control.ascensorCercano(iPulsador);
                } while (iAscensor == -1);

                System.out.println("El ascensor " + iAscensor + " va a la planta " + iPulsador);
                control.semaforosAscensores[iAscensor].acquire();
                Thread.sleep(100);
                System.err.println("El ascensor mas cercano ha ido a recogerlo (" + iAscensor + ") a la planta " + iPulsador + " para ir a la planta " + iPlantaDeseada);
                Thread.sleep((long) Math.abs(iPulsador - iPlantaDeseada) * 500);

                control.semaforosPlantas[iPulsador].release();
                control.plantas[iAscensor] = iPlantaDeseada;
                control.semaforosAscensores[iAscensor].release();
                System.out.println(ANSI_BLUE + "\tEl ascensor " + iAscensor + " ha quedado libre en la planta " + iPlantaDeseada + "  " + ANSI_RESET);

            } catch (InterruptedException e) {
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
            control.iContador++;
            int iPlanta = (int) (Math.random() * 15 + 0);
            control.plantas[idAscensor] = iPlanta;
            System.out.println("El ascensor " + idAscensor + " esta listo en la planta " + iPlanta);
        }

    }

    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;
        control.rellenarArraySemaforos();

        for (int i = 0; i < control.iAscensores; i++) {
            new Thread(new Ascensor(i)).start();
        }
        while (true) {
            Thread.sleep(1000);
            new Thread(new Pulsador()).start();
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
}
