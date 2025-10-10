package controller;

/**
 * Base abstract methods for cUI
 * @author Elias Rinne, Elias Eide and whoever made the base
 */
public interface IControllerVtoM {
		public void startSimulation();
		public void increaseSpeed();
		public void decreaseSpeed();
        public void resetSimulation();
        public void showResults();
}
