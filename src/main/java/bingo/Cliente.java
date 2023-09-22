package bingo;

public class Cliente extends Thread{
    private String nombre;
    private int[] carton = new int[15];
    public Cliente(String nombre) {
        this.nombre = nombre;
    }

    private synchronized void crearCarton(){
        // Cogemos 15 números aleatorios del ArrayList sin repetir
        for (int i = 0; i < 15; i++) {
            int randomInt = (int) (Math.random() * Main.numerosCompartidos.size());
            this.carton[i] = Main.numerosCompartidos.get(randomInt);
            Main.numerosCompartidos.remove(randomInt);
        }
        // Devolvemos los números al ArrayList
        for (int i: this.carton) {
            Main.numerosCompartidos.add(i);
        }
    }

    @Override
    public void run() {
        this.crearCarton();
        // Imprimimos el cartón
        for(int i: carton){
            System.out.println(i);
        }

    }
}





