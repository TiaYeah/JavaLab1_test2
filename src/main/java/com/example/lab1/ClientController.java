package com.example.lab1;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientController {
    ObjectOutputStream outObject;
    ObjectInputStream inObject;

    Socket s;
    Model model = new Model();

    String name;
    Circle[][] circles;

    @FXML
    private Label nameLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Circle turnCircle1;

    @FXML
    private Circle turnCircle2;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField adressField;
    @FXML
    private Button connectButoon;

    @FXML
    void initialize() {
        int serverPort = 8080;
        String serverHost = "127.0.0.1";
        //connect(serverHost, serverPort);

        addCircles();

//        new Thread(() -> {
//            while (true) {
//                receiveMessage();
//            }
//        }).start();
    }
    public void onConnectButtonClicked(){
        String ip = adressField.getText();
        int portPos = ip.lastIndexOf(':');
        int port = Integer.parseInt(ip.substring(portPos + 1));


        ip = ip.substring(0, portPos);
        connect(ip, port);
        new Thread(() -> {
            while (true) {
                receiveMessage();
            }
        }).start();
    }

    private void connect(String serverHost, int serverPort) {
        try {
            s = new Socket(serverHost, serverPort);
            System.out.println("Connected to " + serverHost);

            outObject = new ObjectOutputStream(s.getOutputStream());
            inObject = new ObjectInputStream(s.getInputStream());
            name = (String) inObject.readObject();
            nameLabel.setText(nameLabel.getText() + name);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addCircles() {
        circles = new Circle[19][19];

        int centerX = 42;
        int centerY = 42;

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                Circle circle = new Circle();

                circle.setRadius(14);
                circle.setFill(Color.TRANSPARENT);
                circle.setStrokeWidth(4);
                circle.setStrokeType(StrokeType.INSIDE);
                circle.setCenterX(centerX);
                circle.setCenterY(centerY);

                circle.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        circle.setStroke(Color.RED);
                    }
                });
                circle.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        circle.setStroke(Color.TRANSPARENT);
                    }
                });
                circle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        int xTurn = ((int) (circle.getCenterX() - 40)) / 42;
                        int yTurn = ((int) (circle.getCenterY() - 40)) / 42;

                        sendMessage(new ClientMessage(xTurn, yTurn, name));
                    }
                });
                anchorPane.getChildren().add(circle);
                circles[i][j] = circle;
                centerX += 42;
            }
            centerX = 42;
            centerY += 42;
        }
    }

    private void sendMessage(ClientMessage clientMessage) {
        try {
            if (model.currentTurn != null && model.currentTurn.equals(name)) {
                outObject.writeObject(clientMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receiveMessage() {
        try {
            while (true) {
                Model currentState = (Model) inObject.readObject();
                model = currentState;
                updateField();
                Color colorOfReminder;
                if (model.currentTurn != null) {
                    if (!model.currentTurn.equals(model.firstPlayerName)) {
                        colorOfReminder = Color.WHITE;
                    } else {
                        colorOfReminder = Color.BLACK;
                    }
                    Platform.runLater(() -> {
                        turnCircle1.setFill(colorOfReminder);
                        turnCircle1.setVisible(true);
                        turnCircle2.setFill(colorOfReminder);
                        turnCircle2.setVisible(true);
                        if (model.remainderTurns == 1) {
                            turnCircle2.setVisible(false);
                        } else if (model.remainderTurns == 0) {
                            turnCircle1.setVisible(false);
                            turnCircle2.setVisible(false);
                        }
                    });
                }

                if (model.currentTurn.equals("Игра окончена")) {
                    Platform.runLater(() -> {
                        turnLabel.setText("Игра окончена");
                    });
                } else if (model.currentTurn.equals(name)) {
                    Platform.runLater(() -> {
                        turnLabel.setText("Сейчас ваш ход");
                    });
                } else {
                    Platform.runLater(() -> {
                        turnLabel.setText("Сейчас ход противника");
                    });

                }
                if (model.winnerName != null) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Победил " + model.winnerName);
                        alert.setTitle("Игра окончена");
                        alert.setHeaderText("Игра окончена");

                        //alert.setX(anchorPane.getScene().getWindow().getWidth() / 2 - alert.getWidth());
                        //alert.setY(anchorPane.getScene().getWindow().getHeight() / 2 - alert.getHeight());
                        alert.initOwner(anchorPane.getScene().getWindow());
                        alert.showAndWait();
                    });
                    return;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private void updateField() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (model.field[i][j] != null) {
                    if (!model.field[i][j].equals(model.firstPlayerName)) {
                        int y = i;
                        int x = j;
                        Platform.runLater(() -> {
                            anchorPane.getChildren().remove(circles[y][x]);
                            Circle circle = new Circle();
                            circle.setFill(Color.WHITE);
                            circle.setStroke(Color.BLACK);
                            circle.setStrokeWidth(1);
                            circle.setRadius(14);
                            circle.setCenterX(circles[y][x].getCenterX());
                            circle.setCenterY(circles[y][x].getCenterY());
                            InnerShadow innerShadowBlack = new InnerShadow();
                            innerShadowBlack.setChoke(0.1);
                            innerShadowBlack.setWidth(1);
                            innerShadowBlack.setHeight(1);
                            innerShadowBlack.setRadius(5);
                            innerShadowBlack.setOffsetY(-4);
                            circle.setEffect(innerShadowBlack);
                            circles[y][x] = circle;
                            anchorPane.getChildren().add(circle);
                        });
                    } else {
                        int y = i;
                        int x = j;
                        Platform.runLater(() -> {
                            anchorPane.getChildren().remove(circles[y][x]);
                            Circle circle = new Circle();
                            circle.setFill(Color.BLACK);
                            circle.setStroke(Color.BLACK);
                            circle.setStrokeWidth(1);
                            circle.setRadius(14);
                            circle.setCenterX(circles[y][x].getCenterX());
                            circle.setCenterY(circles[y][x].getCenterY());
                            Light.Distant light = new Light.Distant();
                            light.setAzimuth(-45.0f);

                            Lighting l = new Lighting();
                            l.setLight(light);
                            l.setSurfaceScale(20.0f);
                            circle.setEffect(l);
                            circles[y][x] = circle;
                            anchorPane.getChildren().add(circle);
                        });

                    }

                }
            }
        }
    }
}