import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class B3_EJ4 {
    private final byte NUM_SILLAS = 10;

    public class Control {
        private Semaphore semaforoSillas = new Semaphore(NUM_SILLAS);
        private Semaphore semaforoPaciente = new Semaphore(0);
        private Semaphore semaforoDentista = new Semaphore(0);
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

            System.out.println("Las " + NUM_SILLAS + " sillas de la consulta están libres.");


            while (true) {

                if (control.colaPacientes.size() == 0) {
                    System.out.println("El dentista está descansando.");

                    try {
                        control.semaforoDentista.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {

                    int iPaciente = control.colaPacientes.poll().getiId();

                    System.out.println("El paciente " + iPaciente + " avisa al dentista para atenderle.");
                    control.semaforoSillas.release();

                    try {
                        control.semaforoPaciente.acquire();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("El dentista atiende al paciente " + iPaciente + ".");
                    System.out.println("El dentista está tratando a un paciente.");
                    System.out.println("El paciente " + iPaciente + " deja su silla libre.");

                    try {
                        Thread.sleep((int) (Math.random() * 10) * 1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (iPaciente % 2 == 0) {
                        int iNumEmpastes = (int) (1+ (Math.random() * 32));

                        System.out.println("El paciente " + iPaciente + " ha sido atendido y sale del dentista. Con " + iNumEmpastes + " empastes");
                    }else {
                        System.out.println("El paciente " + iPaciente + " ha sido atendido. No ha sido bueno, le han castigado. Por que dice que el \"Bicho\" no existe.");
                    }


                    System.out.println("El dentista puede atender a un nuevo paciente.");
                }
            }
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
        public void run() {
            if (control.colaPacientes.size() >= NUM_SILLAS) {
                System.out.println("El paciente " + getiId() + " se va enfadado, por que no quedan sillas libres.");

            } else {

                try {
                    control.semaforoSillas.acquire();
                    System.out.println("El paciente " + getiId() + " se ha sentado en una silla.");
                    control.semaforoDentista.release();
                } catch (InterruptedException e) {
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
