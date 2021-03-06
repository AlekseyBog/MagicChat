package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;

    private List<ClientHandler> clients;
    private AuthService authService;

    public Server() {

        clients = new CopyOnWriteArrayList<>();
//        authService = new SimpleAuthService();
        if (!DBHandler.connect()) {
            throw new RuntimeException("No connected");
        }
        authService = new DBAServise();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started!");

            while (true){
                socket = server.accept();
                System.out.println("Client connected:" + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Server stop");
            DBHandler.disconnect();
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribe (ClientHandler clientHandler){
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe (ClientHandler clientHandler){
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public void broadcastMsg (ClientHandler sender, String msg){
        String message = String.format("[ %s ]: %s", sender.getNickname(), msg);
        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }

    public void privateMsg (ClientHandler sender, String addressee, String msg){
        String message = String.format("[ %s ] от [ %s ]: %s", addressee, sender.getNickname(), msg);
        for (ClientHandler c : clients) {
            if (c.getNickname().equals(addressee)){
                c.sendMsg(message);
                if (!sender.getNickname().equals(addressee)){
                    sender.sendMsg(message);
                }
                return;
            }
        }
        sender.sendMsg("No addressee with this nickname");
    }

    public boolean isLoginAuthenticated (String login){
        for (ClientHandler c : clients){
            if (c.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList (){
        StringBuilder sb = new StringBuilder("/clientList");
        for (ClientHandler c : clients) {
            sb.append(" ").append(c.getNickname());
        }

        String message = sb.toString();

        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }

    public AuthService getAuthService() {
        return authService;
    }
}
