package com.example.lab1;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HelloController {
    ObjectOutputStream outObject;
    ObjectInputStream inObject;

    Socket s;

    //Model model = ModelBuilder.getInstance();
    Model model = new Model();

    String name;

    //DataOutputStream out;
    //DataInputStream in;

    @FXML
    private Circle Circle1;

    @FXML
    private Button TestButton;

    @FXML
    private Label TestLabel;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void onMouseEntered(MouseEvent event) {
        Circle1.setStroke(Color.RED);
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        Circle1.setStroke(Color.TRANSPARENT);
    }

    @FXML
    protected void onTestButtonClick() {
        TestLabel.setText("Welcome to JavaFX Application!");
    }

    @FXML
    void initialize() {
        int serverPort = 8080;
        String serverHost = "127.0.0.1";
        connect(serverHost, serverPort);

        addCircles();

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
            System.out.println(name);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addCircles() {
        Circle[][] circles = new Circle[19][19];

        int centerX = 42;
        int centerY = 42;

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                Circle circle = new Circle();

                circle.setRadius(12);
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

//                        System.out.println("x " + xTurn);
//                        System.out.println("y " + yTurn);

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
            outObject.writeObject(clientMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receiveMessage() {
        try {
            while (true) {
                CurrentState currentState = (CurrentState) inObject.readObject();
                System.out.println();
                currentState.model.printModel();
                //model = (Model)inObject.readObject();
                //model.printModel();
                //System.out.println(model.currentTurn);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}