package view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Visualisation2 extends Canvas implements IVisualisation {
    private javafx.animation.AnimationTimer checkinTimer;
    private GraphicsContext gc;
	int customerCount = 0;
    private Image canvasBackGround = new Image("/images/canvas_pohja.png");

	public Visualisation2(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		clearDisplay();
	}

	public void clearDisplay() {

	}
	
	public void newCustomer() {
		customerCount++;
		
		gc.setFill(Color.WHITE);					// first erase old text
        gc.fillRect(0,0, 1300, 100);
		gc.setFill(Color.RED);						// then write new text
		gc.setFont(new Font(20));
		gc.fillText("Customer " + customerCount, 20, 20);
	}

    public void newCustomerCheckin() {
        //alkupistet
        double coordx0 = 140, coordx1 = 240;
        // loppu pisteet
        double coordy0 = 290, coordy1 = 375;
        double duration = 1.0;
        double ballRadius = 5.0;

        if (checkinTimer != null) {
            checkinTimer.stop();
        }
        checkinTimer = new AnimationTimer() {
            long startTime = -1;
            double prevX = coordx0, prevY = coordy0;
            double x = coordx0, y = coordy0;

            @Override
            public void handle(long now) {
                if (startTime < 0) startTime = now;
                double t = (now - startTime) / 1e9 / duration; // 0..1
                if (t > 1) t = 1;

                gc.clearRect(prevX - ballRadius -1, prevY - ballRadius - 1,
                        2*ballRadius + 2, 2*ballRadius + 2 );
                x = coordx0 + (coordx1 - coordx0) * t;
                y = coordy0 + (coordy1 - coordy0) * t;

                gc.setFill(Color.RED);
                gc.fillOval(x - ballRadius, y - ballRadius, 2*ballRadius, 2*ballRadius);

                prevX = x;
                prevY = y;

                if (t >= 1) {
                    stop();
                }
            }
        };
        checkinTimer.start();
    }
/*
    public void servicePoints() {
        gc.
    }

 */
}
