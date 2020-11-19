import java.util.concurrent.Semaphore;

public class B3_P3 {
    private final byte ESTUDIANTES = 3;
    private final byte PAPEL = 1;
    private final byte CERILLA = 2;
    private final byte TABACO = 3;

    public class Control {
        private Semaphore semaforoMalboro = new Semaphore(0);
        private Semaphore semaforoEstudiante = new Semaphore(0);
        private Semaphore semaforoTabaco = new Semaphore(1);
        private Semaphore semaforoPapel = new Semaphore(1);
        private Semaphore semaforoCerillas = new Semaphore(1);

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

        public Semaphore getSemaforoMalboro() {
            return semaforoMalboro;
        }

        public void setSemaforoMalboro(Semaphore semaforoMalboro) {
            this.semaforoMalboro = semaforoMalboro;
        }

        public Semaphore getSemaforoEstudiante() {
            return semaforoEstudiante;
        }

        public void setSemaforoEstudiante(Semaphore semaforoEstudiante) {
            this.semaforoEstudiante = semaforoEstudiante;
        }

        public String ingredienteToString(int iIngrediente) {
            String salida = null;
            if (iIngrediente == PAPEL)
                salida = "Papel";
            else if (iIngrediente == CERILLA)
                salida = "Cerilla";
            else
                salida = "Tabaco";
            return salida;
        }

        public String reponer() {
            String salida = null;
            int iNum1 = numAleatorio();
            int iNum2 = numAleatorio();
            while (iNum1 == iNum2) {
                iNum1 = numAleatorio();
                iNum2 = numAleatorio();
            }

            if ((iNum1 == TABACO || iNum1 == CERILLA) && (iNum2 == CERILLA || iNum2 == TABACO)) {
                semaforoTabaco.release();
                semaforoCerillas.release();
                salida = "Tabaco y cerillas";
            }else if ((iNum1 == TABACO || iNum1 == PAPEL) && (iNum2 == PAPEL || iNum2 == TABACO)) {
                semaforoTabaco.release();
                semaforoPapel.release();
                salida = "Tabaco y papel";
            }else if ((iNum1 == PAPEL || iNum1 == CERILLA) && (iNum2 == CERILLA || iNum2 == PAPEL)) {
                semaforoPapel.release();
                semaforoCerillas.release();
                salida = "Cerilla y papel";
            }else
                salida = "Sucki Sucki";
            return salida;
        }
    }

    private final Control control = new Control();

    public class Estudiante implements Runnable {
        private int iIngrediente;
        private int iId;
        private int iCigarsFumados = 0;
        private boolean cigarListo = false;

        public Estudiante(int iId, int iIngrediente) {
            setiId(iId);
            setiIngrediente(iIngrediente);
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

        public int getiIngrediente() {
            return iIngrediente;
        }

        public void setiIngrediente(int iIngrediente) {
            this.iIngrediente = iIngrediente;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public void run() {
            do {
                setCigarListo(false);
                try {
                    control.semaforoMalboro.acquire();

                    control.semaforoEstudiante.acquire();
                    System.out.println("El estudiante " + getiId() + " se quiere fumar un cigarro. Tiene el siguiente ingrediente: " + control.ingredienteToString(getiIngrediente()));

                    if (getiIngrediente() == TABACO) {
                        control.semaforoPapel.acquire();
                        System.out.println("El estudiante " + getiId() + " ha cogido papel.");
                        control.semaforoCerillas.acquire();
                        System.out.println("El estudiante " + getiId() + " ha cogido cerillas.");
                        setCigarListo(true);
                    }else if (getiIngrediente() == PAPEL) {
                        control.semaforoCerillas.acquire();
                        System.out.println("El estudiante " + getiId() + " ha cogido cerilla.");
                        control.semaforoTabaco.acquire();
                        System.out.println("El estudiante " + getiId() + " ha cogido tabaco.");
                        setCigarListo(true);
                    }else if (getiIngrediente() == CERILLA) {
                        control.semaforoPapel.acquire();
                        System.out.println("El estudiante " + getiId() + " ha cogido papel.");
                        control.semaforoTabaco.acquire();
                        System.out.println("El estudiante " + getiId() + " ha cogido tabaco.");
                        setCigarListo(true);
                    }

                    if (cigarListo) {
                        System.out.println("El estudiante " + getiId() + " se va a fumar un cigar.");
                        iCigarsFumados++;
                    }
                    System.out.println("El estudiante " + getiId() + " se va a dar una vuelta. Dice que ahora vuelve");
                    Thread.sleep(5000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(iCigarsFumados < 10);
        }
    }

    public class Malboro implements Runnable {
        @Override
        public void run() {
            System.out.println("Malboro ha puesto en las mesas los siguientes ingredientes: " + control.reponer());
            do {

                control.semaforoMalboro.release();



                control.semaforoEstudiante.release();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Malboro ha puesto en las mesas los siguientes ingredientes: " + control.reponer());

            }while(true);
        }
    }

    private void executeMultiThreading() throws InterruptedException {
        new Thread(new Malboro()).start();

        for (int i = 0; i < ESTUDIANTES; i++) {
            int iAleatorio = numAleatorio();
            new Thread(new Estudiante(i, iAleatorio)).start();
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

    private int numAleatorio() {
        return (int) (1 + Math.random() * 3);
    }
}
