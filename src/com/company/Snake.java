package com.company;

import javax.swing.*;

public class Snake extends JFrame {

    Snake() {
        Board board = new Board();
        add(board);
        pack();
        setTitle("Snake Game");
        setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
