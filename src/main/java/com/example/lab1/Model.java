package com.example.lab1;

import java.io.Serializable;
import java.util.Arrays;

public class Model implements Serializable {
    String[][] field = new String[19][19];
    String currentTurn;
    String firstPlayerName;
    String winnerName;

    int remainderTurns = 1;


    public boolean doTurn(int y, int x) {


        remainderTurns--;
        if (remainderTurns == 0) {
            if (currentTurn.equals("Игрок1")) {
                currentTurn = "Игрок2";
            } else {
                currentTurn = "Игрок1";
            }
            remainderTurns = 2;
        }
        return checkWin(y, x);
    }

    private boolean checkWin(int y, int x) {
        return checkHorizontalLine(y, x) || checkVerticalLine(y, x) || checkDiagonalLines(y, x);
    }

    private boolean checkDiagonalLines(int y, int x) {
        int bottom = Math.min(y + 5, 18);
        int right = Math.min(x + 5, 18);

        int counter = 0;

        for (int i = 0; i < Math.min(5, Math.min(x, y)); i++) {
            if (field[y][x].equals(field[y - 1 - i][x - 1 - i])) {
                counter++;
            } else {
                break;
            }
        }
        for (int i = 0; i < Math.min(5, Math.min(right - x, bottom - y)); i++) {
            if (field[y][x].equals(field[y + 1 + i][x + 1 + i])) {
                counter++;
            } else {
                break;
            }
        }
        if (counter >= 5) {
            System.out.println("WIN");
            return true;
        }

        counter = 0;

        for (int i = 0; i < Math.min(5, Math.min(right - x, y)); i++) {
            if (field[y][x].equals(field[y - 1 - i][x + 1 + i])) {
                counter++;
            } else {
                break;
            }
        }

        for (int i = 0; i < Math.min(5, Math.min(x, bottom - y)); i++) {
            if (field[y][x].equals(field[y + 1 + i][x - 1 - i])) {
                counter++;
            } else {
                break;
            }
        }
        if (counter >= 5) {
            System.out.println("WIN");
            return true;
        }

        return false;
    }

    private boolean checkVerticalLine(int y, int x) {
        int bottom = Math.min(y + 5, 18);
        int counter = 0;
        for (int i = 0; i < Math.min(y, 5); i++) {
            if (field[y][x].equals(field[y - 1 - i][x])) {
                counter++;
            } else {
                break;
            }
        }
        for (int i = 0; i < Math.min(bottom - y, 5); i++) {
            if (field[y][x].equals(field[y + 1 + i][x])) {
                counter++;
            } else {
                break;
            }
        }
        if (counter >= 5) {
            System.out.println("WIN");
            return true;
        }
        return false;
    }

    private boolean checkHorizontalLine(int y, int x) {
        int right = Math.min(x + 5, 18);
        int counter = 0;
        for (int i = 0; i < Math.min(x, 5); i++) {
            if (field[y][x].equals(field[y][x - 1 - i])) {
                counter++;
            } else {
                break;
            }
        }
        for (int i = 0; i < Math.min(right - x, 5); i++) {
            if (field[y][x].equals(field[y][x + 1 + i])) {
                counter++;
            } else {
                break;
            }
        }
        if (counter >= 5) {
            System.out.println("WIN");
            return true;
        }
        return false;
    }

    @Override
    public String toString() {

        return "Model{" +
                "field=" + Arrays.toString(field) +
                ", currentTurn='" + currentTurn + '\'' +
                '}';
    }
}
