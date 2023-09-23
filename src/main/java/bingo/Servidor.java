package bingo;
import java.net.*;
import java.io.*;

public class Servidor extends Thread{
    public Servidor() {
        super();
    }

    private synchronized int sacarBolas(){
        int randomInt = (int) (Math.random() * Main.numerosCompartidos.size());
        int bola = Main.numerosCompartidos.remove(randomInt);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return bola;
    }

    @Override
    public void run() {
        int bola;
        MulticastSocket socket = null; // socket para enviar
        String cadena = new String(); // se convierte el entero a cadena
        try{
            InetAddress grupo = InetAddress.getByName("225.0.0.100"); // grupo multicast
            socket = new MulticastSocket(6789); // socket abierto en el puerto 6789
            socket.joinGroup(grupo); // se une al grupo
            socket.setSoTimeout(1000);

            for(int i = 0; i < 90; i++){

                bola = this.sacarBolas(); // Sacamos una bola
          
                // Imprimimos la bola que ha salido
                if (bola < 10){
                    System.out.println("Ha salido la bola: 0" + bola);
                }
                else{
                    System.out.println("Ha salido la bola: " + bola);
                }

                cadena = String.valueOf(bola); // se convierte el entero a cadena
                byte[] buf = cadena.getBytes(); // 4 bytes para el entero
                DatagramPacket mensaje = new DatagramPacket(buf, buf.length, grupo, 6789);
                socket.send(mensaje);

                // Comprobamos si algún cliente ha ganado
                try{
                    byte[] bufferRecepcion = new byte[6]; // 4 bytes para el entero
                    DatagramPacket mensajeRecibido = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
                    // Ponemos dos receive() para que se lea la bola y luego el mensaje de Bingo
                    socket.receive(mensajeRecibido);
                    socket.receive(mensajeRecibido);
                    // Comprobamos si alguien ha cantado bingo
                    String valor = new String(mensajeRecibido.getData());
                    valor = new String(mensajeRecibido.getData());
                    if (valor.contains("Bingo")){
                        System.out.println("El juego ha terminado");
                        String finString = "Fin";
                        byte[] bufFinal = finString.getBytes(); // 4 bytes para el entero
                        DatagramPacket mensajeFinal = new DatagramPacket(bufFinal, bufFinal.length, grupo, 6789);
                        socket.send(mensajeFinal);
                        break;
                    }
                }catch (SocketTimeoutException e){
                    // Si se produce un timeout, se continua la ejecución
                    continue;
                }
                
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
    }
}
