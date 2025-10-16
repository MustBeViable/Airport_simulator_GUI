package view;

import simu.model.EventType;



/**
 * Abstraction for the visualization/animation layer of the airport simulation.
 * Implementations render customer movements between service points (check-in, luggage drop,
 * security, passport control, gate) and may also draw overlays like counters. The interface is
 * UI-toolkit-agnostic in signature, but in this project implementations are JavaFX-based
 * canvases.
 * @author Elias Rinne
 */

public interface IVisualisation {
    /**
     * Clears the visualization surface and resets any transient drawings.
     */
	public void clearDisplay();
    /**
     * Signals that a new customer has appeared. Implementations may increment and render
     * a counter or draw an entry animation.
     */
	public void newCustomer();
    /**
     * Animates a new customer entering the check-in area.
     * Implementations define the precise start/end coordinates.
     */
    public void newCustomerCheckin();
    /**
     * Animates a customer moving to the luggage-drop area from an arbitrary starting point.
     * The destination may differ for priority vs. standard lanes.
     *
     * @param startX starting X coordinate in canvas pixels
     * @param startY starting Y coordinate in canvas pixels
     * @param isPriority whether the customer uses the priority luggage-drop lane
     */
    public void newCustomerLuggageDrop(double startX, double startY, boolean isPriority);
    /**
     * Animates a customer moving to security. The start location is derived from the given
     * source {@code from}, and the destination may depend on {@code isPriority}.
     *
     * @param isPriority whether the customer uses the priority security lane
     * @param from the source stage the customer arrives from (e.g., ARR1, CHECK_IN,
     * LUGGAGE_DROP, LUGGAGE_DROP_PRIORITY)
     */
    public void customerAnimationToSecurity(boolean isPriority, EventType from);
    /**
     * Animates a customer moving from security to passport control.
     *
     * @param isPriority whether the customer uses the priority passport-control lane
     * @param from the source security lane (SECURITY or SECURITY_PRIORITY)
     */
    public void customerAnimationToPassport(boolean isPriority, EventType from);
    /**
     * Animates a customer moving from security or passport control to the gate area.
     *
     * @param from source stage: SECURITY / SECURITY_PRIORITY / PASSPORT_CONTROL /
     * PASSPORT_CONTROL_PRIORITY
     */
    public void customerAnimationToGate(EventType from);
    /**
     * Resets any in-memory counters or overlays related to customers.
     */
    public void resetCustomerCount();
    /**
     * Scales the animation speed by a multiplier. Values less than 1.0 typically speed up the
     * animation; values greater than 1.0 slow it down. Implementations may clamp to reasonable
     * bounds to avoid imperceptible or excessively fast animations.
     *
     * @param Multiplier factor by which to scale the current animation speed/duration
     */
    public void scaleAnimationSpeed(double Multiplier);
    /**
     * Sets the base animation duration in seconds used by implementations to parameterize
     * per-leg movement. The exact interpretation may vary (e.g., total leg time vs. per-frame
     * pacing); implementations should document their behavior.
     *
     * @param duration base duration in seconds
     */
    public void setDuration(int duration);
}

