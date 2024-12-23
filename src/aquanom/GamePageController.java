/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aquanom;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.text.Font;
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
    @FXML
    private Label playerPoinLabel; 
    @FXML
    private Label scoreLabel; 

    private int playerPoin = 10; 
    private double velocityX = 0;
    private double velocityY = 0;    
    private final double damping = 0.95;
    private Timeline gameLoop;
    private Timeline generateMusuh;
    private Timeline generateMangsa;
    private List<Musuh> enemies = new ArrayList<>();
    private List<Mangsa> preys = new ArrayList<>();
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
        tapToPlayLabel.setLayoutY(player.getLayoutY() + player.getFitHeight() + 10); 

        tapToPlayLabel.setLayoutX(player.getLayoutX() + player.getFitWidth() / 2 - tapToPlayLabel.getWidth() / 2);
        tapToPlayLabel.setLayoutY(player.getLayoutY() + player.getFitHeight() + 10); 

        playerPoinLabel = new Label(String.valueOf(playerPoin));
        playerPoinLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        ruang.getChildren().add(playerPoinLabel); 

        updatePlayerPoinLabel();
        
        scoreLabel = new Label();
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        scoreLabel.setVisible(false); 
        ruang.getChildren().add(scoreLabel);
        
    }

    public int getPlayerPoin() {
        return playerPoin;
    }

    public void setPlayerPoin(int playerPoin) {
        this.playerPoin = playerPoin;
        if (playerPoinLabel != null) {
            playerPoinLabel.setText(String.valueOf(playerPoin));
        }
    }

    private void updatePlayerPoinLabel() {
        playerPoinLabel.setLayoutX(player.getLayoutX() + player.getFitWidth() / 2 - playerPoinLabel.getWidth() / 2);
        playerPoinLabel.setLayoutY(player.getLayoutY() + player.getFitHeight() ); 
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
        updatePlayerPoinLabel();
        checkCollisions();
        
        
        enemies.removeIf(musuh -> musuh.isOffScreen());
    }

    private void checkCollisions() {
    for (Musuh musuh : new ArrayList<>(enemies)) {
        if (musuh.getGambar().getBoundsInParent().intersects(player.getBoundsInParent())) {
            musuh.hapusMusuh(); 
            enemies.remove(musuh);
            
            ruang.getChildren().remove(player); 
            
            gameOver(); 
            break; 
        }
    }

    for (Mangsa mangsa : new ArrayList<>(preys)) {
        if (mangsa.getGambar().getBoundsInParent().intersects(player.getBoundsInParent())) {
            if (mangsa.getPoin() <= getPlayerPoin()) {
                setPlayerPoin(getPlayerPoin()+ mangsa.getPoin()); 
                playerPoinLabel.setText(" " + playerPoin); 
                mangsa.hapusMangsa(); 
                preys.remove(mangsa); 
            }
            break;
        }
    }
    
}

    private void startGenerateMangsa() {
        generateMangsa = new Timeline(new KeyFrame(Duration.seconds(2), e -> aktivasiMangsa()));
        generateMangsa.setCycleCount(Timeline.INDEFINITE);
        generateMangsa.play();
    }
    
    private void aktivasiMangsa() {
        if (gameOver) return;
    
        boolean moveRight = new Random().nextBoolean();

        Mangsa food = new Mangsa(ruang, moveRight);
        preys.add(food);
    }

    private void startGenerateMusuh() {
        generateMusuh = new Timeline(new KeyFrame(Duration.seconds(2), e -> aktivasiMusuh()));
        generateMusuh.setCycleCount(Timeline.INDEFINITE);
        generateMusuh.play();
    }
    
    private void aktivasiMusuh() {
        if (gameOver) return;
    
        boolean moveRight = new Random().nextBoolean();

        Musuh predator = new Musuh(ruang, moveRight);
        enemies.add(predator);
    }
    
    private void gameOver() {
        gameOver = true;
        gameLoop.stop();
        generateMusuh.stop();
        
        Font hoboFont = Font.loadFont(getClass().getResourceAsStream("/resource/HoboStd.otf"), 55);
        Label gameOverLabel = new Label("Game Over");
        gameOverLabel.setFont(hoboFont);
        gameOverLabel.setStyle("-fx-font-size: 36px; "
            + "-fx-text-fill: #F95454; " 
            + "-fx-font-weight: bold; " 
            + "-fx-effect: dropshadow(gaussian, #333333, 10, 0.8, 2, 2);"); 
        gameOverLabel.setLayoutX(ruang.getWidth() / 2 - 100); 
        gameOverLabel.setLayoutY(ruang.getHeight() / 2 - 50);

        ruang.getChildren().add(gameOverLabel);

        Label scoreLabel = new Label("Score : " + playerPoin);
        scoreLabel.setFont(hoboFont);
        scoreLabel.setStyle("-fx-font-size: 16px; "
            + "-fx-text-fill: #FFFFFF; " 
            + "-fx-font-weight: bold; " 
            + "-fx-effect: dropshadow(gaussian, #333333, 10, 0.8, 2, 2);"); 
        scoreLabel.setLayoutX(gameOverLabel.getLayoutX());
        scoreLabel.setLayoutY(gameOverLabel.getLayoutY() + 60);

        ruang.getChildren().add(scoreLabel);
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(90);  
        restartButton.setPrefHeight(15);  
        restartButton.setLayoutX(ruang.getWidth() / 2 - 50);
        restartButton.setLayoutY(ruang.getHeight() / 2 + 80);
        restartButton.setOnAction(e -> restartGame());
        restartButton.setStyle(
        "-fx-background-color: #80ceff; "  
        + "-fx-text-fill: #0003ff; "  
        + "-fx-background-radius: 10; "  
        + "-fx-font-size: 16px; "  
        + "-fx-font-weight: bold;"  
        );

        restartButton.setOnMouseEntered(e -> 
            restartButton.setStyle(
                "-fx-background-color: #69b7ff; "  
                + "-fx-text-fill: #0003ff; "
                + "-fx-background-radius: 10; "
                + "-fx-font-size: 16px; "
                + "-fx-font-weight: bold;"
            )
        );
        restartButton.setOnMouseExited(e -> 
            restartButton.setStyle(
                "-fx-background-color: #80ceff; "  
                + "-fx-text-fill: #0003ff; "
                + "-fx-background-radius: 10; "
                + "-fx-font-size: 16px; "
                + "-fx-font-weight: bold;"
            )
        );

        ruang.getChildren().add(restartButton);
        playerPoinLabel.setVisible(false);
    }

    private void restartGame() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent homePage = loader.load();
        Stage stage = (Stage) ruang.getScene().getWindow();
        Scene scene = new Scene(homePage);
        stage.setScene(scene);  

        stage.show();

        enemies.clear(); 
        player.setLayoutX(100); 
        player.setLayoutY(ruang.getHeight() / 2);

        startGameLoop();
        startGenerateMusuh();
        startGenerateMangsa();
    } catch (IOException e) {
        e.printStackTrace();
    }
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
        startGenerateMusuh();
        startGenerateMangsa();
    }
    
}
