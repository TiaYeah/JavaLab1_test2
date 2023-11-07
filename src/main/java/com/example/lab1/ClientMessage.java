package com.example.lab1;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    int xTurn, yTurn;
    String from;

    public ClientMessage(int xTurn, int yTurn, String from) {
        this.xTurn = xTurn;
        this.yTurn = yTurn;
        this.from = from;
    }
}
