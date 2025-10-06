package view;

import simu.model.EventType;

public interface IVisualisation {
	public void clearDisplay();
	public void newCustomer();
    public void newCustomerCheckin();
    public void newCustomerLuggageDrop(double startX, double startY, boolean isPriority);
    public void customerAnimationToSecurity(boolean isPriority, EventType from);
    public void customerAnimationToPassport(boolean isPriority, EventType from);
    public void customerAnimationToGate(EventType from);
    public void resetCustomerCount();
}

