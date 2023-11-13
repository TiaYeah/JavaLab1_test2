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
                clientConnections.add(clientConnection);
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
        model.firstPlayerName = startName;
        sendModel();
        System.out.println("Игра началась");
    }

    void sendModel() {
        try {
            for (ClientConnection connection : clientConnections) {
                connection.out.reset();
                connection.out.writeObject(model);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateModel(ClientMessage clientMessage) {
        int i = clientMessage.yTurn;
        int j = clientMessage.xTurn;

        model.field[i][j] = clientMessage.from;
        if (model.doTurn(i, j)) {
            model.remainderTurns = 0;
            model.winnerName = clientMessage.from;
            model.currentTurn = "Игра окончена";
        }
        sendModel();
    }
}
