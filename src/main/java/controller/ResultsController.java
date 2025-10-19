package controller;

import dao.RunDao;
import dao.RunStatisticsDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simu.model.entity.Run;
import simu.model.entity.RunStatistics;

import java.io.IOException;
import java.util.List;

public class ResultsController {

    @FXML private Label checkInMaxLabel;
    @FXML private Label checkInAvgLabel;

    @FXML private Label luggageMaxLabel;
    @FXML private Label luggageAvgLabel;

    @FXML private Label luggPriMaxLabel;
    @FXML private Label luggPriAvgLabel;

    @FXML private Label securityMaxLabel;
    @FXML private Label securityAvgLabel;

    @FXML private Label secPriMaxLabel;
    @FXML private Label secPriAvgLabel;

    @FXML private Label passportMaxLabel;
    @FXML private Label passportAvgLabel;

    @FXML private Label passPriMaxLabel;
    @FXML private Label passPriAvgLabel;

    @FXML private Label gateMaxLabel;
    @FXML private Label gateAvgLabel;

    @FXML private Label runIdLabel;
    @FXML private Label checkInCountLabel;
    @FXML private Label luggageCountLabel;
    @FXML private Label priorityLuggageCountLabel;
    @FXML private Label securityCountLabel;
    @FXML private Label prioritySecurityCountLabel;
    @FXML private Label passportCountLabel;
    @FXML private Label priorityPassportCountLabel;
    @FXML private Label gateCountLabel;

    @FXML private ListView<Run> listView;
    private final ObservableList<Run> observableList = FXCollections.observableArrayList();
    private final RunDao runDao = new RunDao();
    private final RunStatisticsDao runStatisticsDao = new RunStatisticsDao();


    public void initialize() {
        setListView();
    }
    // populate labels from your entity getters
    public void setData(Run run, RunStatistics s) {
        // Assumes RunStatistics has getters matching these names; adapt if different
        checkInMaxLabel.setText(String.valueOf(s.getCheckInQueueMaxLength()));
        checkInAvgLabel.setText(String.format("%.2f", s.getCheckInQueueAverageLength()));

        luggageMaxLabel.setText(String.valueOf(s.getLuggageDropQueueMaxLength()));
        luggageAvgLabel.setText(String.format("%.2f", s.getLuggageDropQueueAverageLength()));

        luggPriMaxLabel.setText(String.valueOf(s.getPriorityLuggageDropQueueMaxLength()));
        luggPriAvgLabel.setText(String.format("%.2f", s.getPriorityLuggageDropQueueAverageLength()));

        securityMaxLabel.setText(String.valueOf(s.getSecurityQueueMaxLength()));
        securityAvgLabel.setText(String.format("%.2f", s.getSecurityQueueAverageLength()));

        secPriMaxLabel.setText(String.valueOf(s.getPrioritySecurityQueueMaxLength()));
        secPriAvgLabel.setText(String.format("%.2f", s.getPrioritySecurityQueueAverageLength()));

        passportMaxLabel.setText(String.valueOf(s.getPassportControlQueueMaxLength()));
        passportAvgLabel.setText(String.format("%.2f", s.getPassportControlQueueAverageLength()));

        passPriMaxLabel.setText(String.valueOf(s.getPriorityPassportControlQueueMaxLength()));
        passPriAvgLabel.setText(String.format("%.2f", s.getPriorityPassportControlQueueAverageLength()));

        gateMaxLabel.setText(String.valueOf(s.getGateQueueMaxLength()));
        gateAvgLabel.setText(String.format("%.2f", s.getGateQueueAverageLength()));
    }

    private void setRunInfo(Run r) {
        if (r == null) {
            clearRunInfo();
            return;
        }
        runIdLabel.setText(String.valueOf(r.getId()));
        checkInCountLabel.setText(String.valueOf(r.getCheckInQueuesCount()));
        luggageCountLabel.setText(String.valueOf(r.getLuggageDropCount()));
        priorityLuggageCountLabel.setText(String.valueOf(r.getPriorityLuggageDropCount()));
        securityCountLabel.setText(String.valueOf(r.getSecurityCount()));
        prioritySecurityCountLabel.setText(String.valueOf(r.getPrioritySecurityCount()));
        passportCountLabel.setText(String.valueOf(r.getPassportControlCount()));
        priorityPassportCountLabel.setText(String.valueOf(r.getPriorityPassportControlCount()));
        gateCountLabel.setText(String.valueOf(r.getGateCount()));
    }

    private void clearRunInfo() {
        runIdLabel.setText("");
        checkInCountLabel.setText("");
        luggageCountLabel.setText("");
        priorityLuggageCountLabel.setText("");
        securityCountLabel.setText("");
        prioritySecurityCountLabel.setText("");
        passportCountLabel.setText("");
        priorityPassportCountLabel.setText("");
        gateCountLabel.setText("");
    }

    public void setListView() {
        List<Run> runList = null;
        try {
            runList = runDao.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (runList != null) {
            observableList.setAll(runList);
        }
        listView.setItems(observableList);

        listView.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Run n, boolean empty) {
                super.updateItem(n, empty);
                if (empty || n == null) {
                    setText(null);
                } else {
                    setText("run" + n.getId() + ".");
                }
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldRun, newRun) -> {
            if (newRun == null) {
                clearLabels();
                clearRunInfo();
                return;
            }
            setRunInfo(newRun); // RIGHT panel

            // IMPORTANT: fetch by run_id (NOT RunStatistics.id)
            RunStatistics stats = runStatisticsDao.find(newRun.getId());
            if (stats != null) {
                setData(newRun, stats);
            } else {
                clearLabels();
            }
        });

        if (!observableList.isEmpty()) {
            listView.getSelectionModel().selectFirst();
        }
    }

    private void clearLabels() {
        checkInMaxLabel.setText("");
        checkInAvgLabel.setText("");
        luggageMaxLabel.setText("");
        luggageAvgLabel.setText("");
        luggPriMaxLabel.setText("");
        luggPriAvgLabel.setText("");
        securityMaxLabel.setText("");
        securityAvgLabel.setText("");
        secPriMaxLabel.setText("");
        secPriAvgLabel.setText("");
        passportMaxLabel.setText("");
        passportAvgLabel.setText("");
        passPriMaxLabel.setText("");
        passPriAvgLabel.setText("");
        gateMaxLabel.setText("");
        gateAvgLabel.setText("");
    }


    @FXML
    private void onClose() {
        Stage stage = (Stage) checkInMaxLabel.getScene().getWindow();
        stage.close();
    }

    public static void open(Stage owner, Run run, RunStatistics stats) {
        try {
            FXMLLoader loader = new FXMLLoader(ResultsController.class.getResource("/results.fxml"));
            Scene scene = new Scene(loader.load());
            ResultsController controller = loader.getController();
            controller.setData(run, stats);

            Stage stage = new Stage();
            if (owner != null) {
                stage.initOwner(owner);
            }
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Simulation results");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
