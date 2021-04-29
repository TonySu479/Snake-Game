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
    private final int ALL_UNITS = (MATRIX_DIM*MATRIX_DIM)/UNIT_SIZE;

    private int appleX;
    private int appleY;
    private int rockX;
    private int rockY;

    private int time;
    private final int snakeX[] = new int[ALL_UNITS];
    private final int snakeY[] = new int[ALL_UNITS];

    private boolean leftDirection = false;
    private boolean rightDirection =  true;
    private boolean upDirection =  false;
    private boolean downDirection =  false;

    private boolean inGame = true;
    private int applesConsumed;
    private int snakeSize;

    Random random;
    private Timer timer;
    private Timer clock;

    Board(){
        random = new Random();
        addKeyListener(new keyListener());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(MATRIX_DIM, MATRIX_DIM));
        setFocusable(true);
        loadImages();
        initGame();
    }

    public void advanceTime(){
        ++time;
    }

    public void loadImages(){
        ImageIcon appleIc = new ImageIcon(ClassLoader.getSystemResource("com/company/icons/apple.png"));
        apple  = appleIc.getImage();

        ImageIcon RockIc = new ImageIcon(ClassLoader.getSystemResource("com/company/icons/rock.png"));
        rock = RockIc.getImage();
    }

    public void initGame(){
        snakeSize = 2;
        applesConsumed = 0;

        for(int i = 0; i < snakeSize; i++){
            snakeX[i] = 300;
            snakeY[i] = 300;
        }

        putApple();
        putRock();
        timer = new Timer(120, this);
        timer.start();
        clock = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                advanceTime();
            }
        });
        clock.start();
    }

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
        if((snakeX[0] == appleX) && (snakeY[0] == appleY)){
            snakeSize++;
            applesConsumed++;
            putApple();
            putRock();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void drawKey(String Key, Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font(Key, Font.BOLD, 40));
        g.drawString(Key, 20, 80);
    }

    public void draw(Graphics g){

        if(inGame){
            g.drawImage(apple, appleX, appleY, this);
            g.drawImage(rock, rockX, rockY, this);
            g.setColor(Color.BLUE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            g.drawString("Score: " + applesConsumed, 220, 50);

            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            g.drawString(Integer.toString(time), 20, 40);

            if(upDirection){
                drawKey("W", g);
            }
            if(downDirection){
                drawKey("S", g);
            }
            if(leftDirection){
                drawKey("A", g);
            }
            if(rightDirection){
                drawKey("D", g);
            }

            for(int i = 0; i < snakeSize; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                }else{
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        }
    }

    public void checkCollision(){

        for(int i = snakeSize; i > 0 ; i--){
            if((i > 2) && (snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])){
                inGame = false;
            }
        }

        if(snakeX[0] == rockX && snakeY[0] == rockY){
            inGame = false;
        }

        if(snakeY[0] >= 600){
            inGame = false;
        }

        if(snakeX[0] >= 600){
            inGame = false;
        }

        if(snakeX[0] < 0){
            inGame = false;
        }

        if(snakeY[0] < 0 ){
            inGame = false;
        }

        if(!inGame){
            timer.stop();
        }
    }

    public void move(){

        for(int i = snakeSize; i > 0 ; i--){
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        if(leftDirection){
            snakeX[0] = snakeX[0] -  UNIT_SIZE;
        }
        if(rightDirection){
            snakeX[0] += UNIT_SIZE;
        }
        if(upDirection){
            snakeY[0] = snakeY[0] -  UNIT_SIZE;
        }
        if(downDirection){
            snakeY[0] += UNIT_SIZE;
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

    private class keyListener extends KeyAdapter{

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