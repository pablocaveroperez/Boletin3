import java.util.concurrent.Semaphore;

public class B3_P3 {
    private final byte ESTUDIANTES = 10;
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

        public String reponer() throws InterruptedException {
            String salida = null;
            int iNum1 = numAleatorio(1, 3);
            int iNum2 = numAleatorio(1, 3);
            while (iNum1 == iNum2) {
                iNum1 = numAleatorio(1, 3);
                iNum2 = numAleatorio(1, 3);
            }

            if ((iNum1 == TABACO || iNum1 == CERILLA) && (iNum2 == CERILLA || iNum2 == TABACO)) {
                semaforoTabaco = new Semaphore(1);
                semaforoCerillas = new Semaphore(1);
                semaforoPapel = new Semaphore(0);
                salida = "Tabaco y cerillas";
            } else if ((iNum1 == TABACO || iNum1 == PAPEL) && (iNum2 == PAPEL || iNum2 == TABACO)) {
                semaforoTabaco = new Semaphore(1);
                semaforoPapel = new Semaphore(1);
                semaforoCerillas = new Semaphore(0);
                salida = "Tabaco y papel";
            } else if ((iNum1 == PAPEL || iNum1 == CERILLA) && (iNum2 == CERILLA || iNum2 == PAPEL)) {
                semaforoPapel = new Semaphore(1);
                semaforoCerillas = new Semaphore(1);
                semaforoTabaco = new Semaphore(0);
                salida = "Cerilla y papel";
            } else
                salida = "Sucki Sucki";
            return salida;
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
        public void run() {
            setiCerilla(numAleatorio(0, 4));
            setiPapel(numAleatorio(0, 4));
            setiTabaco(numAleatorio(0, 4));
            do {
                setCigarListo(false);
                try {
                    System.out.println("El estudiante " + getiId() + " se quiere fumar un cigarro. Tiene los siguientes ingredientes, Tabaco: " + getiTabaco() + ", Papel: " + getiPapel() + ", Cerillas: " + getiCerilla() + ".");

                    if (getiTabaco() == 0) {
                        control.semaforoTabaco.acquire();
                        System.out.println("\tEl estudiante " + getiId() + " ha cogido tabaco.");
                        setiTabaco(getiTabaco() + 1);
                    }
                    if (getiPapel() == 0) {
                        control.semaforoPapel.acquire();
                        System.out.println("\tEl estudiante " + getiId() + " ha cogido papel.");
                        setiPapel(getiPapel() + 1);
                    }
                    if (getiCerilla() == 0) {
                        control.semaforoCerillas.acquire();
                        System.out.println("\tEl estudiante " + getiId() + " ha cogido cerillas.");
                        setiCerilla(getiCerilla() + 1);
                    }

                    if (getiTabaco() > 0 && getiCerilla() > 0 && getiPapel() > 0)
                        cigarListo = true;

                    if (cigarListo) {
                        System.out.println("\t\tEl estudiante " + getiId() + " se va a fumar un cigar.");
                        setiTabaco(getiTabaco() - 1);
                        setiPapel(getiPapel() - 1);
                        setiCerilla(getiCerilla() - 1);
                        iCigarsFumados++;
                        System.out.println("\tEl estudiante " + getiId() + " se ha fumado " + iCigarsFumados);
                    }
                    //System.out.println("El estudiante " + getiId() + " se va a dar una vuelta. Dice que ahora vuelve");
                    //Thread.sleep(1000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (iCigarsFumados < 10);
        }
    }

    public class Malboro implements Runnable {
        @Override
        public void run() {
            String salida = "";
            do {
                if (control.semaforoTabaco.availablePermits() == 0 && control.semaforoCerillas.availablePermits() == 0 && control.semaforoPapel.availablePermits() == 0) {
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

        for (int i = 0; i < ESTUDIANTES; i++) {
            new Thread(new Estudiante(i)).start();
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
