package com.example.lab1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    int port = 8080;
    ArrayList<ClientConnection> clientConnections = new ArrayList<>();

    Model model = new Model();


    public static void main(String[] args) {
        new Server().startServer();
    }

    private void startServer() {
        try {
            ServerSocket listenSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = listenSocket.accept(); // listen for new connection
                String name = "Игрок" + (clientConnections.size() + 1);
                ClientConnection clientConnection = new ClientConnection(clientSocket, this, name);
                clientConnections.add(clientConnection); // launch new thread
                if (clientConnections.size() == 2) {
                    startGame();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startGame() {
        Random random = new Random();
        String startName = "Игрок" + (random.nextInt(2) + 1);
        model.currentTurn = startName;
        sendModel();
        System.out.println("Игра началась");
    }

    void sendModel() {
        try {
//            for (ClientConnection connection : clientConnections) {
//                connection.out.writeObject(model);
//            }
            clientConnections.get(0).out.writeObject(new CurrentState(model));
            clientConnections.get(1).out.writeObject(new CurrentState(model));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateModel(ClientMessage clientMessage) {
        int i = clientMessage.yTurn;
        int j = clientMessage.xTurn;

        model.field[i][j] = clientMessage.from;
        model.currentTurn = "lox";
        model.printModel();
        sendModel();
    }
}
