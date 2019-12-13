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
    public int[] count = new int[4];
    public int lose;
    
    /* Verifica o fim do jogo */
    public class ThreadWin extends Thread{
        
        public int id;
        public ServerGame room;
        
        public ThreadWin(int id, ServerGame room){
            this.id = id;
            this.room = room;
        }
        
        @Override
        public void run(){
            
            /* inicia os vetores de travessias (count) e a variável de mortes (lose) */
            room.lose = 0;
            room.count[0] = 0;
            room.count[1] = 0;
            room.count[2] = 0;
            room.count[3] = 0;
            
            boolean win = false, lost = false;
            int port = 0;
            int winner = 0;
            
            /* relaciona o id das galinhas às portas */
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
            
            System.out.println(" - Servidor conectado na porta " + port);
            
            while(true){ /* Durante todo o funcionamento do jogo */
                
                Socket client = null;
                ObjectOutputStream io = null;
                
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                /* verifica se alguma galinha atravessou 3 vezes */
                if(room.count[0] >= 3){
                    win = true;
                    winner = 0;
                }else if(room.count[1] >= 3){
                    win = true;
                    winner = 1;
                }else if(room.count[2] >= 3){
                    win = true;
                    winner = 1;
                }else if(room.count[3] >= 3){
                    win = true;
                    winner = 3;
                }
                
                /* verifica se todas as galinhas morreram */
                if(room.lose >= 4) lost = true;
                
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
                
                if(win){ //verifica qual galinha venceu
                    
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
                        w = "A galinha preta Venceu!";
                        try {
                            io.writeObject(w);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(winner == 3){
                        w = "A galinha laranja Venceu";
                        try {
                            io.writeObject(w);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                    
                    try {
                        io.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    room.pos[0][1] = 44;
                    room.pos[0][2] = 600;
                    room.pos[1][1] = 364;
                    room.pos[1][2] = 600;
                    room.pos[2][1] = 626;
                    room.pos[2][2] = 600;
                    room.pos[3][1] = 940;
                    room.pos[3][2] = 600;

                    try {
                        client.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    break;
                    
                }else{
                    
                    if(lost){ // Verifica se todas as galinhas morreram
                        String l = "Todas as galinhas morreram!!";
                        try {
                            io.writeObject(l);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        /* reseta as posições */
                        room.pos[0][1] = 44;
                        room.pos[0][2] = 600;
                        room.pos[1][1] = 364;
                        room.pos[1][2] = 600;
                        room.pos[2][1] = 626;
                        room.pos[2][2] = 600;
                        room.pos[3][1] = 940;
                        room.pos[3][2] = 600; 
                        
                        break;
                        
                    }else{ //jogo continua
                        String w = "NULL";

                        try {
                            io.writeObject(w);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
            }  
        }
    }
   
    /* Escuta requisição dos clientes */
    public class ThreadListen extends Thread{
        
        public int id;
        public ServerGame room;
        
        @Override
        public void run(){
            ServerSocket server = null;
            int port = 0;
            
            /* relaciona os ids às portas */
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
        
            while(true){ /* enquanto o jogo funcionar */
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
                    room.pos[this.id] = (Integer[])io.readObject(); //recebe a posição da galinha
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
                
                if(room.pos[this.id][2] == -16){ //conta quantas vezes a galinha conseguiu atravessar 
                    room.count[this.id]++;
                }
                
                if(room.pos[this.id][2] == -200){ //retira as galinhas que perderem todas as vidas
                    room.lose++;
                }
            }
        }
    }
    
    /* Envia as posições das galinhas para o cliente */
    public class ThreadSend extends Thread{
        
        public int id;
        public ServerGame room;
        
        @Override
        public void run(){
            ServerSocket server = null;
            int port = 0;
            
            /* Associa os ids das galinhas às threads */
            if (id == 0) port = ServerGame.portBase+100;
            else if (id == 1) port = ServerGame.portBase+101;
            else if (id == 2) port = ServerGame.portBase+102;
            else if (id == 3) port = ServerGame.portBase+103;
            
            System.out.println(" - Servidor conectado na porta " + port);
            
            try {
               server = new ServerSocket(port); //inicia uma porta no servidor
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while(true){ //espera a conexão de todas as galinhas para iniciar o jogo
                Socket client = null;
                boolean play = false;
                
                try {
                    client = server.accept(); //espera a conexão com o cliente
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                ObjectOutputStream io = null;
                
                try {
                    io = new ObjectOutputStream(client.getOutputStream()); //manda dados de entrada e saída
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    io.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(numberConnections >= 4){ //permite o início do jogo quando quatro galinhas estão conectadas
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
            
            ThreadListen t = new ThreadListen(); //recebe as posições dos cientes
            t.id = this.id;
            t.room = this.room;
            t.start();
            
            new ThreadWin(this.id, this.room).start(); //verifica se uma galinha ganhou ou se todas morreram
            
            try {
               server = new ServerSocket(port+200); //inicia uma nova porta para enviar a posição das galinhas
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println(" + Servidor conectado na porta " + (port+200));
            
            while(true){ //enquanto uma galinha não ganha ou todas estão vivas
                Socket client = null;
                boolean play = false;
                
                try {
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
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
                    io.writeObject(room.pos); //envia uma matriz com a posição das galinhas para todos os clientes
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
    
    /* Abre a conexão com o cliente e designa a porta de comunicação */
    public class ThreadConnection extends Thread{
        
        public int id;
        public ServerGame room;
     
        @Override
        public void run(){
            
            Integer[] vec = new Integer[2];
            ServerSocket server = null;
            int port = 6790;
            
            try {
               server = new ServerSocket(port); //inicia o servidor na porta 10.000
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Servidor conectado na porta " + port);
            
            while(true){
                Socket client = null;
                
                try { //espera a requisição do cliente
                    client = server.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(numberConnections == 4) { //a cada 4 conexões, "reinicia" o servidor para uma nova partida
                    numberConnections = 0;
                    ServerGame.portBase += 1000;
                    this.room = new ServerGame();
                }
                
                vec[0] = numberConnections; //id da galinha
                vec[1] = ServerGame.portBase; //porta base
                
                ObjectOutputStream io = null;
                
                try {
                    io = new ObjectOutputStream(client.getOutputStream()); //objeto de escrita
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    io.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                   io.writeObject(vec); //manda informações do jogador para o cliente
                   numberConnections++;
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
                try {
                    io.close(); //fecha o buffer de escrita
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    client.close(); //fecha a conexão com o cliente
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                ThreadSend t = new ThreadSend(); //thread que manda os dados da galinha para o cliente
                t.id = numberConnections-1;
                t.room = this.room;
                t.start();
            }
        }
    }
    
    /* inicia a thread de conexão */
    public void init(ServerGame room){
        
        ThreadConnection t1 = new ThreadConnection();
        t1.id = 0;
        t1.room = room;
        t1.start();
    }
    
    public static void main(String[] args) {
        
        ServerGame room = new ServerGame();
        room.init(room);
        
    }
}
