import java.util.concurrent.Semaphore;

public class B3_EJ2 {
    private final int NUM_PALILLOS = 5;
    private final int NUM_ESTUDIANTES = 5;

    public class Control {
        private Semaphore[] aSemaforo = new Semaphore[NUM_PALILLOS];

        public Control() {
            for (int i = 0; i < NUM_PALILLOS; i++)
                aSemaforo[i] = new Semaphore(1);
        }

        public Semaphore[] getaSemaforo() {
            return aSemaforo;
        }

        public void setaSemaforo(Semaphore[] aSemaforo) {
            this.aSemaforo = aSemaforo;
        }
    }

    private Control control = new Control();

    public class Filosofo implements Runnable {
        private int iId;
        private int iDerecha;
        private int iIzquierda;

        public Filosofo(int iId) {
            this.iId = iId;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        public boolean palillosAvaliable() {
            boolean bExito = false;

            iIzquierda = getiId();
            if (iIzquierda == 4)
                iDerecha = 0;
            else
                iDerecha = iIzquierda + 1;

            if (control.aSemaforo[iIzquierda].availablePermits() > 0 && control.aSemaforo[iDerecha].availablePermits() > 0) {
                bExito = true;
                try {
                    control.aSemaforo[iIzquierda].acquire();
                    control.aSemaforo[iDerecha].acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return bExito;
        }

        public boolean soltarPalillos() {
            boolean bExito = false;

            if (control.aSemaforo[iIzquierda].availablePermits() == 0 && control.aSemaforo[iDerecha].availablePermits() == 0) {
                bExito = true;
                control.aSemaforo[iIzquierda].release();
                control.aSemaforo[iDerecha].release();
            }
            return bExito;
        }

        @Override
        public synchronized void run() {
            System.out.println("El filósofo " + getiId() + " se ha sentado en el parque. No le teme a los \"Bichos\"");
            do {
                if (palillosAvaliable()) {
                    System.out.println("El filósofo " + getiId() + " ha cogido los palillos " + iIzquierda + " y " + iDerecha + ", y se dispone a comer.");
                    try {
                        Thread.sleep(2000);

                        System.out.println("El filósofo " + getiId() + " ha terminado de comer, y ha soltado los palillos " + iIzquierda + " y " + iDerecha + ". Ahora va a pensar en el \"Bicho\"");

                        soltarPalillos();

                        Thread.sleep(6000);
                        System.out.println("El filósofo " + getiId() + " ha terminado de pensar en el \"Bicho\"");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }while (true);
        }
    }

    /*public class Palillo implements Runnable {
        private int iId;

        public Palillo(int iId) {
            this.iId = iId;
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public synchronized void run() {

            do {
                try {

                    control.semaphoreFilosofo.acquire();

                    int iBichos = (int) (Math.random() * 52);

                    int idFilosofo = control.colaFilosofos.poll().getiId();
                    if (iBichos == 1)
                        System.out.println("El filósofo " + idFilosofo + " está listo para comer. Le ha picado " + iBichos + " bicho");
                    else
                        System.out.println("El filósofo " + idFilosofo + " está listo para comer. Le han picado " + iBichos + " bichon");

                    int iIzquierda = (int) (Math.random() * 5);
                    int iDerecha;

                    if (iIzquierda == 4)
                        iDerecha = 0;
                    else
                        iDerecha = iIzquierda + 1;



                    control.semaforoPalillo.acquire();

                    control.aSemaforo[iIzquierda].acquire();
                    control.aSemaforo[iDerecha].acquire();


                    System.out.println("El filósofo " + idFilosofo + " ha cogido los palillos " + iIzquierda + " y " + iDerecha + ".");
                    System.out.println("El filósofo " + idFilosofo + " ha empezado a comer.");
                    Thread.sleep(2000);


                    control.aSemaforo[iIzquierda].release();
                    control.aSemaforo[iDerecha].release();

                    control.semaforoPalillo.release();

                    System.out.println("El filósofo " + idFilosofo + " ha terminado de comer. Y se dispone a pensar en el \"Bicho\"");
                    System.out.println("Los palillos " + iIzquierda + " y " + iDerecha + " estan libres.");




                    Thread.sleep(6000);
                    System.out.println("El filósofo " + idFilosofo + " ya ha dejado de pensar. Este cree en el \"Bicho\".");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }*/

    /**
     * Este metodo es el que vamos a utilizar para todos los programas de MultiThreading.
     * Sirve para lanzar los hilos correcpondientes.
     */
    private void executeMultiThreading() throws InterruptedException {

        for (int i = 0; i < NUM_ESTUDIANTES; i++)
            new Thread(new Filosofo(i)).start();
    }

    public static void main(String[] args) {
        try {
            B3_EJ2 b3Ej2 = new B3_EJ2();
            b3Ej2.executeMultiThreading();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
