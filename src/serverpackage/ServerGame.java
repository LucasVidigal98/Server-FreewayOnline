/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucas F. Vidigal
 */
public class ServerGame {
    
    public static int numberConnections = 0;
    public static int portBase = 10000;
    public Integer[][] pos = new Integer[4][4];
   
    public class ThreadListen extends Thread{
        
        public int id;
        
        @Override
        public void run(){
            ServerSocket server = null;
            int port = 0;
            
            if (id == 0) port = ServerGame.portBase+200;
            else if (id == 1) port = ServerGame.portBase+201;
            else if (id == 2) port = ServerGame.portBase+202;
            else if (id == 3) port = ServerGame.portBase+203;
            
             try {
               server = new ServerSocket(port);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println(" * Servidor conectado na porta " + port);
            
            while(true){
                Socket client = null;
                ObjectInputStream io = null;
                
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    io = new ObjectInputStream(client.getInputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    pos[this.id] = (Integer[])io.readObject();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.println(pos[this.id][0]);
                System.out.println(pos[this.id][1]);
                System.out.println(pos[this.id][2]);
                
            }
        }
    }
    
    public class ThreadSend extends Thread{
        
        public int id;
        
        @Override
        public void run(){
            ServerSocket server = null;
            int port = 0;
            
            if (id == 0) port = ServerGame.portBase+100;
            else if (id == 1) port = ServerGame.portBase+101;
            else if (id == 2) port = ServerGame.portBase+102;
            else if (id == 3) port = ServerGame.portBase+103;
            
            try {
               server = new ServerSocket(port);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println(" - Servidor conectado na porta " + port);
            
            while(true){
                Socket client = null;
                boolean play = false;
                
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println("Cliente conectado " + client.getInetAddress() + " Porta " + port);
                ObjectOutputStream io = null;
                
                try {
                    io = new ObjectOutputStream(client.getOutputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    io.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(numberConnections >= 3){
                    play = true;
                    try {
                       io.writeObject(play);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                }else{
                    try {
                       io.writeObject(play);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                }
                
                try {
                    io.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(play) break;
                
            }
            
            System.out.println("Começa o jogo");
            ThreadListen t = new ThreadListen();
            t.id = this.id;
            t.start();
          
            try {
               server = new ServerSocket(port+200);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println(" + Servidor conectado na porta " + (port+200));
            
            while(true){
                Socket client = null;
                boolean play = false;
                
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println("Cliente conectado " + client.getInetAddress() + " Porta " + port);
                ObjectOutputStream io = null;
                
                try {
                    io = new ObjectOutputStream(client.getOutputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    io.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    io.writeObject(pos);
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    io.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }  
            }
        }    
    }
    
    public class ThreadConnection extends Thread{
        
        public int id;
        
        @Override
        public void run(){
            
            ServerSocket server = null;
            int port = 0;
            if (id == 0) port = ServerGame.portBase;
            else if (id == 1) port = ServerGame.portBase+1;
            else if (id == 2) port = ServerGame.portBase+2;
            else if (id == 3) port = ServerGame.portBase+3;
            
            try {
               server = new ServerSocket(port);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Servidor conectado na porta " + port);
            
            //while(true){
                Socket client = null;
                
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println("Cliente conectado " + client.getInetAddress() + " Porta " + port);
                ObjectOutputStream io = null;
                
                try {
                    io = new ObjectOutputStream(client.getOutputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    io.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                   io.writeObject(numberConnections);
                   numberConnections++;
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
                try {
                    io.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                ThreadSend t = new ThreadSend();
                t.id = this.id;
                t.start();
               // break;
            //}
        }
    }
    
    public void init(){
        
        ThreadConnection t1 = new ThreadConnection();
        t1.id = 0;
        t1.start();
        
        ThreadConnection t2 = new ThreadConnection();
        t2.id = 1;
        t2.start();
        
        ThreadConnection t3 = new ThreadConnection();
        t3.id = 2;
        t3.start();
        
        ThreadConnection t4 = new ThreadConnection();
        t4.id = 3;
        t4.start();
    }
    
    public static void main(String[] args) {
        
        new ServerGame().init();
        
    }
}
