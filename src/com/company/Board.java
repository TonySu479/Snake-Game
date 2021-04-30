package com.company;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class Board extends JPanel implements ActionListener{

    private Image apple;
    private Image rock;

    private final int UNIT_SIZE = 25;
    private final int MATRIX_DIM = 600;
    private final int ALL_UNITS = (MATRIX_DIM * MATRIX_DIM) / UNIT_SIZE;

    private int appleX;
    private int appleY;
    private int rockX;
    private int rockY;

    private final int snakeX[] = new int[ALL_UNITS];
    private final int y[] = new int[ALL_UNITS];

    private boolean leftDirection = false;
    private boolean rightDirection =  true;
    private boolean upDirection =  false;
    private boolean downDirection =  false;
    private boolean inGame = true;

    private int applesConsumed;
    private int snakeSize;

    Random random;
    private Timer timer;

    Board(){
        random = new Random();
        addKeyListener(new KListener());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(MATRIX_DIM, MATRIX_DIM));
        setFocusable(true);
        loadImages();
        initGame();
    }


    public void initGame(){
        snakeSize = 2;
        applesConsumed = 0;

        for(int i = 0; i < snakeSize; i++){
            snakeX[i] = 300;
            y[i] = 300;
        }

        putApple();
        putRock();
        timer = new Timer(120, this);
        timer.start();

    }

    public void loadImages(){
        ImageIcon appleIc = new ImageIcon(ClassLoader.getSystemResource("com/company/icons/apple.png"));
        apple  = appleIc.getImage();

        ImageIcon RockIc = new ImageIcon(ClassLoader.getSystemResource("com/company/icons/rock.png"));
        rock = RockIc.getImage();
    }

    /* item interactions */
    public void putApple(){
        appleX = random.nextInt((int)(MATRIX_DIM/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(MATRIX_DIM/UNIT_SIZE))*UNIT_SIZE;
    }

    public void putRock(){
        rockX = random.nextInt((int)(MATRIX_DIM/UNIT_SIZE))*UNIT_SIZE;
        rockY = random.nextInt((int)(MATRIX_DIM/UNIT_SIZE))*UNIT_SIZE;

        while(rockX == appleX && rockY == appleY){
            rockX = random.nextInt((int)(MATRIX_DIM/UNIT_SIZE))*UNIT_SIZE;
            rockY = random.nextInt((int)(MATRIX_DIM/UNIT_SIZE))*UNIT_SIZE;
        }
    }

    public void checkApple(){
        if((snakeX[0] == appleX) && (y[0] == appleY)){
            snakeSize++;
            applesConsumed++;
            putApple();
            putRock();
        }
    }

    /* Visualizer */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(inGame){
            g.drawImage(apple, appleX, appleY, this);
            g.drawImage(rock, rockX, rockY, this);
            g.setColor(Color.BLUE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            g.drawString("Score: " + applesConsumed, 220, 50);

            for(int i = 0; i < snakeSize; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(snakeX[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }else{
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(snakeX[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        }else{
            String name = JOptionPane.showInputDialog("Please Enter your name: ");
            String[] options = { "new game", "show highscores"};

            int response = JOptionPane.showOptionDialog(null, "What would you like to do?", "Game Over",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            if(response == 0){
                new Snake().setVisible(true);
            }
            else{
                HighscorePage highscorePage = new HighscorePage(name, applesConsumed);
                highscorePage.show();
            }

        }
    }

    /* Movement trackers */
    public void checkCollision(){

        for(int i = snakeSize; i > 0 ; i--){
            if((i > 2) && (snakeX[0] == snakeX[i]) && (y[0] == y[i])){
                inGame = false;
            }
        }

        if(snakeX[0] == rockX && y[0] == rockY){
            inGame = false;
        }

        if(y[0] >= 600){
            inGame = false;
        }

        if(snakeX[0] >= 600){
            inGame = false;
        }

        if(snakeX[0] < 0){
            inGame = false;
        }

        if(y[0] < 0 ){
            inGame = false;
        }

        if(!inGame){
            timer.stop();
        }
    }

    public void move(){

        for(int i = snakeSize; i > 0 ; i--){
            snakeX[i] = snakeX[i - 1];
            y[i] = y[i - 1];
        }

        if(leftDirection){
            snakeX[0] = snakeX[0] -  UNIT_SIZE;
        }
        if(rightDirection){
            snakeX[0] += UNIT_SIZE;
        }
        if(upDirection){
            y[0] = y[0] -  UNIT_SIZE;
        }
        if(downDirection){
            y[0] += UNIT_SIZE;
        }
    }

    public void actionPerformed(ActionEvent ae){
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class KListener extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e){
            int key =  e.getKeyCode();

            if(key == KeyEvent.VK_A && (!rightDirection)){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if(key == KeyEvent.VK_D && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if(key == KeyEvent.VK_W && (!downDirection)){
                leftDirection = false;
                upDirection = true;
                rightDirection = false;
            }

            if(key == KeyEvent.VK_S && (!upDirection)){
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}