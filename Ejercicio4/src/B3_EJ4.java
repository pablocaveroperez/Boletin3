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

    public class Paciente implements Runnable {
        private int iId;

        public Paciente(int iId) {
            this.iId = iId;
        }

        @Override
        public synchronized void run() {

        }
    }

    public class Dentista implements Runnable {
        @Override
        public synchronized void run() {

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
