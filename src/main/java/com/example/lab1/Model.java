package com.example.lab1;

import java.io.Serializable;

public class Model implements Serializable {
    String[][] field = new String[19][19];
    String currentTurn;

    public void printModel() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }
}
