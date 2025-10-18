package view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import simu.model.EventType;

/**
 * Canvas-based visualization component that animates passenger movements between
 * service points in the airport simulation. The class draws a small "ball" (a filled oval)
 * that travels along straight line segments between pre-defined coordinates.
 * Threading: All public methods are intended to be called from the JavaFX Application Thread.
 * @author Elias Rinne
 */
public class Visualisation2 extends Canvas implements IVisualisation {
    private GraphicsContext gc;
    int customerCount = 0;

    /**
     * Animation duration per leg in seconds. Smaller values increase perceived speed.
     */
    private static double duration = 1;  // animation timer, determines how long is the animation

    /**
     * Radius of the animated indicator ("ball") in pixels.
     */
    private final static double ballRadius = 5.0;  // animation ball size

    /**
     * Creates a visualization canvas with the given dimensions.
     *
     * @param w canvas width in pixels
     * @param h canvas height in pixels
     * @author Elias Rinne
     */
    public Visualisation2(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }

    public void clearDisplay() {

    }
    /**
     * Increments the customer counter and renders the updated value in the
     * top-left corner of the canvas.
     * @author Elias Rinne
     */
    public void newCustomer() {
        customerCount++;

        gc.setFill(Color.WHITE);					// first erase old text
        gc.fillRect(0,0, 350, 100);
        gc.setFill(Color.RED);						// then write new text
        gc.setFont(new Font(20));
        gc.fillText("Customer " + customerCount, 20, 20);
    }
    /**
     * Animates a passenger from the arrival area to the check-in desk along a horizontal line.
     * Start: (125, 280), End: (255, 280).
     * @author Elias Rinne
     */
    @Override
    public void newCustomerCheckin() {
        //starting points
        double coordx0 = 125, coordx1 = 255;
        //end points
        double coordy0 = 280, coordy1 = 280;

        // 0..1
        AnimationTimer checkinTimer = new AnimationTimer() {
            long startTime = -1;
            double prevX = coordx0, prevY = coordy0;
            double x = coordx0, y = coordy0;

            @Override
            public void handle(long now) {
                if (startTime < 0) startTime = now;
                double t = (now - startTime) / 1e9 / duration; // 0..1
                if (t > 1) t = 1;

                gc.clearRect(prevX - ballRadius - 1, prevY - ballRadius - 1,
                        2 * ballRadius + 2, 2 * ballRadius + 2);
                x = coordx0 + (coordx1 - coordx0) * t;
                y = coordy0 + (coordy1 - coordy0) * t;

                gc.setFill(Color.RED);
                gc.fillOval(x - ballRadius, y - ballRadius, 2 * ballRadius, 2 * ballRadius);

                prevX = x;
                prevY = y;

                if (t >= 1) {
                    stop();
                }
            }
        };
        checkinTimer.start();
    }

    /**
     * Animates a passenger from an arbitrary starting point to the luggage-drop area.
     * The destination depends on whether the passenger uses the priority lane.
     *
     * @param startX starting X coordinate in canvas pixels
     * @param startY starting Y coordinate in canvas pixels
     * @param isPriority {@code true} for priority luggage drop (destination (370, 50));
     * {@code false} for standard luggage drop (destination (430, 495))
     * @author Elias Rinne
     */
    @Override
    public void newCustomerLuggageDrop(double startX, double startY, boolean isPriority) {
        double coordx0 = startX;
        double coordy0 = startY;
        double coordx1, coordy1;
        if (isPriority) {
            coordx1 = 370;
            coordy1 = 50;
        } else {
            coordx1 = 430;
            coordy1 = 495;
        }

        // 0..1
        AnimationTimer luggageDropTimer = new AnimationTimer() {
            long startTime = -1;
            double prevX = coordx0, prevY = coordy0;
            double x = coordx0, y = coordy0;

            @Override
            public void handle(long now) {
                if (startTime < 0) startTime = now;
                double t = (now - startTime) / 1e9 / duration; // 0..1
                if (t > 1) t = 1;

                gc.clearRect(prevX - ballRadius - 1, prevY - ballRadius - 1,
                        2 * ballRadius + 2, 2 * ballRadius + 2);
                x = coordx0 + (coordx1 - coordx0) * t;
                y = coordy0 + (coordy1 - coordy0) * t;

                gc.setFill(Color.RED);
                gc.fillOval(x - ballRadius, y - ballRadius, 2 * ballRadius, 2 * ballRadius);

                prevX = x;
                prevY = y;

                if (t >= 1) {
                    stop();
                }
            }
        };
        luggageDropTimer.start();
    }

