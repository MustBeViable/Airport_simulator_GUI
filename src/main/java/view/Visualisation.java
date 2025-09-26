package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simu.framework.Event;
import simu.model.EventType;

public class Visualisation extends Canvas implements IVisualisation {
	private GraphicsContext gc;
	double i = 0;
	double j = 10;

	public Visualisation(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		clearDisplay();
	}

	public void clearDisplay() {
		gc.setFill(Color.YELLOW);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public void newCustomer() {
		gc.setFill(Color.RED);
		gc.fillOval(i,j,10,10);

		i += 10;
		if (i > this.getWidth()-1) {
			i = 0;
			j += 10;
		}
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
