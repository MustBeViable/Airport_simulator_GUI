package simu.model;

import controller.IControllerMtoV;
import dao.RunDao;
import dao.RunStatisticsDao;
import eduni.distributions.*;
import simu.entity.Run;
import simu.entity.RunStatistics;
import simu.framework.ArrivalProcess;
import simu.framework.Clock;
import simu.framework.Engine;
import simu.framework.Event;

import java.util.Random;

import static simu.model.EventType.*;

/**
 * Discrete-event simulation engine for the airport flow.
 * <p>
 * This engine wires together an {@link ArrivalProcess}, a fixed set of {@link ServicePoint}s
 * (one logical station per {@link EventType}), and a UI controller ({@link IControllerMtoV})
 * for visualization callbacks. It also persists the configured line counts and measured
 * queue statistics via {@link RunDao} and {@link RunStatisticsDao} at the end of the run.
 * </p>
 *
 * <h2>Service point layout (8 entries)</h2>
 * <ol>
 * <li>index 0: {@link EventType#CHECK_IN}</li>
 * <li>index 1: {@link EventType#LUGGAGE_DROP}</li>
 * <li>index 2: {@link EventType#LUGGAGE_DROP_PRIORITY}</li>
 * <li>index 3: {@link EventType#SECURITY}</li>
 * <li>index 4: {@link EventType#SECURITY_PRIORITY}</li>
 * <li>index 5: {@link EventType#PASSPORT_CONTROL}</li>
 * <li>index 6: {@link EventType#PASSPORT_CONTROL_PRIORITY}</li>
 * <li>index 7: {@link EventType#GATE}</li>
 * </ol>
 *
 * <h3>Distributions</h3>
 * <p>
 * In <em>text demo</em> mode ({@link #TEXTDEMO}), arrival/service-time generation can be toggled between
 * fixed and random using {@link #FIXEDARRIVALTIMES} and {@link #FXIEDSERVICETIMES}.
 * In the realistic mode (default), each station uses a domain-appropriate distribution
 * (e.g., {@link LogNormal}, {@link Gamma}, {@link TruncatedNormal}, {@link Normal}).
 * </p>
 *
 * <h3>Threading</h3>
 * <p>
 * The engine itself runs on its own thread (see {@link Engine}), while the controller
 * callbacks should marshal UI updates to the JavaFX Application Thread as needed.
 * </p>
 */

public class MyEngine extends Engine {
    /** Generates arrivals into the system ({@link EventType#ARR1}). */
    private ArrivalProcess arrivalProcess;
    /** All service points (8 stations; see class javadoc for index mapping). */
    private ServicePoint[] servicePoints;
    /** Number of configured service points (always 8 in this model). */
    private final int SERVICE_POINT_COUNT;
    /** Initial line counts per station; saved with the run results. */
    private final int[] initialLineCounts;
    public static final boolean TEXTDEMO = false;
    /** If {@code true} in text demo mode, use fixed inter-arrival times. */
    public static final boolean FIXEDARRIVALTIMES = false;
    /** If {@code true} in text demo mode, use fixed service times (note: field name contains a typo). */
    public static final boolean FXIEDSERVICETIMES = false;
    /** DAO for persisting per-run queue statistics. */
    private RunStatisticsDao runStatisticsDao=new RunStatisticsDao();
    /** DAO for persisting run configuration (line counts). */
    private RunDao runDao = new RunDao();

    /**
     * Default constructor keeps previous defaults.
     * @param controller UI/controller callback interface used for visualization updates
     */
    public MyEngine(IControllerMtoV controller) {
        this(controller, new int[]{1,1,1,1,1,1,1,1});
    }