    /**
     * Animates a passenger from a given source area to the security checkpoint.
     * The start coordinate is derived from {@code from}, and the destination is
     * chosen based on {@code isPriority}.
     *
     * @param isPriority whether the passenger uses the priority security lane
     * (destination (585, 145)) or the standard lane (destination (590, 435))
     * @param from source stage: ARR1, CHECK_IN, LUGGAGE_DROP, or LUGGAGE_DROP_PRIORITY
     * @author Elias Rinne
     */
    @Override
    public void customerAnimationToSecurity(boolean isPriority, EventType from) {
        double coordx0, coordy0;
        switch (from) {
            case ARR1:
                coordx0 = 120;
                coordy0 = 280;
                break;
            case CHECK_IN:
                coordx0 = 375;
                coordy0 = 275;
                break;
            case LUGGAGE_DROP:
                coordx0 = 490;
                coordy0 = 525;
                break;
            case LUGGAGE_DROP_PRIORITY:
                coordx0 = 490;
                coordy0 = 45;
                break;
            default:
                System.out.println("lost lamb. Ei pitäs koskaa näkyä tätä printtiä");
                coordy0 = 0;
                coordx0 = 0;
        }

        double coordx1, coordy1;
        if (isPriority) {
            coordx1 = 585;
            coordy1 = 145;
        } else {
            coordx1 = 590;
            coordy1 = 435;
        }

        // 0..1
        AnimationTimer securityTimer = new AnimationTimer() {
            long startTime = -1;
            double prevX = coordx0, prevY = coordy0;
            double x = coordx0, y = coordy0;

            @Override
            public void handle(long now) {
                if (startTime < 0) startTime = now;
                double t = (now - startTime) / 1e9 / duration; // 0..1
                if (t > 1) t = 1;

                gc.clearRect(prevX - ballRadius - 1, prevY - ballRadius - 1,
                        2 * ballRadius + 2, 2 * ballRadius + 2);
                x = coordx0 + (coordx1 - coordx0) * t;
                y = coordy0 + (coordy1 - coordy0) * t;

                gc.setFill(Color.RED);
                gc.fillOval(x - ballRadius, y - ballRadius, 2 * ballRadius, 2 * ballRadius);

                prevX = x;
                prevY = y;

                if (t >= 1) {
                    stop();
                }
            }
        };
        securityTimer.start();

    }

    /**
     * Animates a passenger from security to passport control. The start coordinate
     * is derived from the {@code from} security lane, and the destination depends
     * on {@code isPriority}.
     *
     * @param isPriority whether the passenger uses the priority passport lane
     * (destination (865, 145)) or the standard lane (destination (865, 435))
     * @param from source: SECURITY or SECURITY_PRIORITY
     * @author Elias Rinne
     */
    @Override
    public void customerAnimationToPassport(boolean isPriority, EventType from) {
        double coordx0, coordy0;

        switch (from) {
            case SECURITY:
                coordx0 = 710;
                coordy0 = 435;
                break;
            case SECURITY_PRIORITY:
                coordx0 = 710;
                coordy0 = 145;
                break;
            default:
                System.out.println("lost lamb. Ei pitäs koskaa näkyä tätä printtiä.");
                coordy0 = 0;
                coordx0 = 0;
        }


        double coordx1, coordy1;
        if (isPriority) {
            coordx1 = 865;
            coordy1 = 145;
        } else {
            coordx1 = 865;
            coordy1 = 435;
        }

        AnimationTimer passportTimer = new AnimationTimer() {
            long startTime = -1;
            double prevX = coordx0, prevY = coordy0;
            double x = coordx0, y = coordy0;

            @Override
            public void handle(long now) {
                if (startTime < 0) startTime = now;
                double t = (now - startTime) / 1e9 / duration; // 0..1
                if (t > 1) t = 1;

                gc.clearRect(prevX - ballRadius - 1, prevY - ballRadius - 1,
                        2 * ballRadius + 2, 2 * ballRadius + 2);
                x = coordx0 + (coordx1 - coordx0) * t;
                y = coordy0 + (coordy1 - coordy0) * t;

                gc.setFill(Color.RED);
                gc.fillOval(x - ballRadius, y - ballRadius, 2 * ballRadius, 2 * ballRadius);

                prevX = x;
                prevY = y;

                if (t >= 1) {
                    stop();
                }
            }
        };
        passportTimer.start();

    }

