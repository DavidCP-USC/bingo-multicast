package bingo;
import java.util.ArrayList;
public class Main {
    public static ArrayList<Integer> numerosCompartidos = new ArrayList<>();
    public static Integer clientesSuscritos = 0;
    public static int numClientes = 2;
    public static void main(String[] args) {
        
        // Llenar el ArrayList con números del 1 al 10
        for (int i = 1; i <= 90; i++) {
            numerosCompartidos.add(i);
        }

        // Crear varios hilos que operarán sobre el ArrayList compartido
        for (int i = 1; i <= numClientes; i++) {
            Thread cliente = new Cliente("Hilo cliente " + i);
            cliente.start();
        }
        
        Thread servidor = new Servidor();
        servidor.start();


    }
}