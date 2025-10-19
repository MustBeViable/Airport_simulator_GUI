
package controller;

import dao.RunDao;
import dao.RunStatisticsDao;
import javafx.application.Platform;
import simu.model.entity.Run;
import simu.model.entity.RunStatistics;
import simu.framework.Clock;
import simu.framework.IEngine;
import simu.model.EventType;
import simu.model.MyEngine;
import view.ISimulatorUI;

/**
 * Controller that orchestrates the simulation engine and the JavaFX UI.
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Starts/stops the simulation engine and configures timing parameters.</li>
 *   <li>Fetches persisted {@link Run} and {@link RunStatistics} via DAOs.</li>
 *   <li>Schedules UI updates on the JavaFX Application Thread.</li>
 * </ul>
 * <p><b>Threading:</b> Methods may be called from the engine thread; all UI changes are marshalled via
 * {@link javafx.application.Platform#runLater(Runnable)} to the JavaFX Application Thread.</p>
 */
public class Controller implements IControllerVtoM, IControllerMtoV {   // NEW
    private IEngine engine;
    private ISimulatorUI ui;
    private final RunDao rDao = new RunDao();
    private final RunStatisticsDao runStatisticsDao = new RunStatisticsDao();

    // sensible defaults used when UI doesn't provide values
    private static final int[] DEFAULT_LINE_COUNTS = new int[]{1,1,1,1,1,1,1,1};

    public Controller(ISimulatorUI ui) {
        this.ui = ui;
    }

    /**
     * Initialises necessary objects to start the simulation
     * @author Elias Eide and whoever made the base
     */
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
        ui.getVisualisation().setDuration(1);
        ((Thread) engine).start();
    }

    /**
     * Sets clock back to 0 and ends the engine thread. Enables the new run.
     * @author Elias Eide and Elias Rinne
     */
    @Override
    public void resetSimulation() {
        if (engine!=null) {
            try{
                ((Thread) engine).interrupt();
            } catch (Exception ignored) {}
            engine = null;
        }
        Platform.runLater(() -> {
            Clock.getInstance().setTime(0.0);
        });
    }

    /**
     * Opens result window and shows correct data from run
     * @author Elias Rinne and Elias Eide
     */
    @Override
    public void showResults() {
        try {
            Run run = rDao.find(1);
            if (run == null) {
                run = new Run(0,0,0,0,
                        0,0,0,0);
            }
            RunStatistics runStatistics = runStatisticsDao.find(1);
            if (runStatistics == null) {
                runStatistics = new RunStatistics(run,0,0,
                        0,0,0,
                        0,0,0,
                        0,0,0,
                        0,0,
                        0,0,0);
            }
            visualiseResults(run, runStatistics);
            resetSimulation();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Decreases simulation and animation speed
     * @author Elias Rinne and whoever made the base
     * @version 1.0
     */
    @Override
    public void decreaseSpeed() {
        engine.setDelay((long)(engine.getDelay()*1.10));
        ui.getVisualisation().scaleAnimationSpeed(1.1);
    }

    /**
     * Increases simulation and animation speed
     * @author Elias Rinne and whoever made the base
     * @version 1.0
     */
    @Override
    public void increaseSpeed() { // nopeutetaan moottorisäiettä
        engine.setDelay((long)(engine.getDelay()*0.9));
        ui.getVisualisation().scaleAnimationSpeed(0.9);
    }


    /* Simulation results passing to the UI
     * Because FX-UI updates come from engine thread, they need to be directed to the JavaFX thread
     */
    @Override
    public void showEndTime(double time) {
        Platform.runLater(()->ui.setEndingTime(time));
    }

    /**
     * Times the animation for customer counter
     */
    @Override
    public void visualiseCustomer() {
        Platform.runLater(() -> {
            ui.getVisualisation().newCustomer();
        });
    }
    /**
     * Times the animation for checkin
     * @author Elias Rinne
     */
    public void visualiseCheckIn() {
        Platform.runLater(() -> {
            ui.getVisualisation().newCustomerCheckin();
        });
    }

    /**
     * Times the animation for luggage drop
     * @author Elias Rinne
     */
    @Override
    public void visualiseLuggageDrop(double startX, double startY, boolean isPriority) {
        Platform.runLater(() -> {
            ui.getVisualisation().newCustomerLuggageDrop(startX, startY, isPriority);
        });
    }

    /**
     * Times the animation for security
     * @author Elias Rinne
     */
    @Override
    public void visualiseSecurity(boolean isPriority, EventType from) {
        Platform.runLater(() -> {
            ui.getVisualisation().customerAnimationToSecurity(isPriority, from);
        });
    }

    /**
     * Times the animation for passport
     * @author Elias Rinne
     */
    @Override
    public void visualisePassport(boolean isPriority, EventType from) {
        Platform.runLater(() -> {
            ui.getVisualisation().customerAnimationToPassport(isPriority,from);
        });
    }

    /**
     * Times the animation for gate
     * @author Elias Rinne
     */
    @Override
    public void visualiseGate(EventType from) {
        Platform.runLater(() -> {
            ui.getVisualisation().customerAnimationToGate(from);
        });
    }


    /**
     * After the simulation have bees finished opens result window for user
     * @param run is current run instance
     * @param runStats is current stats from the run
     * @author Elias Eide and Elias Rinne
     */
    public void visualiseResults(Run run, RunStatistics runStats) {
        Platform.runLater(() -> {
            ui.getVisualisation().resetCustomerCount();
            resetSimulation();
            ResultsController.open(null, run, runStats);
            ui.activateRestart(); //changes the Start button text to restart. No time to fix it to the presentation
        });
    }
}
