/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aquanom;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class GamePageController implements Initializable {

    @FXML
    private AnchorPane ruang;
    @FXML
    private ImageView player;
    @FXML
    private ImageView background;
   
    private double velocityX = 0;
    private double velocityY = 0;    
    private final double damping = 0.95;
    private Timeline gameLoop;
    private boolean gameOver = false;
    private Label tapToPlayLabel;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        background.fitWidthProperty().bind(ruang.widthProperty());
        background.fitHeightProperty().bind(ruang.heightProperty());
        background.setPreserveRatio(false);
        ruang.widthProperty().addListener((obs, oldVal, newVal) -> {
            player.setLayoutX(newVal.doubleValue() / 2 - player.getBoundsInLocal().getWidth() / 2);
            tapToPlayLabel.setLayoutX(newVal.doubleValue() / 2 - tapToPlayLabel.getBoundsInLocal().getWidth());
        });
        ruang.heightProperty().addListener((obs, oldVal, newVal) -> {
            player.setLayoutY(newVal.doubleValue() / 2 - player.getBoundsInLocal().getHeight() / 2);
            tapToPlayLabel.setLayoutY(newVal.doubleValue() / 2 - tapToPlayLabel.getBoundsInLocal().getHeight() / 2);
        });
        player.setLayoutX(ruang.getWidth() / 2 - player.getBoundsInLocal().getWidth() / 2);
        player.setLayoutY(ruang.getHeight() / 2 - player.getBoundsInLocal().getHeight() / 2);

        ruang.setOnKeyPressed(this::gerakPlayer);
        ruang.setOnKeyReleased(this::lepas);
        ruang.setFocusTraversable(true);
        ruang.requestFocus();
        ruang.setOnMouseClicked(this::aktivasi);
        startGameLoop();
        
        
        tapToPlayLabel = new Label("Tap to Play");
        tapToPlayLabel.setStyle("-fx-font-size: 24px; "
                + "-fx-text-fill: #FFFFFF; "
                + "-fx-font-family: 'Arial'; "
                + "-fx-font-weight: bold; "
                + "-fx-effect: dropshadow(gaussian, #333333, 10, 0.8, 2, 2);");
        ruang.getChildren().add(tapToPlayLabel);

        tapToPlayLabel.setLayoutX(player.getLayoutX() + player.getFitWidth() / 2 - tapToPlayLabel.getWidth() / 2);
        tapToPlayLabel.setLayoutY(player.getLayoutY() + player.getFitHeight() + 10); // Menambahkan sedikit jarak dari bawah player

        
    }


    private void startGameLoop(){
        gameLoop = new Timeline(new KeyFrame(Duration.millis(16), e -> updateGame()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
        
    }
    private void updateGame() {
        double currentX = player.getLayoutX();
        double currentY = player.getLayoutY();

        double newX = currentX + velocityX;
        double newY = currentY + velocityY;

        newX = Math.max(0, Math.min(ruang.getWidth() - player.getFitWidth(), newX));
        newY = Math.max(0, Math.min(ruang.getHeight() - player.getFitHeight(), newY));
        
        player.setLayoutX(newX);
        player.setLayoutY(newY);
        
        velocityX *= damping;
        velocityY *= damping;
    }

    private void lepas(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
            case DOWN:
                velocityY *= 0.5; 
                break;
            case LEFT:
            case RIGHT:
                velocityX *= 1;
                break;
        }
    } 
    
    private void gerakPlayer(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                velocityY = -8;
                break;
            case DOWN:
                velocityY = 8;
                break;
            case LEFT:
                velocityX = -8;
                player.setScaleX(1); 
                break;
            case RIGHT:
                velocityX = 8;
                player.setScaleX(-1); 
                break;
        }      
    }


    private void aktivasi(MouseEvent event) {
        ruang.requestFocus();
        ruang.getChildren().remove(tapToPlayLabel);
    }
    
}
