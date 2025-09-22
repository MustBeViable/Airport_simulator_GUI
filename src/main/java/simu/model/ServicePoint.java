package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class ServicePoint {
    private LinkedList<Passenger>[] queues; // Data Structure used
    private ContinuousGenerator generator;
    private EventList eventList;
    private EventType eventTypeScheduled;
    private boolean[] reserved ;
    private int maxLenght;
    private int minLength;
    private double averageLength;
    //private boolean reserved = false;

    /* Random number generator for choosing a queue, CAN BE CHANGED */
    private Random rand = new Random();
    private ArrayList<Integer> queueLengths = new ArrayList<>();
    //Queuestrategy strategy; // option: ordering of the customer



    /**
     * Create the service point with lines with waiting queue.
     *
     * @param generator Random number generator for service time simulation
     * @param eventList Simulator event list, needed for the insertion of service ready event
     * @param type Event type for the service end event
     * queues, number of lines withwaiting queue
     * reserved, array indicating whether a line is busy or not
     */
    public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type, int lineCount){
        this.eventList = eventList;
        this.generator = generator;
        this.eventTypeScheduled = type;
        queues = new LinkedList[lineCount];
        reserved = new boolean[lineCount];
        for (int i=0; i<lineCount;i++){
            queues[i] = new LinkedList<>();
            this.queueLengths.add(queues[i].toArray().length);    //lisää sen hetkisen jonon listaa
            reserved[i] = false;
        }
    }

    /**
     * Add a customer to the service point queue.
     *
     * @param a Customer to be queued
     */
    public void addQueue(Passenger a) {	// The first customer of the queue is always in service, also added to random line
        int shortestQueue=queues[0].size();
        int shortestQueueIndex=0;
        for (int i=0; i<queues.length; i++){
            int lengthOfQueue=queues[i].size();

            if(lengthOfQueue<shortestQueue){
                shortestQueue=lengthOfQueue;
                shortestQueueIndex=i;
            }
            if (lengthOfQueue==0) {
                shortestQueueIndex = i;
                break;
            }
        }
        queues[shortestQueueIndex].add(a);
    }

    /**
     * Remove customer from the waiting queue.
     * Here we calculate also the appropriate measurement values.
     *
     * @return Customer retrieved from the waiting queue
     */
    public Passenger removeQueue() {
        int longestQueue=-1;
        int longestQueueIndex=-1;
        for (int i=0; i<queues.length; i++){
            int lengthOfQueue=queues[i].size();

            if(lengthOfQueue>longestQueue){
                longestQueue=lengthOfQueue;
                longestQueueIndex=i;
            }
            System.out.println(lengthOfQueue+"test");
        }
        if (longestQueueIndex!=-1 && longestQueue>0) {
            reserved[longestQueueIndex]=false;
            return queues[longestQueueIndex].poll();

        }

        return null;
    }

    /**
     * Begins a new service, customer is on the queue during the service
     *
     * Inserts a new event to the event list when the service should be ready.
     */
    public void beginService(int lineIndex) {
        // Begins a new service, customer is on the queue during the service
        Passenger p= queues[lineIndex].peek();
        if (p!=null) {
            Trace.out(Trace.Level.INFO, "Starting a new service for the customer #" + queues[lineIndex].peek().getId());
            reserved[lineIndex] = true;
            double serviceTime = generator.sample();
            eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getTime() + serviceTime));

        }
    }

    /**
     * Check whether the service point is busy
     *
     * @return logical value indicating service state
     */
    public boolean isReserved(int lineIndex){
        return reserved[lineIndex];
    }

    /**
     * Check whether there is customers on the waiting queue
     *
     * @return logival value indicating queue status
     */
    public boolean isOnQueue(int lineIndex) {
        if (!queues[lineIndex].isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isQueueEmpty(int lineIndex) {
        return queues[lineIndex].isEmpty();
    }

    //Pitäsköhä servicepointit kaikki olla jossai listassa? katotaan miten sakke o sen tehny
    // to-do: selvitä miten sakke luo ja hallinnoi listoi
    /*
    public void addLenghtToQueueLengths() {
        queueLengths.add(queue.toArray().length);
    }
*/
    private void addQueueLength(int length) {
        this.queueLengths.add(length);
    }

    public void findMinMaxLengths() {
        int shortest = Integer.MAX_VALUE;
        int longest = Integer.MIN_VALUE;
        int sum = 0;
        for (Integer length : queueLengths) {
            sum += length;
            if (length < shortest) {
                shortest = length;
            } if (length > longest) {
                longest =  length;
            }
        }
        this.maxLenght = longest;
        this.minLength = shortest;
        this.averageLength = (double) sum / queueLengths.size();
    }

    public int getMaxLenght() {
        return maxLenght;
    }

    public int getLineCount() {
        return queues.length;
    }

    public int getMinLength() {
        return minLength;
    }

    public double getAverageLength() {
        return averageLength;
    }

}
/**
 * Get the number of lines (queues) in the service point
 *
 * @return number of lines
 */