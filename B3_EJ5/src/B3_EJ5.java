import java.util.*;
import java.util.concurrent.Semaphore;

public class B3_EJ5 {
    private final int NUM_ASCENSORES = 4;

    public class Control {
        private Semaphore semaforoAscensor = new Semaphore(NUM_ASCENSORES);
        private Semaphore semaforoPlanta = new Semaphore(0);
        private Deque<Planta> colaLlamadasAscensor = new LinkedList<Planta>();
        private List<Ascensor> ascensores = new ArrayList<>();

        public List<Ascensor> getAscensores() {
            return ascensores;
        }

        public void setAscensores(List<Ascensor> ascensores) {
            this.ascensores = ascensores;
        }

        public Semaphore getSemaforoAscensor() {
            return semaforoAscensor;
        }

        public void setSemaforoAscensor(Semaphore semaforoAscensor) {
            this.semaforoAscensor = semaforoAscensor;
        }

        public Semaphore getSemaforoPlanta() {
            return semaforoPlanta;
        }

        public void setSemaforoPlanta(Semaphore semaforoPlanta) {
            this.semaforoPlanta = semaforoPlanta;
        }

        public Deque<Planta> getColaLlamadasAscensor() {
            return colaLlamadasAscensor;
        }

        public void setColaLlamadasAscensor(Deque<Planta> colaLlamadasAscensor) {
            this.colaLlamadasAscensor = colaLlamadasAscensor;
        }

        public Ascensor ascensorCercano(int iPlanta) {
            Ascensor ascensor = null;

            for (int i = 0; i < ascensores.size()-1; i++) {
                if (Math.abs(ascensores.get(i).getiPlanta() - iPlanta) > Math.abs(ascensores.get(i+1).getiPlanta() - iPlanta))
                    ascensor = ascensores.get(i+1);
                else if (Math.abs(ascensores.get(i).getiPlanta() - iPlanta) < Math.abs(ascensores.get(i+1).getiPlanta() - iPlanta))
                    ascensor = ascensores.get(i);
                else
                    ascensor = ascensores.get(i);
            }
            return ascensor;
        }
    }

    private final Control control = new Control();

    public class Ascensor implements Runnable {
        private int iId = 0;
        private int iPlanta = 0;

        public Ascensor(int iId) {
            this.iId = iId;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        public int getiPlanta() {
            return iPlanta;
        }

        public void setiPlanta(int iPlanta) {
            this.iPlanta = iPlanta;
        }

        @Override
        public synchronized void run() {
            control.ascensores.add(this);

            while (true) {
                System.out.println("El Ascensor " + iId + " está listo para usarse. Esta en la planta " + getiPlanta());

                try {
                    control.semaforoAscensor.acquire();
                    control.semaforoPlanta.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int iPlanta = control.colaLlamadasAscensor.poll().iId;

                System.out.println("Se ha llamado al ascensor " + iId + " desde la planta " + iPlanta);
                System.out.println("El ascensor " + iId + " está ocupado");

                try {
                    Thread.sleep((int) ((Math.random() * 7) * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setiPlanta(iPlanta);
                System.out.println("El ascensor " + iId + " ha quedado libre en la planta " + getiPlanta());
                control.semaforoAscensor.release();
            }
        }
    }

    public class Planta implements Runnable {

        private int iId = 0;

        public Planta(int iId) {
            this.iId = iId;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public void run() {

            System.out.println("Se pulsa el botón en la planta " + getiId());

            Ascensor ascensor = control.ascensorCercano(getiId());

           if (ascensor != null) {
               control.colaLlamadasAscensor.offerFirst(this);
           }else
               control.colaLlamadasAscensor.add(this);

            control.semaforoPlanta.release();
        }

    }

    private void executeMultiThreading() throws InterruptedException {

        for (int i = 0; i < NUM_ASCENSORES; i++) {
            new Thread(new Ascensor(i)).start();
        }

        while (true) {
            int iId = (int) (Math.random() * 15);
            Thread.sleep(2000);
            new Thread(new Planta(iId)).start();
        }

    }

    public static void main(String[] args) {

        try {
            B3_EJ5 b3Ej5 = new B3_EJ5();
            b3Ej5.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
