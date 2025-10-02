package view;

import java.text.DecimalFormat;
import controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import simu.framework.Trace;
import simu.framework.Trace.Level;

public class SimulatorGUI extends Application implements ISimulatorUI {

    private static final int canvasSize = 1200;
    private static final int[] DEFAULT_LINE_COUNTS = new int[]{1, 1, 1, 1, 1, 1, 1, 1};

    // Controller object (UI needs)
    private IControllerVtoM controller;

    // UI Components:
    @FXML private TextField time;
    @FXML private TextField delay;
    @FXML private Label results;
    @FXML private Label timeLabel;
    @FXML private Label delayLabel;
    @FXML private Label resultLabel;
    @FXML private Button startButton;
    @FXML private Button slowButton;
    @FXML private Button speedUpButton;
    @FXML private Button resetButton;
    @FXML private StackPane animationPane;

    // Spinners for line counts (add matching fx:id entries to GUI.fxml)
    @FXML private Spinner<Integer> checkInSpinner;
    @FXML private Spinner<Integer> luggageDropSpinner;
    @FXML private Spinner<Integer> luggageDropPrioritySpinner;
    @FXML private Spinner<Integer> securitySpinner;
    @FXML private Spinner<Integer> securityPrioritySpinner;
    @FXML private Spinner<Integer> passportSpinner;
    @FXML private Spinner<Integer> passportPrioritySpinner;
    @FXML private Spinner<Integer> gateSpinner;

    private IVisualisation displayBG, displayAnimation;

    @Override
    public void init() {
        Trace.setTraceLevel(Level.INFO);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI.fxml"));
            Parent root = loader.load();

            primaryStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });

            primaryStage.setTitle("Simulator");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Called automatically after FXML is loaded
    @FXML
    public void initialize() {

        if (controller == null) {
            controller = new Controller(this);
        }

        displayBG = new VisualisationBG(canvasSize, canvasSize / 2);
        displayAnimation = new Visualisation2(canvasSize, canvasSize / 2);
        animationPane.getChildren().addAll((Node) displayBG, (Node) displayAnimation);

        // Initialize spinners only if they were injected from FXML
        if (checkInSpinner != null) {
            checkInSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, DEFAULT_LINE_COUNTS[0]));
        }
        if (luggageDropSpinner != null) {
            luggageDropSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, DEFAULT_LINE_COUNTS[1]));
        }
        if (luggageDropPrioritySpinner != null) {
            luggageDropPrioritySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, DEFAULT_LINE_COUNTS[2]));
        }
        if (securitySpinner != null) {
            securitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, DEFAULT_LINE_COUNTS[3]));
        }
        if (securityPrioritySpinner != null) {
            securityPrioritySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, DEFAULT_LINE_COUNTS[4]));
        }
        if (passportSpinner != null) {
            passportSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, DEFAULT_LINE_COUNTS[5]));
        }
        if (passportPrioritySpinner != null) {
            passportPrioritySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, DEFAULT_LINE_COUNTS[6]));
        }
        if (gateSpinner != null) {
            gateSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, DEFAULT_LINE_COUNTS[7]));
        }

        // Button actions
        startButton.setOnAction(event -> {
            controller.startSimulation();
            startButton.setDisable(true);
        });
        slowButton.setOnAction(e -> controller.decreaseSpeed());
        speedUpButton.setOnAction(e -> controller.increaseSpeed());

        // Reset button: call controller.resetSimulation() and re-enable Start
        if (resetButton != null) {
            resetButton.setOnAction(e -> onReset());
        }
    }

    @FXML
    private void onReset() {
        if (controller != null) {
            controller.resetSimulation();
        }
        if (startButton != null) {
            startButton.setDisable(false);
        }
    }

    /* UI interface methods (controller calls) */
    @Override
    public double getTime() {
        return Double.parseDouble(time.getText());
    }

    @Override
    public long getDelay() {
        return Long.parseLong(delay.getText());
    }

    @Override
    public void setEndingTime(double time) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        this.results.setText(formatter.format(time));
    }

    @Override
    public IVisualisation getVisualisation() {
        return displayAnimation;
    }

    // Added: gather current spinner values and return an array of 8 ints.
    @Override
    public int[] getLineCounts() {
        try {
            if (checkInSpinner == null || luggageDropSpinner == null || luggageDropPrioritySpinner == null ||
                    securitySpinner == null || securityPrioritySpinner == null || passportSpinner == null ||
                    passportPrioritySpinner == null || gateSpinner == null) {
                return DEFAULT_LINE_COUNTS.clone();
            }

            return new int[] {
                    checkInSpinner.getValue(),
                    luggageDropSpinner.getValue(),
                    luggageDropPrioritySpinner.getValue(),
                    securitySpinner.getValue(),
                    securityPrioritySpinner.getValue(),
                    passportSpinner.getValue(),
                    passportPrioritySpinner.getValue(),
                    gateSpinner.getValue()
            };
        } catch (Exception ex) {
            // Fallback to defaults on any error
            return DEFAULT_LINE_COUNTS.clone();
        }
    }

    /* JavaFX-application (UI) start-up */
    public static void main(String[] args) {
        launch(args);
    }
}
