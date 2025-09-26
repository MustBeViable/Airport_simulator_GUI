package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import simu.model.EventType;

public class VisualisationBG extends Canvas implements IVisualisation {
    private GraphicsContext gc;
    int customerCount = 0;
    private Image canvasBackGround = new Image("/images/canvas_pohja2.png");

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

    }

    @Override
    public void newCustomerCheckin() {

    }

    @Override
    public void newCustomerLuggageDrop(double startX, double startY, boolean isPriority) {

    }

    @Override
    public void customerAnimationToSecurity(boolean isPriority, EventType from) {

    }


    @Override
    public void customerAnimationToPassport(boolean isPriority, EventType from) {

    }

    @Override
    public void customerAnimationToGate(EventType from) {

    }

}