    /**
     * New constructor: accept line counts for each service point.
     * Expects an array of length 8 (the rest of the code assumes 8 service points).
     * @param controller UI/controller callback interface used for visualization updates
     * @param lineCounts array of length 8 containing the number of parallel lines (servers)
     * for each station; see class javadoc for index mapping
     * @throws IllegalArgumentException if {@code lineCounts} is {@code null} or its length is not 8
     */
    public MyEngine(IControllerMtoV controller, int[] lineCounts) {
        super(controller);
        if (lineCounts == null || lineCounts.length != 8) {
            throw new IllegalArgumentException("lineCounts must be non-null and length 8");
        }
        this.initialLineCounts = lineCounts.clone();
        this.SERVICE_POINT_COUNT = initialLineCounts.length;
        servicePoints = new ServicePoint[SERVICE_POINT_COUNT];

        if (TEXTDEMO) {
            Random r = new Random();

            ContinuousGenerator arrivalTime = null;
            if (FIXEDARRIVALTIMES) {
                arrivalTime = new ContinuousGenerator() {
                    @Override
                    public double sample() { return 10; }
                    @Override public void setSeed(long seed) {}
                    @Override public long getSeed() { return 0; }
                    @Override public void reseed() {}
                };
            } else
                arrivalTime = new Negexp(10, Integer.toUnsignedLong(r.nextInt()));

            ContinuousGenerator serviceTime = null;
            if (FXIEDSERVICETIMES) {
                serviceTime = new ContinuousGenerator() {
                    @Override public double sample() { return 9; }
                    @Override public void setSeed(long seed) {}
                    @Override public long getSeed() { return 0; }
                    @Override public void reseed() {}
                };
            } else
                serviceTime = new Normal(10, 6, Integer.toUnsignedLong(r.nextInt()));

            // use provided line counts
            servicePoints[0] = new ServicePoint(serviceTime, eventList, EventType.CHECK_IN, initialLineCounts[0]);
            servicePoints[1] = new ServicePoint(serviceTime, eventList, EventType.LUGGAGE_DROP, initialLineCounts[1]);
            servicePoints[2] = new ServicePoint(serviceTime, eventList, EventType.LUGGAGE_DROP_PRIORITY, initialLineCounts[2]);
            servicePoints[3] = new ServicePoint(serviceTime, eventList, EventType.SECURITY, initialLineCounts[3]);
            servicePoints[4] = new ServicePoint(serviceTime, eventList, EventType.SECURITY_PRIORITY, initialLineCounts[4]);
            servicePoints[5] = new ServicePoint(serviceTime, eventList, EventType.PASSPORT_CONTROL, initialLineCounts[5]);
            servicePoints[6] = new ServicePoint(serviceTime, eventList, EventType.PASSPORT_CONTROL_PRIORITY, initialLineCounts[6]);
            servicePoints[7] = new ServicePoint(serviceTime, eventList, EventType.GATE, initialLineCounts[7]);

            arrivalProcess = new ArrivalProcess(arrivalTime, eventList, ARR1);
        } else {
            // realistic case, use line counts
            servicePoints[0] = new ServicePoint(new LogNormal(2.3, 0.5), eventList, EventType.CHECK_IN, initialLineCounts[0]);
            servicePoints[1] = new ServicePoint(new Gamma(2.0, 5.0), eventList, EventType.LUGGAGE_DROP, initialLineCounts[1]);
            servicePoints[2] = new ServicePoint(new Gamma(2.0, 5.0), eventList, EventType.LUGGAGE_DROP_PRIORITY, initialLineCounts[2]);
            servicePoints[3] = new ServicePoint(new TruncatedNormal(12, 6), eventList, EventType.SECURITY, initialLineCounts[3]);
            servicePoints[4] = new ServicePoint(new Normal(8, 4), eventList, EventType.SECURITY_PRIORITY, initialLineCounts[4]);
            servicePoints[5] = new ServicePoint(new LogNormal(2.1, 0.7), eventList, EventType.PASSPORT_CONTROL, initialLineCounts[5]);
            servicePoints[6] = new ServicePoint(new LogNormal(2.1, 0.7), eventList, EventType.PASSPORT_CONTROL_PRIORITY, initialLineCounts[6]);
            servicePoints[7] = new ServicePoint(new Normal(5, 1), eventList, EventType.GATE, initialLineCounts[7]);

            arrivalProcess = new ArrivalProcess(new Negexp(15, 5), eventList, ARR1);
        }
    }

    /**
     * A-phase: schedules the very first arrival into the system.
     * Called by the {@link Engine} lifecycle when the simulation starts.
     */
    @Override
    protected void initialization() {    // First arrival in the system
        arrivalProcess.generateNext();
    }

