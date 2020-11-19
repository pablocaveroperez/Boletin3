import java.util.concurrent.Semaphore;

public class B3_P3 {
    private final byte PAPEL = 1;
    private final byte CERILLA = 2;
    private final byte TABACO = 3;

    public class Control {
        private Semaphore semaforoTabaco = new Semaphore(0);
        private Semaphore semaforoPapel = new Semaphore(0);
        private Semaphore semaforoCerillas = new Semaphore(0);

        public Semaphore getSemaforoTabaco() {
            return semaforoTabaco;
        }

        public void setSemaforoTabaco(Semaphore semaforoTabaco) {
            this.semaforoTabaco = semaforoTabaco;
        }

        public Semaphore getSemaforoPapel() {
            return semaforoPapel;
        }

        public void setSemaforoPapel(Semaphore semaforoPapel) {
            this.semaforoPapel = semaforoPapel;
        }

        public Semaphore getSemaforoCerillas() {
            return semaforoCerillas;
        }

        public void setSemaforoCerillas(Semaphore semaforoCerillas) {
            this.semaforoCerillas = semaforoCerillas;
        }
    }

    private final Control control = new Control();

    public class Estudiante implements Runnable {
        private int iTabaco;
        private int iPapel;
        private int iCerilla;
        private int iId;
        private int iCigarsFumados = 0;
        private boolean cigarListo = false;

        public Estudiante(int iId) {
            setiId(iId);
        }

        public int getiTabaco() {
            return iTabaco;
        }

        public void setiTabaco(int iTabaco) {
            this.iTabaco = iTabaco;
        }

        public int getiPapel() {
            return iPapel;
        }

        public void setiPapel(int iPapel) {
            this.iPapel = iPapel;
        }

        public int getiCerilla() {
            return iCerilla;
        }

        public void setiCerilla(int iCerilla) {
            this.iCerilla = iCerilla;
        }

        public int getiCigarsFumados() {
            return iCigarsFumados;
        }

        public void setiCigarsFumados(int iCigarsFumados) {
            this.iCigarsFumados = iCigarsFumados;
        }

        public boolean isCigarListo() {
            return cigarListo;
        }

        public void setCigarListo(boolean cigarListo) {
            this.cigarListo = cigarListo;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public synchronized void run() {
            setiCerilla(numAleatorio(0, 4));
            setiPapel(numAleatorio(0, 4));
            setiTabaco(numAleatorio(0, 4));
            System.out.println("El estudiante " + getiId() + " se quiere fumar un cigarro. Tiene los siguientes ingredientes, Tabaco: " + getiTabaco() + ", Papel: " + getiPapel() + ", Cerillas: " + getiCerilla() + ".");
            do {
                setCigarListo(false);
                try {

                    if (getiTabaco() == 0) {
                        if (control.semaforoTabaco.availablePermits() != 0) {
                            control.semaforoTabaco.acquire();
                            System.out.println("\tEl estudiante " + getiId() + " ha cogido tabaco.");
                            setiTabaco(getiTabaco() + 1);
                            System.out.println("El estudiante " + getiId() + " tiene los siguientes ingredientes, Tabaco: " + getiTabaco() + ", Papel: " + getiPapel() + ", Cerillas: " + getiCerilla() + ".");
                        }
                    }
                    if (getiPapel() == 0) {
                        if (control.semaforoPapel.availablePermits() != 0) {
                            control.semaforoPapel.acquire();
                            System.out.println("\tEl estudiante " + getiId() + " ha cogido papel.");
                            setiPapel(getiPapel() + 1);
                            System.out.println("El estudiante " + getiId() + " tiene los siguientes ingredientes, Tabaco: " + getiTabaco() + ", Papel: " + getiPapel() + ", Cerillas: " + getiCerilla() + ".");
                        }
                    }
                    if (getiCerilla() == 0) {
                        if (control.semaforoCerillas.availablePermits() != 0) {
                            control.semaforoCerillas.acquire();
                            System.out.println("\tEl estudiante " + getiId() + " ha cogido cerillas.");
                            setiCerilla(getiCerilla() + 1);
                            System.out.println("El estudiante " + getiId() + " tiene los siguientes ingredientes, Tabaco: " + getiTabaco() + ", Papel: " + getiPapel() + ", Cerillas: " + getiCerilla() + ".");
                        }
                    }

                    if (getiTabaco() > 0 && getiCerilla() > 0 && getiPapel() > 0)
                        cigarListo = true;

                    if (cigarListo) {
                        System.out.println("\t\tEl estudiante " + getiId() + " se va a fumar un cigar.");
                        setiTabaco(getiTabaco() - 1);
                        setiPapel(getiPapel() - 1);
                        setiCerilla(getiCerilla() - 1);
                        iCigarsFumados++;
                        System.out.println("El estudiante " + getiId() + " tiene ahora lo siguiente -> Tabaco: " + getiTabaco() + ", Papel: " + getiPapel() + ", Cerillas: " + getiCerilla() + ".");
                        System.out.println("\tEl estudiante " + getiId() + " se ha fumado " + iCigarsFumados);
                    }

                    Thread.sleep(1500);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (iCigarsFumados < 10);
            System.err.println("El estudiante " + getiId() + " ya se ha fumado los 10 cigarros.");
        }
    }

    public class Malboro implements Runnable {
        @Override
        public synchronized void run() {
            String salida = "";
            do {
                if (control.semaforoTabaco.availablePermits() == 0 && control.semaforoCerillas.availablePermits() == 0 && control.semaforoPapel.availablePermits() == 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Malboro va a reponer las mesas");
                    int iNum1 = numAleatorio(1, 3);
                    int iNum2 = numAleatorio(1, 3);
                    while (iNum1 == iNum2) {
                        iNum1 = numAleatorio(1, 3);
                        iNum2 = numAleatorio(1, 3);
                    }
                    if ((iNum1 == TABACO || iNum1 == CERILLA) && (iNum2 == CERILLA || iNum2 == TABACO)) {
                        control.semaforoTabaco.release();
                        control.semaforoCerillas.release();
                        salida = "Tabaco y cerillas";
                    } else if ((iNum1 == TABACO || iNum1 == PAPEL) && (iNum2 == PAPEL || iNum2 == TABACO)) {
                        control.semaforoTabaco.release();
                        control.semaforoPapel.release();
                        salida = "Tabaco y papel";
                    } else if ((iNum1 == PAPEL || iNum1 == CERILLA) && (iNum2 == CERILLA || iNum2 == PAPEL)) {
                        control.semaforoPapel.release();
                        control.semaforoCerillas.release();
                        salida = "Cerilla y papel";
                    }
                    System.out.println("Malboro ha puesto en las mesas los siguientes ingredientes: " + salida);
                }

            } while (true);
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        new Thread(new Malboro()).start();
        int iContador = 0;

        while (true) {
            Thread.sleep(5000);
            new Thread(new Estudiante(iContador)).start();
            iContador++;
        }
    }

    public static void main(String[] args) {
        try {
            B3_P3 b3Ep3 = new B3_P3();
            b3Ep3.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private int numAleatorio(int iMin, int iMax) {
        return (int) (iMin + Math.random() * iMax);
    }
}
