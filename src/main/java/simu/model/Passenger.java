package simu.model;

import simu.framework.Clock;
import simu.framework.Trace;

import java.util.Random;

/**
 * Domain object representing a single passenger in the simulation.
 * <p>
 * A {@code Passenger} is stamped with its arrival time (from {@link Clock}),
 * assigned a running identifier, and randomly designated with several boolean
 * attributes based on configurable Bernoulli fractions:
 * priority status, whether check-in is required, whether luggage drop is needed,
 * and whether the passenger is an EU citizen.
 * </p>
 *
 * <h3>Randomness & fractions</h3>
 * <ul>
 *   <li>Fractions are global (static) and apply to all new passengers.</li>
 *   <li>Values ≤ 0 produce {@code false}, values ≥ 1 produce {@code true}, otherwise
 *       a sample is drawn from a shared {@link Random}.</li>
 *   <li>When a fraction is exactly 0 or 1, outcomes are deterministic.</li>
 * </ul>
 *
 * <h3>Logging</h3>
 * The constructor logs a creation message via {@link Trace}. Ensure the trace
 * level is initialized before constructing instances in tests.
 *
 * <h3>Thread-safety</h3>
 * This class is <em>not</em> thread-safe due to shared static state
 * (identifier counter and cumulative {@code sum}). If you use it from multiple
 * threads, guard updates externally or confine usage to a single thread.
 */
public class Passenger {
    /** Simulation time when the passenger arrived. */
    private double arrivalTime;

    /** Simulation time when the passenger left the system. */
    private double removalTime;

    /** Sequential identifier assigned at construction. */
    private int id;

    /** Global counter for assigning sequential ids (starts from 1). */
    private static int i = 1;

    /** Cumulative time spent in the system (sum of removal-arrival over all reported passengers). */
    private static double sum = 0;

    /** Whether the passenger has priority status. */
    private boolean isPriority;

    /** Whether the passenger needs check-in. */
    private boolean checkIn;

    /** Whether the passenger needs luggage drop. */
    private boolean luggage;

    /** Whether the passenger is an EU citizen (affects passport control). */
    private boolean euCitizen;

    /** Global fraction for assigning priority status. */
    private static double priorityFraction = 0.2;

    /** Global fraction for assigning luggage-drop requirement. */
    private static double luggageFraction = 0.7;

    /** Global fraction for assigning EU citizenship. */
    private static double euCitizenFraction = 0.9;

    /** Global fraction for assigning check-in requirement. */
    private static double checkInFraction = 0.8;

    /** Shared PRNG for all Bernoulli decisions. */
    private static final Random RAND = new Random();

