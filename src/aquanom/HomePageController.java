/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aquanom;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class HomePageController implements Initializable {

    @FXML
    private ImageView background;
    @FXML
    private Button btnPlay;
    @FXML
    private AnchorPane page;
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        background.fitWidthProperty().bind(page.widthProperty());
        background.fitHeightProperty().bind(page.heightProperty());
        background.setPreserveRatio(false);

        // Memuat font dari file
        Font hoboFont = Font.loadFont(getClass().getResourceAsStream("/resource/HoboStd.otf"), 55);

        // Membuat Label dengan font khusus
        Label gameTitle = new Label("Aquanom");
        gameTitle.setFont(hoboFont);
        gameTitle.setStyle("-fx-text-fill: #FFFFFF;"
        + "-fx-effect: dropshadow(gaussian, #333333, 10, 0.8, 2, 2);"); // Efek bayangan
        
        page.widthProperty().addListener((obs, oldVal, newVal) -> {
        gameTitle.setLayoutX(newVal.doubleValue() / 2 - gameTitle.prefWidth(-1) / 2);
        });
        page.heightProperty().addListener((obs, oldVal, newVal) -> {
            gameTitle.setLayoutY(newVal.doubleValue() / 2 - 100); // 100 untuk memberi jarak ke tombol
        });

        page.widthProperty().addListener((obs, oldVal, newVal) -> {
            btnPlay.setLayoutX(newVal.doubleValue() / 2 - btnPlay.getPrefWidth() / 2);
        });
        page.heightProperty().addListener((obs, oldVal, newVal) -> {
            btnPlay.setLayoutY(newVal.doubleValue() / 2);
        });

        // Tambahkan Label ke dalam AnchorPane
        page.getChildren().add(gameTitle);
        
        btnPlay.setOnMouseEntered(event -> {
            btnPlay.setEffect(new DropShadow());
        });

        btnPlay.setOnMouseExited(event -> {
            btnPlay.setEffect(null);
        });
    
    } 



    @FXML
    private void startGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GamePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
}
