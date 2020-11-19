import java.util.concurrent.Semaphore;

public class B3_P3 {
    private final byte ESTUDIANTES = 3;
    private final byte PAPEL = 1;
    private final byte CERILLA = 2;
    private final byte TABACO = 3;

    public class Control {
        private Semaphore semaforoMalboro = new Semaphore(0);
        private Semaphore semaforoEstudiante = new Semaphore(0);
        private volatile int iIngrediente;
        private volatile int iId;
        private Semaphore hayTabaco = new Semaphore(1);
        private Semaphore hayPapel = new Semaphore(1);
        private Semaphore hayCerillas = new Semaphore(1);
        private volatile boolean cigarListo = false;

        public boolean isCigarListo() {
            return cigarListo;
        }

        public void setCigarListo(boolean cigarListo) {
            this.cigarListo = cigarListo;
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

        public String ingredienteToString() {
            String salida = null;
            if (iIngrediente == PAPEL)
                salida = "Papel";
            else if (iIngrediente == CERILLA)
                salida = "Cerilla";
            else
                salida = "Tabaco";
            return salida;
        }

        private void nadaDisponible() {
            setHayPapel(false);
            setHayTabaco(false);
            setHayCerillas(false);
        }
    }

    private final Control control = new Control();

    public class Estudiante implements Runnable {
        private int iIngrediente;
        private int iId;
        private int iCigarsFumados = 0;

        public Estudiante(int iId, int iIngrediente) {
            setiId(iId);
            setiIngrediente(iIngrediente);
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
                try {
                    control.semaforoMalboro.acquire();
                    control.setiIngrediente(getiIngrediente());
                    control.setiId(getiId());

                    control.semaforoEstudiante.acquire();
                    System.out.println("El estudiante " + control.getiId() + " se quiere fumar un cigarro. Tiene el siguiente ingrediente: " + control.ingredienteToString());

                    if (control.cigarListo) {
                        System.out.println("El estudiante " + control.getiId() + " se va a fumar un cigar.");
                        iCigarsFumados++;
                        System.out.println("En la mesa de malboro pasa a haber los siguientes ingredientes: " + control.reponer());
                    }
                    System.out.println("El estudiante " + control.getiId() + " se va a dar una vuelta. Dice que ahora vuelve");
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



                control.setCigarListo(false);

                System.out.println("El estudiante " + control.getiId() + " va a por los ingredientes restantes.");
                if (control.getiIngrediente() == TABACO) {
                    if (control.isHayCerillas() && control.isHayPapel()) {
                        System.out.println("El estudiante " + control.getiId() + " coge los ingredientes que le faltaban");
                        control.setCigarListo(true);
                    } else
                        System.out.println("Los ingredientes que el estudiante " + control.getiId() + " necesita no estan en la mesa.");

                }else if (control.getiIngrediente() == CERILLA) {
                    if (control.isHayPapel() && control.hayTabaco) {
                        System.out.println("El estudiante " + control.getiId() + " coge los ingredientes que le faltaban");
                        control.setCigarListo(true);
                    } else
                        System.out.println("Los ingredientes que el estudiante " + control.getiId() + " necesita no estan en la mesa.");
                }else if (control.getiIngrediente() == PAPEL) {
                    if (control.hayTabaco && control.hayCerillas) {
                        System.out.println("El estudiante " + control.getiId() + " coge los ingredientes que le faltaban");
                        control.setCigarListo(true);
                    } else
                        System.out.println("Los ingredientes que el estudiante " + control.getiId() + " necesita no estan en la mesa.");
                }else
                    System.err.println("Parece que algo no ha ido bien");
                control.semaforoEstudiante.release();
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
