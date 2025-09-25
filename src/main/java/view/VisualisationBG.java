package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class VisualisationBG extends Canvas implements IVisualisation {
    private GraphicsContext gc;
    int customerCount = 0;
    private Image canvasBackGround = new Image("/images/canvas_pohja.png");

    public VisualisationBG(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }

    public void clearDisplay() {
        gc.clearRect(0,0,this.getWidth(), this.getHeight());
        gc.drawImage(canvasBackGround, 0, 0);
    }

    @Override
    public void newCustomer() {
        customerCount++;
    }

    @Override
    public void newCustomerCheckin() {

    }

}
