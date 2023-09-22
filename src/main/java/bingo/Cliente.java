package bingo;
import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;
import java.util.ArrayList;

public class Cliente extends Thread{
    private String nombre;
    private ArrayList<Integer> carton = new ArrayList<>();
    public Cliente(String nombre) {
        this.nombre = nombre;
    }

    private synchronized void crearCarton(){
        // Cogemos 15 números aleatorios del ArrayList sin repetir
        for (int i = 0; i < 15; i++) {
            int randomInt = (int) (Math.random() * Main.numerosCompartidos.size());
            //System.out.println("Random: " + randomInt);
            this.carton.add(Main.numerosCompartidos.get(randomInt));
            Main.numerosCompartidos.remove(randomInt);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Devolvemos los números al ArrayList
        for (int i: this.carton) {
            Main.numerosCompartidos.add(i);
        }
    }

    @Override
    public void run(){
        this.crearCarton();
        int contador = 0;
        MulticastSocket socket = null;
        ArrayList<Integer> bolas = new ArrayList<>();
        try{
            InetAddress grupo = InetAddress.getByName("225.0.0.100"); // grupo multicast
            socket = new MulticastSocket(6789); // socket abierto en el puerto 6789
            socket.joinGroup(grupo); // se une al grupo
            while(true){
                byte[] buf = new byte[4]; // 4 bytes para el entero
                DatagramPacket mensaje = new DatagramPacket(buf, buf.length);
                //System.out.println("Esperando mensaje...");
                socket.receive(mensaje);
                // Comprobamos si el carton contiene al numero recibido
                String valor = new String(mensaje.getData());
                // Al hacer esta conversión hay dos caracteres extranos al final de la cadena
                // Por eso se recorre la cadena y se eliminan los caracteres nulos
                for (int i = 0; i < valor.length(); i++) {
                    char c = valor.charAt(i);
                    int asciiValue = (int) c;
                    if (asciiValue == 0){
                        valor = valor.substring(0, i);
                        break;
                    }
                }
                if (bolas.contains(Integer.parseInt(valor))){
                    System.out.println("La bola " + valor + " ya ha salido!!!!!!!!!!");
                }
                bolas.add(Integer.parseInt(valor));
                if (this.carton.contains(Integer.parseInt(valor))){
                    System.out.println("El cartón de " + this.nombre + " contiene el número " + valor);
                    contador++;
                    if (contador == 15){
                        System.out.println("El cartón de " + this.nombre + " ha cantado bingo");
                        break;
                    }
                }
                System.out.println("Recibido: " + valor.toString());
            }
            socket.leaveGroup(grupo);
        }catch (SocketException e){ // Excepción de socket
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){ // Excepción de entrada/salida
            System.out.println("IO: " + e.getMessage());
        }catch (NumberFormatException e){
            System.out.println("Error: " + e.getMessage());
        }
        finally { // Se cierra el socket
            if (socket != null){
                socket.close();
            }
        }
        System.out.println("El cliente ha terminado");
    }
}