    /**
     * B-phase: handles a single event from the event list (routing logic and visualization side-effects).
     * <p>
     * For arrivals ({@link EventType#ARR1}), a new {@link Passenger} is created and routed to
     * check-in, luggage drop, or security depending on {@link Passenger#isCheckIn()},
     * {@link Passenger#isLuggage()} and priority status. Downstream events pop a passenger from
     * the corresponding {@link ServicePoint} queue and route to the next station.
     * </p>
     *
     * @param t the event to be processed; its type must be an {@link EventType}
     */
    @Override
    protected void runEvent(Event t) {  // B phase events
        switch ((EventType) t.getType()) {
            case ARR1 -> {
                // create passenger and route once
                Passenger p = new Passenger();

                if (p.isCheckIn()) {
                    // needs check-in first
                    servicePoints[0].addQueue(p);
                    controller.visualiseCheckIn();
                } else if (p.getIsPriority()) {
                    // priority passenger goes to priority luggage or priority security
                    if (p.isLuggage()) {
                        controller.visualiseLuggageDrop(125,280, p.getIsPriority());
                        servicePoints[2].addQueue(p); // LUGGAGE_DROP_PRIORITY
                    } else {
                        controller.visualiseSecurity(true, CHECK_IN);
                        controller.visualiseSecurity(p.getIsPriority(), ARR1);
                        servicePoints[4].addQueue(p); // SECURITY_PRIORITY
                    }
                } else {
                    // normal passenger: luggage or normal security
                    if (p.isLuggage()) {
                        controller.visualiseLuggageDrop(125,280, p.getIsPriority());
                        servicePoints[1].addQueue(p); // LUGGAGE_DROP
                    } else {
                        controller.visualiseSecurity(false, CHECK_IN);
                        controller.visualiseSecurity(p.getIsPriority(), ARR1);
                        servicePoints[3].addQueue(p); // SECURITY
                    }
                }
                arrivalProcess.generateNext();
                controller.visualiseCustomer();
            }

            case CHECK_IN -> {
                Passenger p = servicePoints[0].removeQueue();
                if (p == null) break;
                // after check-in route based on priority and luggage flag
                if (p.getIsPriority()) {
                    if (p.isLuggage()) {
                        controller.visualiseLuggageDrop(375,280, p.getIsPriority());
                        servicePoints[2].addQueue(p); // LUGGAGE_DROP_PRIORITY
                    } else {
                        controller.visualiseSecurity(p.getIsPriority(), CHECK_IN);
                        servicePoints[4].addQueue(p); // SECURITY_PRIORITY
                    }
                } else {
                    if (p.isLuggage()) {
                        controller.visualiseLuggageDrop(375,280, p.getIsPriority());
                        servicePoints[1].addQueue(p); // LUGGAGE_DROP
                    } else {
                        controller.visualiseSecurity(false, EventType.LUGGAGE_DROP);
                        servicePoints[3].addQueue(p); // SECURITY
                    }
                }
            }

            case LUGGAGE_DROP -> {
                Passenger p = servicePoints[1].removeQueue();
                if (p == null) break;
                // after normal luggage, go to normal security
                controller.visualiseSecurity(false, EventType.LUGGAGE_DROP);
                servicePoints[3].addQueue(p); // SECURITY
            }

            case LUGGAGE_DROP_PRIORITY -> {
                Passenger p = servicePoints[2].removeQueue();
                if (p == null) break;
                // after priority luggage, go to priority security
                controller.visualiseSecurity(true, EventType.LUGGAGE_DROP_PRIORITY);
                servicePoints[4].addQueue(p); // SECURITY_PRIORITY
            }

            case SECURITY -> {
                Passenger p = servicePoints[3].removeQueue();
                if (p == null) break;
                // normal security: if EU -> gate, else -> passport control (non-priority)
                if (p.isEuCitizen()) {
                    controller.visualiseGate(SECURITY);
                    servicePoints[7].addQueue(p); // GATE
                } else {
                    controller.visualisePassport(false, SECURITY);
                    servicePoints[5].addQueue(p); // PASSPORT_CONTROL
                }
            }

            case SECURITY_PRIORITY -> {
                Passenger p = servicePoints[4].removeQueue();
                if (p == null) break;
                // priority security: if EU -> gate, else -> passport control priority
                if (p.isEuCitizen()) {
                    controller.visualiseGate(SECURITY_PRIORITY);
                    servicePoints[7].addQueue(p); // GATE
                } else {
                    controller.visualisePassport(true, SECURITY_PRIORITY);
                    servicePoints[6].addQueue(p); // PASSPORT_CONTROL_PRIORITY
                }
            }

            case PASSPORT_CONTROL -> {
                Passenger p = servicePoints[5].removeQueue();
                if (p == null) break;
                controller.visualiseGate(PASSPORT_CONTROL);
                servicePoints[7].addQueue(p); // GATE
            }

            case PASSPORT_CONTROL_PRIORITY -> {
                Passenger p = servicePoints[6].removeQueue();
                if (p == null) break;
                controller.visualiseGate(PASSPORT_CONTROL_PRIORITY);
                servicePoints[7].addQueue(p); // GATE
            }

            case GATE -> {
                Passenger p = servicePoints[7].removeQueue();
                if (p == null) break;
                p.setRemovalTime(Clock.getInstance().getTime());
                //p.reportResults();
            }
        }
    }

