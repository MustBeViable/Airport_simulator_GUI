package controller;

import javafx.application.Platform;
import simu.model.EventType;

/* interface for the engine */
public interface IControllerMtoV {
		public void showEndTime(double time);
		public void visualiseCustomer();
        public void visualiseCheckIn();
        public void visualiseLuggageDrop(double startX, double startY, boolean isPriority);
        public void visualiseSecurity(boolean isPriority, EventType from);
        public void visualisePassport(boolean isPriority, EventType from);
        public void visualiseGate(EventType from);
}
