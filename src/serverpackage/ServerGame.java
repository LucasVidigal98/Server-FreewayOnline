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
    public static int[] count = new int[4];
    public static int lose;
    
    public class ThreadWin extends Thread{
        
        public int id;
        
        public ThreadWin(int id){
            this.id = id;
        }
        
        @Override
        public void run(){
            
            lose = 0;
            count[0] = 0;
            count[1] = 0;
            count[2] = 0;
            count[3] = 0;
            
            boolean win = false, lost = false;
            int port = 0;
            int winner = 0;
            
            if (id == 0) port = ServerGame.portBase+400;
            else if (id == 1) port = ServerGame.portBase+401;
            else if (id == 2) port = ServerGame.portBase+402;
            else if (id == 3) port = ServerGame.portBase+403;
            
            ServerSocket server = null;
            
            try {
                server = new ServerSocket(port);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println(" & Servidor conectado na porta " + port);
            
            while(true){
                
                Socket client = null;
                ObjectOutputStream io = null;
                
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(count[0] >= 3){
                    win = true;
                    winner = 0;
                }else if(count[1] >= 3){
                    win = true;
                    winner = 1;
                }else if(count[2] >= 3){
                    win = true;
                    winner = 1;
                }else if(count[3] >= 3){
                    win = true;
                    winner = 3;
                }
                
                if(lose >= 4) lost = true;
                
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
                
                if(win){
                    
                    String w = "";
                    
                    if(winner == 0){
                        w = "A galinha barnca Venceu!";
                        try {
                            io.writeObject(w);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(winner == 1){
                        w = "A galinha azul Venceu!";
                        try {
                            io.writeObject(w);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(winner == 2){
                        w = "A galinha laranja Venceu!";
                        try {
                            io.writeObject(w);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(winner == 3){
                        w = "A galinha preta Venceu";
                        try {
                            io.writeObject(w);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        try {
                            io.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        pos[0][1] = 44;
                        pos[0][2] = 600;
                        pos[1][1] = 364;
                        pos[1][2] = 600;
                        pos[2][1] = 626;
                        pos[2][2] = 600;
                        pos[3][1] = 940;
                        pos[3][2] = 600; 
                        
                        //break;
                    }
                    
                }else{
                    
                    String w = "NULL";
                    
                    try {
                        io.writeObject(w);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if(lost){
                    String l = "Todas as galinhas morreram!!";
                    System.out.println("Vai mandar " + l);
                    try {
                        io.writeObject(l);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    pos[0][1] = 44;
                    pos[0][2] = 600;
                    pos[1][1] = 364;
                    pos[1][2] = 600;
                    pos[2][1] = 626;
                    pos[2][2] = 600;
                    pos[3][1] = 940;
                    pos[3][2] = 600;
                    
                    //break;
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
                
                if(pos[this.id][2] == -16){
                    count[this.id]++;
                }
                
                if(pos[this.id][2] == -200){
                    lose++;
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
                
                if(numberConnections >= 4){
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
            
            new ThreadWin(this.id).start();
            
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
            
            Integer[] vec = new Integer[2];
            ServerSocket server = null;
            int port = ServerGame.portBase;
            
            try {
               server = new ServerSocket(port);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Servidor conectado na porta " + port);
            
            while(true){
                Socket client = null;
                
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(numberConnections == 4) {
                    numberConnections = 0;
                    ServerGame.portBase += 1000;
                }
                
                vec[0] = numberConnections;
                vec[1] = ServerGame.portBase;
                
                
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
                   io.writeObject(vec);
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
                t.id = numberConnections-1;
                t.start();
            }
        }
    }
    
    public void init(){
        
        ThreadConnection t1 = new ThreadConnection();
        t1.id = 0;
        t1.start();
    }
    
    public static void main(String[] args) {
        
        new ServerGame().init();
        
    }
}
