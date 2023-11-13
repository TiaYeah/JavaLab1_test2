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
        int top = Math.max(y - 5, 0);
        int bottom = Math.min(y + 5, 18);
        int left = Math.max(x - 5, 0);
        int right = Math.min(x + 5, 18);

        int counter = 0;
        if (y - 5 >= 0 && x - 5 >= 0) {
            for (int i = 0; i < 5; i++) {
                if (field[y][x].equals(field[top + i][left + i])) {
                    counter++;
                }
            }
            if (counter == 5) {
                System.out.println("WIN");
                return true;
            }
        }
        counter = 0;
        if (y - 5 >= 0 && x + 5 <= 18) {
            for (int i = 0; i < 5; i++) {
                if (field[y][x].equals(field[top + i][right - i])) {
                    counter++;
                }
            }
            if (counter == 5) {
                System.out.println("WIN");
                return true;
            }
        }
        counter = 0;
        if (y + 5 <= 18 && x + 5 <= 18) {
            for (int i = 0; i < 5; i++) {
                if (field[y][x].equals(field[bottom - i][right - i])) {
                    counter++;
                }
            }
            if (counter == 5) {
                System.out.println("WIN");
                return true;
            }
        }
        counter = 0;
        if (y + 5 <= 18 && x - 5 >= 0) {
            for (int i = 0; i < 5; i++) {
                if (field[y][x].equals(field[bottom - i][left + i])) {
                    counter++;
                }
            }
            if (counter == 5) {
                System.out.println("WIN");
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalLine(int y, int x) {
        int top = Math.max(y - 5, 0);
        int bottom = Math.min(y + 5, 18);
        int counter = 0;
        if (y - 5 >= 0) {
            for (int i = 0; i < 5; i++) {
                if (field[y][x].equals(field[top + i][x])) {
                    counter++;
                }
            }
            if (counter == 5) {
                System.out.println("WIN");
                return true;
            }
        }
        counter = 0;
        if (x + 5 <= 18) {
            for (int i = 0; i < 5; i++) {
                if (field[y][x].equals(field[bottom - i][x])) {
                    counter++;
                }
            }
            if (counter == 5) {
                System.out.println("WIN");
                return true;
            }
        }
        return false;
    }

    private boolean checkHorizontalLine(int y, int x) {
        int left = Math.max(x - 5, 0);
        int right = Math.min(x + 5, 18);
        int counter = 0;
        if (x - 5 >= 0) {
            for (int i = 0; i < 5; i++) {
                if (field[y][x].equals(field[y][left + i])) {
                    counter++;
                }
            }
            if (counter == 5) {
                System.out.println("WIN");
                return true;
            }
        }
        counter = 0;
        if (x + 5 <= 18) {
            for (int i = 0; i < 5; i++) {
                if (field[y][x].equals(field[y][right - i])) {
                    counter++;
                }
            }
            if (counter == 5) {
                System.out.println("WIN");
                return true;
            }
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