    /**
     * Constructs a new passenger, assigns a sequential id, draws attributes
     * from the configured fractions, and stamps the current arrival time from {@link Clock}.
     * A creation message is logged via {@link Trace}.
     */
    public Passenger() {
        id = i++;
        this.isPriority = decideByFraction(priorityFraction);
        this.checkIn = decideByFraction(checkInFraction);
        this.luggage = decideByFraction(luggageFraction);
        this.euCitizen = decideByFraction(euCitizenFraction);
        arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "New  #" + id + " arrived at  " + arrivalTime);
    }

    /**
     * Returns a boolean outcome for a Bernoulli trial with probability {@code fraction}.
     * <ul>
     *   <li>If {@code fraction <= 0}, returns {@code false}.</li>
     *   <li>If {@code fraction >= 1}, returns {@code true}.</li>
     *   <li>Otherwise samples {@link #RAND} &lt; {@code fraction}.</li>
     * </ul>
     *
     * @param fraction probability in [0,1]
     * @return {@code true} with the given probability
     */
    private static boolean decideByFraction(double fraction) {
        if (fraction <= 0.0) return false;
        if (fraction >= 1.0) return true;
        return RAND.nextDouble() < fraction;
    }

    /**
     * Sets the global fraction for priority assignment.
     *
     * @param f probability in [0, 1]
     * @throws IllegalArgumentException if {@code f} is not within [0, 1]
     */
    public static void setPriorityFraction(double f) {
        if (f < 0.0 || f > 1.0) throw new IllegalArgumentException("priorityFraction must be between 0.0 and 1.0");
        priorityFraction = f;
    }

    /**
     * Sets the global fraction for luggage-drop assignment.
     *
     * @param f probability in [0, 1]
     * @throws IllegalArgumentException if {@code f} is not within [0, 1]
     */
    public static void setLuggageFraction(double f) {
        if (f < 0.0 || f > 1.0) throw new IllegalArgumentException("luggageFraction must be between 0.0 and 1.0");
        luggageFraction = f;
    }

    /**
     * Sets the global fraction for EU-citizenship assignment.
     *
     * @param f probability in [0, 1]
     * @throws IllegalArgumentException if {@code f} is not within [0, 1]
     */
    public static void setEuCitizenFraction(double f) {
        if (f < 0.0 || f > 1.0) throw new IllegalArgumentException("euCitizenFraction must be between 0.0 and 1.0");
        euCitizenFraction = f;
    }

    /**
     * Sets the global fraction for check-in assignment.
     *
     * @param f probability in [0, 1]
     * @throws IllegalArgumentException if {@code f} is not within [0, 1]
     */
    public static void setCheckInFraction(double f) {
        if (f < 0.0 || f > 1.0) throw new IllegalArgumentException("checkInFraction must be between 0.0 and 1.0");
        checkInFraction = f;
    }

    /**
     * @return the current global priority fraction
     */
    public static double getPriorityFraction() { return priorityFraction; }

    /**
     * @return the time this passenger left the system (simulation time units)
     */
    public double getRemovalTime() {
        return removalTime;
    }

    /**
     * @return whether this passenger has priority status
     */
    public boolean getIsPriority(){
        return isPriority;
    }

    /**
     * @return whether this passenger needs check-in
     */
    public boolean isCheckIn() {
        return checkIn;
    }

    /**
     * @return whether this passenger needs luggage drop
     */
    public boolean isLuggage() {
        return luggage;
    }

    /**
     * @return whether this passenger is an EU citizen
     */
    public boolean isEuCitizen() {
        return euCitizen;
    }

    /**
     * Sets the time this passenger left the system.
     *
     * @param removalTime simulation time at removal
     */
    public void setRemovalTime(double removalTime) {
        this.removalTime = removalTime;
    }

    /**
     * @return the time this passenger arrived (simulation time units)
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the arrival time (primarily useful for tests).
     *
     * @param arrivalTime simulation time at arrival
     */
    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return the sequential identifier assigned at construction
     */
    public int getId() {
        return id;
    }

    /**
     * Reports this passenger's timing results to the trace and updates the global
     * cumulative service time.
     * <p>
     * Logs arrival, removal, and residence time (removal - arrival) via {@link Trace}.
     * Adds the residence time to the global {@code sum}, computes a simple running
     * mean as {@code sum / id}, and prints it to {@code System.out}.
     * </p>
     * <p><b>Note:</b> The computed mean uses the per-instance {@link #id} as the divisor.
     * This matches a “first N passengers” average only if every passenger up to this id
     * has called {@code reportResults()} exactly once.</p>
     */
    public void reportResults() {
        Trace.out(Trace.Level.INFO, "\nPassenger " + id + " ready! ");
        Trace.out(Trace.Level.INFO, "Passenger "   + id + " arrived: " + arrivalTime);
        Trace.out(Trace.Level.INFO,"Passenger "    + id + " removed: " + removalTime);
        Trace.out(Trace.Level.INFO,"Passenger "    + id + " stayed: "  + (removalTime - arrivalTime));

        sum += (removalTime - arrivalTime);
        double mean = sum/id;
        System.out.println("Current mean of the Passenger service times " + mean);
    }
}
