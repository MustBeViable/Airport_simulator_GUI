package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import simu.model.EventType;
/**
 * Background-augmented visualization canvas for the airport simulation.
 * This component draws a static background image and provides the {@link IVisualisation}
 * operations that higher layers can call to animate customers between service points.
 * In this variant the animation methods are placeholders to be implemented on top of the
 * background, while {@link #clearDisplay()} resets the canvas to the background state.
 * The background image is loaded from the classpath at {@code /images/canvas_pohja2.png}.
 * @author Elias Rinne
 */
public class VisualisationBG extends Canvas implements IVisualisation {
    private GraphicsContext gc;

    /**
     * Static background image. The path is classpath-relative from the root
     * due to the leading slash.
     */
    private Image canvasBackGround = new Image("/images/canvas_pohja2.png");

    /**
     * Creates the visualization canvas with the given dimensions and clears it to the background.
     *
     * @param w canvas width in pixels
     * @param h canvas height in pixels
     * @author Elias Rinne
     */
    public VisualisationBG(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }
    /**
     * Clears the entire canvas and redraws the static background image at the origin.
     * This method assumes the canvas dimensions are at least as large as the background image.
     * If you need scaling, use {@link GraphicsContext#drawImage(Image,double,double,double,double)}
     * with explicit width/height.
     * @author Elias Rinne
     */
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

    @Override
    public void resetCustomerCount() {

    }

    @Override
    public void scaleAnimationSpeed(double multiplier) {

    }

    @Override
    public void setDuration(int duration) {

    }

}