    /**
     * C-phase: attempts to start service on all idle lines where a queue is present.
     * <p>Iterates through each {@link ServicePoint} and each of its parallel lines.</p>
     */
    @Override
    protected void tryCEvents() {
        for (ServicePoint p : servicePoints) {
            for (int i = 0; i < p.getLineCount(); i++) {
                if (!p.isReserved(i) && p.isOnQueue(i)) {
                    p.beginService(i);
                }
            }
        }
    }

    /**
     * Finalization/reporting: persists run configuration and collected queue statistics,
     * triggers result visualization, and prints the simulation end time.
     */
    @Override
    protected void results() {

        System.out.println("=== Simulation results ===");
        // ... (console printing kept as before)

        // Persist run using provided initial line counts
        Run run = new Run(
                initialLineCounts[1], // luggageDrop
                initialLineCounts[2], // priorityLuggageDrop
                initialLineCounts[3], // security
                initialLineCounts[0], // checkInQueues
                initialLineCounts[4], // prioritySecurity
                initialLineCounts[5], // passportControl
                initialLineCounts[6], // priorityPassportControl
                initialLineCounts[7]  // gate
        );
        runDao.persist(run);

        // collect stats values
        int checkInMax = servicePoints[0].getMaxLength();
        double checkInAvg = servicePoints[0].getAverageLength();

        int luggageDropMax = servicePoints[1].getMaxLength();
        double luggageDropAvg = servicePoints[1].getAverageLength();

        int priorityLuggageDropMax = servicePoints[2].getMaxLength();
        double priorityLuggageDropAvg = servicePoints[2].getAverageLength();

        int securityMax = servicePoints[3].getMaxLength();
        double securityAvg = servicePoints[3].getAverageLength();

        int prioritySecurityMax = servicePoints[4].getMaxLength();
        double prioritySecurityAvg = servicePoints[4].getAverageLength();

        int passportControlMax = servicePoints[5].getMaxLength();
        double passportControlAvg = servicePoints[5].getAverageLength();

        int priorityPassportControlMax = servicePoints[6].getMaxLength();
        double priorityPassportControlAvg = servicePoints[6].getAverageLength();

        int gateMax = servicePoints[7].getMaxLength();
        double gateAvg = servicePoints[7].getAverageLength();

        RunStatistics runStats = new RunStatistics(
                run,
                checkInMax, checkInAvg,
                luggageDropMax, luggageDropAvg,
                priorityLuggageDropMax, priorityLuggageDropAvg,
                securityMax, securityAvg,
                prioritySecurityMax, prioritySecurityAvg,
                passportControlMax, passportControlAvg,
                priorityPassportControlMax, priorityPassportControlAvg,
                gateMax, gateAvg
        );

        runStatisticsDao.persist(runStats);


        controller.visualiseResults(run, runStats);

        System.out.printf("Simulation ended at %.2f%n", Clock.getInstance().getTime());
    }
}
