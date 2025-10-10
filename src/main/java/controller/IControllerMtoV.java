package controller;

import javafx.application.Platform;
import simu.entity.Run;
import simu.entity.RunStatistics;
import simu.model.EventType;

/**
 * Base abstract methods for controller
 * @author Elias Rinne and whoever made the base
 */
public interface IControllerMtoV {
		public void showEndTime(double time);
		public void visualiseCustomer();
        public void visualiseCheckIn();
        public void visualiseLuggageDrop(double startX, double startY, boolean isPriority);
        public void visualiseSecurity(boolean isPriority, EventType from);
        public void visualisePassport(boolean isPriority, EventType from);
        public void visualiseGate(EventType from);
        public void visualiseResults(Run run, RunStatistics runStatistics);
}
