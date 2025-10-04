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

import java.util.ArrayList;
import java.util.Random;

import static simu.model.EventType.*;


public class MyEngine extends Engine {
    private ArrivalProcess arrivalProcess;
    private ServicePoint[] servicePoints;
    private final int SERVICE_POINT_COUNT;
    private final int[] initialLineCounts;
    public static final boolean TEXTDEMO = false; // set false to get more realistic simulation case
    public static final boolean FIXEDARRIVALTIMES = false;
    public static final boolean FXIEDSERVICETIMES = false;
    private RunStatisticsDao runStatisticsDao=new RunStatisticsDao();
    private RunDao runDao = new RunDao();

    /**
     * Default constructor keeps previous defaults.
     */
    public MyEngine(IControllerMtoV controller) {
        this(controller, new int[]{1,1,1,1,1,1,1,1});
    }

    /**
     * New constructor: accept line counts for each service point.
     * Expects an array of length 8 (the rest of the code assumes 8 service points).
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

    @Override
    protected void initialization() {    // First arrival in the system
        arrivalProcess.generateNext();
    }

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

    @Override
    protected void results() {

        System.out.println("=== Simulation results ===");
        // ... (console printing kept as before)

        // Persist run using provided initial line counts
        Run run = new Run(
                initialLineCounts[0],
                initialLineCounts[1],
                initialLineCounts[2],
                initialLineCounts[3],
                initialLineCounts[4],
                initialLineCounts[5],
                initialLineCounts[6],
                initialLineCounts[7]
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
