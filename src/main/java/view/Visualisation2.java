package view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import simu.model.EventType;


public class Visualisation2 extends Canvas implements IVisualisation {
    private GraphicsContext gc;
    int customerCount = 0;
    private final static double duration = 0.5;  // animation timer, determines how long is the animation
    private final static double ballRadius = 5.0;  // animation ball size

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
        gc.fillRect(0,0, 350, 100);
        gc.setFill(Color.RED);						// then write new text
        gc.setFont(new Font(20));
        gc.fillText("Customer " + customerCount, 20, 20);
    }
    @Override
    public void newCustomerCheckin() {
        //alkupistet
        double coordx0 = 125, coordx1 = 255;
        // loppu pisteet
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

    @Override
    public void resetCustomerCount() {
        this.customerCount = 0;
    }

}
