/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aquanom;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class HewanLaut {
    protected ImageView gambar;
    protected double x;
    protected double y;

    public HewanLaut(String imagePath, double startX, double startY, double width, double height) {
        this.gambar = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        this.x = startX;
        this.y = startY;

        gambar.setLayoutX(x);
        gambar.setLayoutY(y);
        gambar.setFitWidth(width);
        gambar.setFitHeight(height);
        gambar.setPreserveRatio(true);
    }

    public ImageView getGambar() {
        return gambar;
    }

    public abstract void updateX();
}
