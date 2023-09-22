package bingo;

public class Servidor extends Thread{
    private String nombre;

    public Servidor(String nombre) {
        super(nombre);
    }

    private synchronized int sacarBolas(){
        int bola=0;
        for (int i = 0; i < 15; i++) {
            int randomInt = (int) (Math.random() * Main.numerosCompartidos.size());
            bola = Main.numerosCompartidos.get(randomInt);
            Main.numerosCompartidos.remove(randomInt);
        }
        return bola;
    }

    @Override
    public void run() {
        int bola = this.sacarBolas();

    }
    
}
