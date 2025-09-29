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
    @FXML private StackPane animationPane;

    private IVisualisation displayBG, displayAnimation;

    @Override
    public void init() {
        Trace.setTraceLevel(Level.INFO);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI.fxml"));
            // remove: loader.setController(this);
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

        // Button actions
        startButton.setOnAction(event -> {
            controller.startSimulation();
            startButton.setDisable(true);
        });
        slowButton.setOnAction(e -> controller.decreaseSpeed());
        speedUpButton.setOnAction(e -> controller.increaseSpeed());
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

    /* JavaFX-application (UI) start-up */
    public static void main(String[] args) {
        launch(args);
    }
}
