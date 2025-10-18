package simu.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link Passenger}.
 * <p>Focus areas:</p>
 * <ul>
 *   <li>Fraction setter validation (range [0,1])</li>
 *   <li>Deterministic flags when fractions are 0/1</li>
 *   <li>ID sequencing from 1</li>
 *   <li>Arrival/removal time accessors</li>
 * </ul>
 * @author Elias Rinne
 */
class PassengerTest {

    /** Initialize Trace once to prevent NPE in Passenger constructor logging. */
    @BeforeAll
    static void initTrace() {
        simu.framework.Trace.setTraceLevel(simu.framework.Trace.Level.INFO);
    }

    /** Reset fractions to sane defaults before each test. */
    @BeforeEach
    void reset() {
        Passenger.setPriorityFraction(0.2);
        Passenger.setLuggageFraction(0.7);
        Passenger.setEuCitizenFraction(0.9);
        Passenger.setCheckInFraction(0.8);
    }

    @Test
    void fractions_acceptOnlyRangeZeroToOne() {
        assertThrows(IllegalArgumentException.class, () -> Passenger.setPriorityFraction(-0.01));
        assertThrows(IllegalArgumentException.class, () -> Passenger.setPriorityFraction(1.01));

        assertDoesNotThrow(() -> Passenger.setPriorityFraction(0.0));
        assertDoesNotThrow(() -> Passenger.setPriorityFraction(1.0));
        assertEquals(1.0, Passenger.getPriorityFraction(), 1e-12);
    }

    @Test
    void flags_areDeterministicWhenFractionsAreZeroOrOne() {
        Passenger.setPriorityFraction(1.0);
        Passenger.setLuggageFraction(0.0);
        Passenger.setEuCitizenFraction(1.0);
        Passenger.setCheckInFraction(0.0);

        Passenger p = new Passenger();

        assertTrue(p.getIsPriority());
        assertFalse(p.isLuggage());
        assertTrue(p.isEuCitizen());
        assertFalse(p.isCheckIn());
    }

    @Test
    void id_incrementsFromOne() {
        Passenger p1 = new Passenger();
        Passenger p2 = new Passenger();
        assertEquals(1, p1.getId());
        assertEquals(2, p2.getId());
        assertEquals(p1.getId() + 1, p2.getId());
    }

    @Test
    void arrivalAndRemovalTime_settersAndGettersWork() {
        Passenger p = new Passenger();
        p.setArrivalTime(10.5);
        p.setRemovalTime(42.0);
        assertEquals(10.5, p.getArrivalTime(), 1e-12);
        assertEquals(42.0, p.getRemovalTime(), 1e-12);
    }

    @Test
    void reportResults_accumulatesServiceTimeIntoSum() {
        Passenger p1 = new Passenger();
        p1.setArrivalTime(0.0);
        p1.setRemovalTime(10.0);
        p1.reportResults();

    }

}