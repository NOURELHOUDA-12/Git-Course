import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

public class ChatMulticastClient {
    static InetAddress group;
    static int port;
    String nom;
    MulticastSocket s;

    public ChatMulticastClient(InetAddress group, int port, String nom) {
        this.group = group;
        this.port = port;
        this.nom = nom;
        try {
            s = new MulticastSocket(port);
            s.joinGroup(group);
            //lancer les deux threads d'emission et de reception
            new Emetteur(s).start();
            new Recepteur(s).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public class Emetteur extends Thread {
        MulticastSocket s;
        Emetteur(MulticastSocket s){
            this.s =s;
        }
        public void run() {
            emettre();
        }

        public void emettre() {
            byte[] bufferOut = new byte[1024];
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Entrez un message");
                String message = in.readLine();
                message = "Client : " + nom + " a dit " + message;
                bufferOut = message.getBytes();
                DatagramPacket p = new DatagramPacket(bufferOut, bufferOut.length, ChatMulticastClient.group, ChatMulticastClient.port);
                s.send(p);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class Recepteur extends Thread {
        MulticastSocket s;
        Recepteur(MulticastSocket s){
            this.s =s;
        }
        public void run(){
            // En ecoute permanante des messages
            while(true) {
                recevoir();
            }
        }
        public void recevoir(){
            byte [] bufferIn = new byte[1024];
            try {
                DatagramPacket packet = new DatagramPacket(bufferIn,bufferIn.length);
                s.receive(packet);
                System.out.println(new String(packet.getData()));
                // Si je recupere un message je relance la reponse 
                try {
                    Thread.sleep(1000);
                    new Thread(new Emetteur(s)).start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        try {
            InetAddress group = InetAddress.getByName("239.255.80.84");
            String name = args[0];
            int port = 5000;
            new ChatMulticastClient(group,port,name);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
