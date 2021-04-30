package com.company;

import com.company.database.Databases;
import com.company.database.HighScore;
import com.company.database.HighScores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HighscorePage extends JFrame {

    public HighscorePage(String name, int score){
        setSize(625, 630);
        setLocationRelativeTo(null);
        setVisible(true);
        this.setLayout(new GridLayout(11, 1, 5, 10));
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JButton startNew = new JButton("start new game");
        startNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                new Snake().setVisible(true);
                disposeHSPage();
            }
        });

        this.add(startNew);
        try {
            HighScores highScores = new HighScores(10);
            highScores.putHighScore(name, score);
            printHighScores(highScores.getHighScores());

        } catch (SQLException ex) {
            Logger.getLogger(Databases.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void disposeHSPage(){
        this.dispose();
    }

    private void printHighScores(ArrayList<HighScore> highScores) {
        JLabel[] Labels = new JLabel[10];

        for(int i = 0; i < highScores.size(); i++){
            Labels[i] = new JLabel(i+1 + ". " + highScores.get(i).getName() + " score: " + highScores.get(i).getScore(), SwingConstants.CENTER);
            this.add(Labels[i]);
        }
    }

}