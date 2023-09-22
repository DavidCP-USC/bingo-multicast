package bingo;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Servidor extends Thread{
    private String nombre;

    public Servidor(String nombre) {
        super(nombre);
    }

    private synchronized int sacarBolas(){
        int randomInt = (int) (Math.random() * Main.numerosCompartidos.size());
        int bola = Main.numerosCompartidos.remove(randomInt);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bola;
    
    }

    @Override
    public void run() {
        int bola;
        MulticastSocket socket = null; // socket para enviar
        String cadena = new String(); // se convierte el entero a cadena
        ArrayList<Integer> bolas = new ArrayList<>();
        
        try{
            InetAddress grupo = InetAddress.getByName("225.0.0.100"); // grupo multicast
            socket = new MulticastSocket(6789); // socket abierto en el puerto 6789
            socket.joinGroup(grupo); // se une al grupo
            // while (true){
            //System.out.println("Servidor inicia el juego");
            for(int i = 0; i < 90; i++){
                bola = this.sacarBolas(); // Sacamos una bola
                //System.out.println(bola);
                if (bolas.contains(bola)){
                    //System.out.println("La bola " + bola + " ya ha salido");
                }
                bolas.add(bola);
                Thread.sleep(1000); // Esperamos 1 segundo
                cadena = String.valueOf(bola); // se convierte el entero a cadena
                byte[] buf = cadena.getBytes(); // 4 bytes para el entero
                DatagramPacket mensaje = new DatagramPacket(buf, buf.length, grupo, 6789);
                //System.out.println("Enviando mensaje...");
                socket.send(mensaje);
                //System.out.println("Enviado: " + mensaje.getData());
            }
            socket.leaveGroup(grupo);
        }catch (SocketException e){ // Excepción de socket
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){ // Excepción de entrada/salida
            System.out.println("IO: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        finally { // Se cierra el socket
            if (socket != null){
                socket.close();
            }
        }
        System.out.println("El servidor ha terminado");
    }
}
