package bingo;
import java.net.*;
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
        ArrayList<Integer> numerosDisponibles = new ArrayList<>(Main.numerosCompartidos);
        for (int i = 0; i < 15; i++) {
            int randomInt = (int) (Math.random() * numerosDisponibles.size());
            this.carton.add(numerosDisponibles.get(randomInt));
            numerosDisponibles.remove(randomInt);
        }
    }

    @Override
    public void run(){
        this.crearCarton();
        int contador = 0;
        ArrayList<Integer> bolas = new ArrayList<>();
        MulticastSocket socket = null;
        try{
            InetAddress grupo = InetAddress.getByName("225.0.0.100"); // grupo multicast
            socket = new MulticastSocket(6789);
            socket.joinGroup(grupo); // se une al grupo
            while(true){                
                byte[] buf = new byte[6]; // 4 bytes para el entero
                DatagramPacket mensaje = new DatagramPacket(buf, buf.length);
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

                // Si el mensaje es "Fin", se termina el juego. El fin lo envia el servidor
                if (valor.equals("Fin")){
                    break;
                }
                if (valor.equals("Bingo")){
                    continue;
                }

                bolas.add(Integer.parseInt(valor));
                if (this.carton.contains(Integer.parseInt(valor))){
                    // System.out.println("El cartón de " + this.nombre + " contiene el número " + valor);
                    contador++;
                    if (contador == 15){ // Si el contador llega a 15, el cliente ha ganado
                        System.out.println("El cliente " + this.nombre + " ha ganado el bingo");
                        String finString = "Bingo";
                        byte[] bufFinal = finString.getBytes(); // 4 bytes para el entero  
                        DatagramPacket mensajeFinal = new DatagramPacket(bufFinal, bufFinal.length, grupo, 6789);
                        socket.send(mensajeFinal);
                        break;
                    }
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





