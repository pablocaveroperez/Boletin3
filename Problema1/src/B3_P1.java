import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class B3_P1 {
    public class Control {
        private final byte bNUM_PASAJEROS = 15;
        private final byte bNUM_COCHES = 3;
        private Semaphore oSemaforoCoches;
        private Semaphore oSemaforoPasajeros;
        private ArrayList<HiloPasajero> listaPasajeros = new ArrayList<HiloPasajero>();
        private ArrayList<HiloCoche> listaCoches = new ArrayList<HiloCoche>();

        public ArrayList<HiloCoche> getListaCoches() {
            return listaCoches;
        }

        public ArrayList<HiloPasajero> getListaPasajeros() {
            return listaPasajeros;
        }

        public void setListaCoches(ArrayList<HiloCoche> listaCoches) {
            this.listaCoches = listaCoches;
        }

        public void setListaPasajeros(ArrayList<HiloPasajero> listaPasajeros) {
            this.listaPasajeros = listaPasajeros;
        }

        public Semaphore getoSemaforoCoches() {
            return oSemaforoCoches;
        }

        public void setoSemaforoCoches(Semaphore oSemaforoCoches) {
            this.oSemaforoCoches = oSemaforoCoches;
        }

        public Semaphore getoSemaforoPasajeros() {
            return oSemaforoPasajeros;
        }

        public void setoSemaforoPasajeros(Semaphore oSemaforoPasajeros) {
            this.oSemaforoPasajeros = oSemaforoPasajeros;
        }

        public byte getbNUM_PASAJEROS() {
            return bNUM_PASAJEROS;
        }

        public byte getbNUM_COCHES() {
            return bNUM_COCHES;
        }

        public byte rand() {
            Random r = new Random();
            return (byte) (1 + r.nextInt(5 - 1 + 1));
        }

        public boolean hayCochesLibres() {
            boolean bResultado = false;
            byte bContador = 0;

            while (!bResultado && bContador < bNUM_COCHES) {
                if (listaCoches.get(bContador).isbEstaLibre())
                    bResultado = true;
                else
                    bContador++;
            }
            return bResultado;
        }

        public void ocuparCoche() {
            boolean bResultado = false;
            byte bContador = 0;

            while (!bResultado && bContador < bNUM_COCHES) {
                if (listaCoches.get(bContador).isbEstaLibre())
                    bResultado = true;
                else
                    bContador++;
            }

            if (bResultado)
                listaCoches.get(bContador).setbEstaLibre(bResultado);
        }

    }

    final Control control = new Control();

    public class HiloCoche implements Runnable {
        private byte bId;
        private boolean bEstaLibre = true;

        public boolean isbEstaLibre() {
            return bEstaLibre;
        }

        public void setbEstaLibre(boolean bEstaLibre) {
            this.bEstaLibre = bEstaLibre;
        }

        public HiloCoche(byte bId) {
            setbId(bId);
        }

        public byte getbId() {
            return bId;
        }

        public void setbId(byte bId) {
            this.bId = bId;
        }

        @Override
        public void run() {
            do {

            }while(true);
        }
    }

    public class HiloPasajero implements Runnable {
        private byte bId;
        private byte bTimeToRollerCoaster;
        private boolean bListo = false;

        public boolean isbListo() {
            return bListo;
        }

        public void setbListo(boolean bListo) {
            this.bListo = bListo;
        }

        public HiloPasajero(byte bId) {
            setbId(bId);
        }

        public byte getbId() {
            return bId;
        }

        public void setbId(byte bId) {
            this.bId = bId;
        }

        public byte getbTimeToRollerCoaster() {
            return bTimeToRollerCoaster;
        }

        public void setbTimeToRollerCoaster(byte bTimeToRollerCoaster) {
            this.bTimeToRollerCoaster = bTimeToRollerCoaster;
        }

        @Override
        public void run() {
            setbTimeToRollerCoaster(control.rand());
            try {
                Thread.sleep(getbTimeToRollerCoaster()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            do {
                try {

                    System.out.println("Visitante " + getbId() + " llegando a la atracción en " + getbTimeToRollerCoaster() + " segundos.");
                    setbListo(true);
                    control.oSemaforoCoches.acquire();
                    if (control.hayCochesLibres()){
                        control.ocuparCoche();
                        System.out.println("Visitante " + getbId() + " se ha montado en un coche de la atraccion.");

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }while (true);
        }
    }

    private void executeMultiThreading() {
        ArrayList<Thread> threadsPasajeros = new ArrayList<Thread>();
        ArrayList<Thread> threadsCoches = new ArrayList<Thread>();
        control.oSemaforoCoches = new Semaphore(3);
        control.oSemaforoPasajeros = new Semaphore(1);

        // CREANDO LOS HILOS DE LOS PASAJEROS
        for (byte i = 0; i < control.getbNUM_PASAJEROS(); i++) {
            control.listaPasajeros.add(new HiloPasajero(i));
        }

        // AÑADIENDO LOS HILOS DE PASAJEROS A THREADS PASAJEROS
        for (int i = 0; i < control.getbNUM_PASAJEROS(); i++) {
            threadsPasajeros.add(new Thread(control.listaPasajeros.get(i)));
        }

        // CREANDO LOS HILOS DE LOS COCHES
        for (byte i = 0; i < control.getbNUM_COCHES(); i++) {
            control.listaCoches.add(new HiloCoche(i));
        }

        // AÑADIENDO LOS HILOS DE COCHES A THREADS COCHES
        for (int i = 0; i < control.getbNUM_COCHES(); i++) {
            threadsCoches.add(new Thread(control.listaCoches.get(i)));
        }

        // START HILOS PASAJEROS
        for (int i = 0; i < control.getbNUM_PASAJEROS(); i++) {
            threadsPasajeros.get(i).start();
        }

        // START HILOS COCHES
        for (int i = 0; i < control.getbNUM_COCHES(); i++) {
            threadsCoches.get(i).start();
        }
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
