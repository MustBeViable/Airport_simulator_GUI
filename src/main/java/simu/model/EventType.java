package simu.model;

import simu.framework.IEventType;

/**
 * Enumerate type for simulation.
 * @author Sakari Honkavaara, Elias Rinne, Elias Eide
 */
public enum EventType implements IEventType {
    ARR1,
    CHECK_IN,
    SECURITY,
    PASSPORT_CONTROL,
    GATE,
    LUGGAGE_DROP,
    LUGGAGE_DROP_PRIORITY,
    SECURITY_PRIORITY,
    PASSPORT_CONTROL_PRIORITY
}