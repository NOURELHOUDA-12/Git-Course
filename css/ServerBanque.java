/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbanque;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import data.*;
/**
 *
 * @author macbook
 */
public class ServerBanque extends Thread{

    public static ArrayList<Compte> list;
    
    public static void main(String[] args) {
	//lancer le thread main du serveur de la banque 
        
        new ServerBanque().start();
        list = new ArrayList<>();
        // Ajouter un ensemble de clients 
        list.add(new Compte("Yahya", "005", 10000));
        list.add(new Compte("Yakine", "003", 10000));
        list.add(new Compte("Youssef", "001", 10000));
    }

    @Override
    public void run() {
        try {
            int nbConnection = 0;
            ServerSocket ss = new ServerSocket(5000);
            while(true){
		// creer un socket main et attendre l'acceptation 
		Socket sk = ss.accept();
		++nbConnection;
		System.out.println("client "+ nbConnection + " connecté");
		// lancer le thread socket afin de gerer la communication avec un client et
		new ServerThreadSocket(sk).start();
		
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerBanque.class.getName()).
		log(Level.SEVERE, null, ex);
        }
        
    }
}