    /**
     * Animates a passenger from security or passport control to the gate.
     * The start coordinate is chosen from {@code from}, destination is fixed
     * at (1030, 290).
     *
     * @param from source: SECURITY / SECURITY_PRIORITY / PASSPORT_CONTROL / PASSPORT_CONTROL_PRIORITY
     * @author Elias Rinne
     */
    @Override
    public void customerAnimationToGate(EventType from) {

        double coordx0, coordy0;

        switch (from) {
            case SECURITY:
                coordx0 = 710;
                coordy0 = 435;
                break;
            case SECURITY_PRIORITY:
                coordx0 = 710;
                coordy0 = 145;
                break;
            case PASSPORT_CONTROL:
                coordx0 = 970;
                coordy0 = 175;
                break;
            case PASSPORT_CONTROL_PRIORITY:
                coordx0 = 970;
                coordy0 = 405;
                break;
            default:
                System.out.println("lost lamb. Ei pitäs koskaa näkyä tätä printtiä");
                coordy0 = 0;
                coordx0 = 0;
        }


        double coordx1, coordy1;

        coordx1 = 1030;
        coordy1 = 290;


        AnimationTimer gateTimer = new AnimationTimer() {
            long startTime = -1;
            double prevX = coordx0, prevY = coordy0;
            double x = coordx0, y = coordy0;

            @Override
            public void handle(long now) {
                if (startTime < 0) startTime = now;
                double t = (now - startTime) / 1e9 / duration; // 0..1
                if (t > 1) t = 1;

                gc.clearRect(prevX - ballRadius - 1, prevY - ballRadius - 1,
                        2 * ballRadius + 2, 2 * ballRadius + 2);
                x = coordx0 + (coordx1 - coordx0) * t;
                y = coordy0 + (coordy1 - coordy0) * t;

                gc.setFill(Color.RED);
                gc.fillOval(x - ballRadius, y - ballRadius, 2 * ballRadius, 2 * ballRadius);

                prevX = x;
                prevY = y;

                if (t >= 1) {
                    stop();
                }
            }
        };
        gateTimer.start();

    }
    /**
     * Resets the in-memory customer counter used for display.
     * @author Elias Rinne
     */
    @Override
    public void resetCustomerCount() {
        this.customerCount = 0;
    }

    /**
     * Scales animation speed by adjusting {@link #duration}. Values &lt; 1.0 speed up the animation,
     * values &gt; 1.0 slow it down. A small lower bound helps avoid extremely fast (hard-to-see)
     * frames.
     *
     * @param multiplier factor applied to the current {@code duration}; recommended &gt; 0
     * @implNote A typical implementation is {@code duration = Math.max(duration * multiplier, 0.05)}.
     * @author Elias Rinne
     */
    @Override
    public void scaleAnimationSpeed(double multiplier) {
        Math.max(duration *= multiplier, 0.05);
    }
    /**
     * Sets the base per-leg animation duration.
     *
     * @param dur duration in seconds; non-positive values may result in instantaneous
     * movement and are not recommended.
     * @author Elias Rinne
     */
    @Override
    public void setDuration(int dur) {
        duration = dur;
    }

}
