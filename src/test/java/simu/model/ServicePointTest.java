package simu.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the ServicePoint class.
 *
 * These tests verify queue assignment, service start and event scheduling,
 * queue removal, and statistics tracking.
 */
class ServicePointTest {

    private ContinuousGenerator generator;

    @BeforeEach
    void setUp() {
        // Return 1.0 from generator
        generator = new ContinuousGenerator() {
            @Override
            public double sample() { return 1.0; }
            @Override
            public void setSeed(long seed) { }
            @Override
            public long getSeed() { return 0; }
            @Override
            public void reseed() { }
        };

        // Turn on trace once for all tests
        Trace.setTraceLevel(Trace.Level.INFO);
    }

    @AfterEach
    void afterEach() {
        System.out.println("**--- Test finished ---**");
    }

    /**
     * Verifies that passengers are distributed into the shortest queues.
     */
    @Test
    void passengerGoesIntoShortestQueue() {
        EventList eventList = new EventList() {
            @Override
            public void add(Event e) { }
        };

        ServicePoint sp = new ServicePoint(generator, eventList, EventType.CHECK_IN, 4);

        Passenger p1 = new Passenger();
        Passenger p2 = new Passenger();
        Passenger p3 = new Passenger();
        Passenger p4 = new Passenger();

        sp.addQueue(p1);
        sp.addQueue(p2);
        sp.addQueue(p3);
        sp.addQueue(p4);

        // All queues should have one passenger
        assertFalse(sp.isQueueEmpty(0), "Queue 0 is empty");
        assertFalse(sp.isQueueEmpty(1), "Queue 1 is empty");
        assertFalse(sp.isQueueEmpty(2), "Queue 2 is empty");
        assertFalse(sp.isQueueEmpty(3), "Queue 3 is empty");
    }

    /**
     * Verifies that starting service reserves the queue
     * and schedules a completion event.
     */
    @Test
    void beginsServiceAndEventIsScheduledTest() {
        // Collecting events to list
        List<Event> capturedEvents = new ArrayList<>();
        EventList eventList = new EventList() {
            @Override
            public void add(Event e) {
                capturedEvents.add(e);
            }
        };

        ServicePoint sp = new ServicePoint(generator, eventList, EventType.CHECK_IN, 1);

        Passenger p = new Passenger();
        sp.addQueue(p);

        assertFalse(sp.isReserved(0), "Queue 0 should not be reserved yet");

        sp.beginService(0);

        assertTrue(sp.isReserved(0), "Queue 0 should be reserved");
        assertEquals(1, capturedEvents.size(), "One event should have be in list");
    }

    /**
     * Verifies that removing from the longest queue works correctly.
     */
    @Test
    void removeQueueTest() {
        EventList eventList = new EventList() {
            @Override
            public void add(Event e) { }
        };

        ServicePoint sp = new ServicePoint(generator, eventList, EventType.CHECK_IN, 4);

        Passenger p1 = new Passenger();
        Passenger p2 = new Passenger();
        Passenger p3 = new Passenger();
        Passenger p4 = new Passenger();

        sp.addQueue(p1);
        sp.addQueue(p2);
        sp.addQueue(p3);
        sp.addQueue(p4);

        Passenger removed = sp.removeQueue();

        // One passenger should have been removed from the longest queue
        assertNotNull(removed);

        // All queues should have one passenger, except queue 0
        assertTrue(sp.isQueueEmpty(0), "Queue 0 is not empty");
        assertFalse(sp.isQueueEmpty(1), "Queue 1 is empty");
        assertFalse(sp.isQueueEmpty(2), "Queue 2 is empty");
        assertFalse(sp.isQueueEmpty(3), "Queue 3 is empty");
    }

    /**
     * Verifies that statistics (max and average queue length) are updated.
     */
    @Test
    void statisticsUpdateTest() {
        EventList eventList = new EventList() {
            @Override
            public void add(Event e) { }
        };

        ServicePoint sp = new ServicePoint(generator, eventList, EventType.CHECK_IN, 2);

        sp.addQueue(new Passenger());
        sp.addQueue(new Passenger());
        sp.addQueue(new Passenger());

        // Max length should be 2 or more
        assertTrue(sp.getMaxLength() >= 2, "Max queue length should be at least 2");

        // Average length should be more than 0
        assertTrue(sp.getAverageLength() > 0, "Average length should be positive");
    }
}
