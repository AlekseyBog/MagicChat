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
        authService = new SimpleAuthService();

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
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribe (ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    public void unsubscribe (ClientHandler clientHandler){
        clients.remove(clientHandler);
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

    public AuthService getAuthService() {
        return authService;
    }
}
