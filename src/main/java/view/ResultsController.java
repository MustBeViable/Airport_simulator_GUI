package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simu.entity.Run;
import simu.entity.RunStatistics;

import java.io.IOException;

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
