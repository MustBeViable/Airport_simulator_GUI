
package controller;

import javafx.application.Platform;
import simu.entity.Run;
import simu.entity.RunStatistics;
import simu.framework.IEngine;
import simu.model.EventType;
import simu.model.MyEngine;
import view.ISimulatorUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller implements IControllerVtoM, IControllerMtoV {   // NEW
    private IEngine engine;
    private ISimulatorUI ui;

    // sensible defaults used when UI doesn't provide values
    private static final int[] DEFAULT_LINE_COUNTS = new int[]{1,1,1,1,1,1,1,1};

    public Controller(ISimulatorUI ui) {
        this.ui = ui;
    }

    /* Engine control: */
    @Override
    public void startSimulation() {
        // Try to obtain line counts from the UI; fall back to defaults when missing/invalid
        int[] lineCounts = null;
        try {
            lineCounts = ui.getLineCounts();  // get user-selected line counts from UI
        } catch (Exception ignored) {}

        if (lineCounts == null || lineCounts.length != DEFAULT_LINE_COUNTS.length) {
            lineCounts = DEFAULT_LINE_COUNTS.clone();
        }

        engine = new MyEngine(this, lineCounts); // pass the user-selected line counts
        engine.setSimulationTime(ui.getTime());
        engine.setDelay(ui.getDelay());
        ui.getVisualisation().clearDisplay();
        ((Thread) engine).start();
        //((Thread)engine).run(); // Never like this, why?
    }

    @Override
    public void resetSimulation() {
        if (engine!=null) {
            try{
                ((Thread) engine).interrupt();
            } catch (Exception ignored) {}
            engine = null;
        }

        Platform.runLater(() -> {
            ui.getVisualisation().clearDisplay();
            ui.setEndingTime(0);
        });

        restartApplication();
    }

    private void restartApplication() {
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String mainClass = "Main"; // adjust if your main class is in a package

        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add(mainClass);

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.inheritIO(); // optional: attach IO to new process

        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }


    @Override
    public void decreaseSpeed() { // hidastetaan moottorisäiettä
        engine.setDelay((long)(engine.getDelay()*1.10));
    }

    @Override
    public void increaseSpeed() { // nopeutetaan moottorisäiettä
        engine.setDelay((long)(engine.getDelay()*0.9));
    }


    /* Simulation results passing to the UI
     * Because FX-UI updates come from engine thread, they need to be directed to the JavaFX thread
     */
    @Override
    public void showEndTime(double time) {
        Platform.runLater(()->ui.setEndingTime(time));
    }

    @Override
    public void visualiseCustomer() {
        Platform.runLater(() -> {
            ui.getVisualisation().newCustomer();
        });
    }

    public void visualiseCheckIn() {
        Platform.runLater(() -> {
            ui.getVisualisation().newCustomerCheckin();
        });
    }

    @Override
    public void visualiseLuggageDrop(double startX, double startY, boolean isPriority) {
        Platform.runLater(() -> {
            ui.getVisualisation().newCustomerLuggageDrop(startX, startY, isPriority);
        });
    }

    @Override
    public void visualiseSecurity(boolean isPriority, EventType from) {
        Platform.runLater(() -> {
            ui.getVisualisation().customerAnimationToSecurity(isPriority, from);
        });
    }

    @Override
    public void visualisePassport(boolean isPriority, EventType from) {
        Platform.runLater(() -> {
            ui.getVisualisation().customerAnimationToPassport(isPriority,from);
        });
    }

    @Override
    public void visualiseGate(EventType from) {
        Platform.runLater(() -> {
            ui.getVisualisation().customerAnimationToGate(from);
        });
    }

    public void visualiseResults(Run run, RunStatistics runStats) {
        Platform.runLater(() -> {
            view.ResultsController.open(null, run, runStats);
        });
    }
}
