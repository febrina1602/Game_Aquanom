/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aquanom;

import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author HP
 */
public class Musuh extends HewanLaut{
    private double velocityX; 
    private AnimationTimer timer;
    private AnchorPane pane;
    private boolean isActive = true;
    private static final String[] gambarMusuh = { 
        "/resource/shark1.png",
        "/resource/shark2.png"
    };

    public Musuh(AnchorPane pane, boolean moveRight) {
        super(getRandomImagePath(), 
              moveRight ? getStartXLeft() : getStartXRight(pane), 
              getRandomY(pane),                                   
              80,                                                 
              80);                                                

        this.pane = pane;
        this.velocityX = moveRight ? 3 : -3; 

        pane.getChildren().add(getGambar());

        startGameLoop();
    }

    private static String getRandomImagePath() {
        Random random = new Random();
        int index = random.nextInt(gambarMusuh.length); 
        return gambarMusuh[index]; 
    }

    private static int getStartXLeft() {
        return -50;
    }

    private static int getStartXRight(AnchorPane pane) {
        double width = pane.getWidth();
        if (width <= 0) width = 300;
        return (int) width + 50; 
    }

    private static int getRandomY(AnchorPane pane) {
        double height = pane.getHeight();
        if (height <= 0) height = 300; 
        return new Random().nextInt((int) height - 50); 
    }

    @Override
    public void updateX() {
        if (!isActive) return;

        double newX = getGambar().getLayoutX() + velocityX;
        getGambar().setLayoutX(newX);

        if (velocityX < 0 && getGambar().getScaleX() == -1) {
            getGambar().setScaleX(1); 
        }

        if (velocityX > 0 && getGambar().getScaleX() == 1) {
            getGambar().setScaleX(-1); 
        }


        if (isOffScreen()) {
            hapusMusuh();
        }
    }

    public boolean isOffScreen() {
        if (velocityX > 0) {
            return getGambar().getLayoutX() > pane.getWidth();
        } else {
            return getGambar().getLayoutX() + getGambar().getFitWidth() < 0;
        }
    }

    
    private void startGameLoop() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateX();
            }
        };
        timer.start();
    }

    private void stopGameLoop() {
        if (timer != null) {
            timer.stop();
        }
    }

    public void hapusMusuh() {
        if (isActive) {
            pane.getChildren().remove(getGambar());
            stopGameLoop();
        }
    }
    
}
