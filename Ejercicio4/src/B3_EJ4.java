import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_EJ4 {
    private final byte NUM_SILLAS = 10;

    public class Control {
        private Semaphore semaforoSillas = new Semaphore(NUM_SILLAS);
        private Semaphore semaforoPaciente = new Semaphore(0);
        private Semaphore semaforoDentista = new Semaphore(1);
        private Queue<Paciente> colaPacientes = new LinkedList<Paciente>();

        public Semaphore getSemaforoSillas() {
            return semaforoSillas;
        }

        public void setSemaforoSillas(Semaphore semaforoSillas) {
            this.semaforoSillas = semaforoSillas;
        }

        public Semaphore getSemaforoPaciente() {
            return semaforoPaciente;
        }

        public void setSemaforoPaciente(Semaphore semaforoPaciente) {
            this.semaforoPaciente = semaforoPaciente;
        }

        public Semaphore getSemaforoDentista() {
            return semaforoDentista;
        }

        public void setSemaforoDentista(Semaphore semaforoDentista) {
            this.semaforoDentista = semaforoDentista;
        }

        public Queue<Paciente> getColaPacientes() {
            return colaPacientes;
        }

        public void setColaPacientes(Queue<Paciente> colaPacientes) {
            this.colaPacientes = colaPacientes;
        }
    }

    private Control control = new Control();

    public class Dentista implements Runnable {

        @Override
        public void run() {
            int idPaciente = 0;
            Paciente p = new Paciente(idPaciente);
            System.out.println("Hay " + NUM_SILLAS + " sillas libres");
            System.out.println("El dentista esta libre.");
            do {

                    control.semaforoSillas.release();

                    try {
                        control.semaforoDentista.acquire();
                        control.semaforoPaciente.acquire();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    int iPos = control.colaPacientes.poll().getiId();
                    System.out.println("El dentista ha llamado al paciente " + iPos);
                    System.out.println("El dentista esta ocupado.");
                    System.out.println("La silla ocupada por " + iPos + " esta libre.");



                    try {
                        Thread.sleep((int) (Math.random() * 10) * 1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    System.out.println("El paciente " + iPos + " ha salido de la consulta.");
                    System.out.println("El dentista esta libre de nuevo.");

                    control.semaforoDentista.release();


            } while (true);

        }

    }

    public class Paciente implements Runnable {
        private int iId;

        public Paciente(int iId) {
            setiId(iId);
        }

        public int getiId() {
            return iId;
        }

        public void setiId(int iId) {
            this.iId = iId;
        }

        @Override
        public synchronized void run() {

            if (control.colaPacientes.size() >= NUM_SILLAS) {
                System.out.println("El paciente " + getiId() + " se va enfadado, por que la sala de espera estaba llena.");
            } else {
                try {
                    control.semaforoSillas.acquire();
                    System.out.println("El paciente " + getiId() + " se ha sentado en una silla.");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                control.colaPacientes.add(this);
                control.semaforoPaciente.release();
            }
        }
    }


    private void executeMultiThreading() throws InterruptedException {
        int iContador = 0;

        new Thread(new Dentista()).start();

        while (true) {
            Thread.sleep(500);
            new Thread(new Paciente(iContador)).start();
            iContador++;
        }
    }

    public static void main(String[] args) {
        try {
            B3_EJ4 b3Ej4 = new B3_EJ4();
            b3Ej4.executeMultiThreading();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
