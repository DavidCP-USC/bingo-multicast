package bingo;
import java.util.ArrayList;
public class Main {
    public static ArrayList<Integer> numerosCompartidos = new ArrayList<>();
    public static void main(String[] args) {
        // Lo rellenamos con los valores posibles para cada carton
        // ArrayList<Integer> valores = new ArrayList<>();
        //Thread cliente = new Cliente("Cliente 1");
        //cliente.start();
        
        
        // Generar un número aleatorio entre 1 y 99
        // int randomInt = (int) (Math.random() * 98) + 1;
        // System.out.println("Número aleatorio: " + randomInt);


        // Llenar el ArrayList con números del 1 al 10
        for (int i = 1; i <= 90; i++) {
            numerosCompartidos.add(i);
        }

        // Crear varios hilos que operarán sobre el ArrayList compartido
        Thread cliente = new Cliente("Hilo cliente");
        Thread servidor = new Servidor("Hilo servidor");
        cliente.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        servidor.start();


    }
}