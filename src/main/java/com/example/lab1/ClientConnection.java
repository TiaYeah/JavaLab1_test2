package com.example.lab1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket clientSocket;
    Server server;

    public ClientConnection(Socket clientSocket, Server server, String name) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.start();
    }

    public void run() {
        try {
            while (true) {
                ClientMessage clientMessage = (ClientMessage) in.readObject();

                server.updateModel(clientMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
