/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aquanom;

import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author HP
 */
public class Mangsa extends HewanLaut{
    private double velocityX; 
    private AnimationTimer timer;
    private AnchorPane pane;
    private boolean isActive = true;
    private int poin;
    private Label poinLabel; 
    private static final String[] gambarMangsa = { 
        "/resource/fish2.png",
        "/resource/fish3.png",
        "/resource/fish4.png"
    };

    public Mangsa(AnchorPane pane, boolean moveRight) {
        
        super(getRandomImagePath(), 
              moveRight ? getStartXLeft() : getStartXRight(pane), 
              getRandomY(pane),                                   
              60,                                                 
              60);                                                

        this.pane = pane;
        this.velocityX = moveRight ? 2 : -2; 
        this.poin = 5 * (new Random().nextInt(9) + 1); 

        pane.getChildren().add(getGambar());

        poinLabel = new Label(String.valueOf(poin));
        poinLabel.setFont(Font.font("Arial", 14));
        poinLabel.setTextFill(Color.WHITE);
        pane.getChildren().add(poinLabel);

        startGameLoop();
    }

    private static String getRandomImagePath() {
        Random random = new Random();
        int index = random.nextInt(gambarMangsa.length); 
        return gambarMangsa[index]; 
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

        updatePosisiLabel();
  
        if (cekPosisiMangsa()) {
            hapusMangsa();

        }
    }

    private void updatePosisiLabel() {
        poinLabel.setLayoutX(getGambar().getLayoutX() + getGambar().getFitWidth() / 2 - poinLabel.getWidth() / 2);
        poinLabel.setLayoutY(getGambar().getLayoutY() + getGambar().getFitHeight() - 30); 
    }

    public boolean cekPosisiMangsa() {
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

    public void hapusMangsa() {
        if (isActive) {
            pane.getChildren().remove(getGambar());
            pane.getChildren().remove(poinLabel);
            stopGameLoop();
        }
    }

    public int getPoin() {
        return poin;
    }
    
}
