package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Represents a service point in the simulation.
 * Each service point has one or more queues where passengers wait,
 * and servers that process them. It also tracks statistics such as
 * maximum and average queue lengths.
 */
public class ServicePoint {
    private LinkedList<Passenger>[] queues; // array of queues
    private ContinuousGenerator generator;  // Random generator
    private EventList eventList;            // Event list for scheduling
    private EventType eventTypeScheduled;   // Event type for service completion
    private boolean[] reserved;             // Tracks which lines are busy
    private int maxLength = Integer.MIN_VALUE;
    private int minLength = Integer.MAX_VALUE;
    private long sampleCount = 0;
    private long sampleSum = 0;
    private double averageLength;

    /**
     * Create the service point with the given number of queues.
     *
     * @param generator  random number generator for service time simulation
     * @param eventList  simulator event list, needed for inserting service completion events
     * @param type       event type for the service end event
     * @param lineCount  number of lines in this service point
     * @throws IllegalArgumentException if lineCount <= 0
     */
    public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, int lineCount) {
        if (lineCount <= 0) {
            throw new IllegalArgumentException("lineCount must be > 0");
        }
        this.eventList = eventList;
        this.generator = generator;
        this.eventTypeScheduled = type;

        // reset stats
        this.maxLength = Integer.MIN_VALUE;
        this.minLength = Integer.MAX_VALUE;
        this.sampleCount = 0;
        this.sampleSum = 0;

        queues = new LinkedList[lineCount];
        reserved = new boolean[lineCount];
        for (int i = 0; i < lineCount; i++) {
            queues[i] = new LinkedList<>();
            reserved[i] = false;
        }
        sampleAllQueues();
    }

    /**
     * Add a passenger to the shortest available queue.
     *
     * @param a passenger to be queued
     */
    public void addQueue(Passenger a) { // The first customer of the queue is always in service
        int shortestQueue = queues[0].size();
        int shortestQueueIndex = 0;
        for (int i = 0; i < queues.length; i++) {
            int lengthOfQueue = queues[i].size();

            if (lengthOfQueue < shortestQueue) {
                shortestQueue = lengthOfQueue;
                shortestQueueIndex = i;
            }
            if (lengthOfQueue == 0) {
                shortestQueueIndex = i;
                break;
            }
        }
        queues[shortestQueueIndex].add(a);
        sampleAllQueues();
    }

    /**
     * Remove a passenger from the longest queue.
     * Updates statistics as well.
     *
     * @return passenger retrieved from the waiting queue, or null if all queues are empty
     */
    public Passenger removeQueue() {
        int longestQueue = -1;
        int longestQueueIndex = -1;
        for (int i = 0; i < queues.length; i++) {
            int lengthOfQueue = queues[i].size();

            if (lengthOfQueue > longestQueue) {
                longestQueue = lengthOfQueue;
                longestQueueIndex = i;
            }
        }
        if (longestQueueIndex != -1 && longestQueue > 0) {
            reserved[longestQueueIndex] = false;
            sampleAllQueues();
            return queues[longestQueueIndex].poll();
        }

        return null;
    }

    /**
     * Begins a new service on the given queue index.
     * Marks the line as reserved and schedules a completion event.
     *
     * @param lineIndex index of the queue to serve
     */
    public void beginService(int lineIndex) {
        // Begins a new service, customer is on the queue during the service
        Passenger p = queues[lineIndex].peek();
        if (p != null) {
            Trace.out(Trace.Level.INFO,
                    "Starting a new service for the customer #" + queues[lineIndex].peek().getId());
            reserved[lineIndex] = true;
            double serviceTime = generator.sample();
            eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getTime() + serviceTime));
            sampleAllQueues();
        }
    }

    /**
     * Check whether the given line is currently reserved (busy).
     *
     * @param lineIndex index of the queue
     * @return true if reserved, false otherwise
     */
    public boolean isReserved(int lineIndex) {
        return reserved[lineIndex];
    }

    /**
     * Check whether there are passengers waiting in the given queue.
     *
     * @param lineIndex index of the queue
     * @return true if the queue has passengers, false otherwise
     */
    public boolean isOnQueue(int lineIndex) {
        if (!queues[lineIndex].isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Check whether the given queue is empty.
     *
     * @param lineIndex index of the queue
     * @return true if empty, false otherwise
     */
    public boolean isQueueEmpty(int lineIndex) {
        return queues[lineIndex].isEmpty();
    }

    /**
     * Records current length to statistics and max length
     * @param length records current length to statistics
     */

    private void recordQueueLength(int length) {
        sampleSum += length;
        sampleCount++;
        if (length > maxLength) {
            maxLength = length;
        }
        if (length < minLength) {
            minLength = length;
        }
    }

    /**
     * Samples the length of all queues and updates statistics.
     */
    public void sampleAllQueues() {
        for (LinkedList<Passenger> q : queues) {
            recordQueueLength(q.size());
        }
    }

    /**
     * Get the number of lines (queues) in the service point.
     *
     * @return number of lines
     */
    public int getLineCount() {
        return queues.length;
    }

    /**
     * Get the maximum observed queue length.
     *
     * @return maximum queue length, or 0 if no samples exist
     */
    public int getMaxLength() {
        if (maxLength == Integer.MIN_VALUE) {
            return 0;
        } else {
            return maxLength;
        }
    }

    /**
     * Get the average queue length across all samples.
     *
     * @return average queue length, or 0.0 if no samples exist
     */
    public double getAverageLength() {
        if (sampleCount == 0) {
            return 0.0;
        } else {
            return (double) sampleSum / sampleCount;
        }
    }
}
