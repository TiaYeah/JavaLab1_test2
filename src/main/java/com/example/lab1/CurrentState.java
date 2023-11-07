package com.example.lab1;

import java.io.Serializable;

public class CurrentState implements Serializable {
    Model model;

    public CurrentState(Model model) {
        this.model = model;
    }
}
