package simu.model;

import controller.IControllerMtoV;
import eduni.distributions.*;
import simu.framework.ArrivalProcess;
import simu.framework.Clock;
import simu.framework.Engine;
import simu.framework.Event;
import java.util.Random;

import static simu.model.EventType.*;


public class MyEngine extends Engine {
    private ArrivalProcess arrivalProcess;
    private ServicePoint[] servicePoints;
    private final int SERVICE_POINT_COUNT;
    public static final boolean TEXTDEMO = false; // set false to get more realistic simulation case
    public static final boolean FIXEDARRIVALTIMES = false;
    public static final boolean FXIEDSERVICETIMES = false;

    /**
     * Service Points and random number generator with different distributions are created here.
     * We use exponent distribution for customer arrival times and normal distribution for the
     * service times.
     */
    public MyEngine(IControllerMtoV controller) {
        super(controller);
        this.SERVICE_POINT_COUNT = 8;
        servicePoints = new ServicePoint[SERVICE_POINT_COUNT];

        if (TEXTDEMO) {
            /* special setup for the example in text
             * https://github.com/jacquesbergelius/PP-CourseMaterial/blob/master/1.1_Introduction_to_Simulation.md
             */
            Random r = new Random();

            ContinuousGenerator arrivalTime = null;
            if (FIXEDARRIVALTIMES) {
                /* version where the arrival times are constant (and greater than service times) */

                // make a special "random number distribution" which produces constant value for the customer arrival times
                arrivalTime = new ContinuousGenerator() {
                    @Override
                    public double sample() {
                        return 10;
                    }

                    @Override
                    public void setSeed(long seed) {
                    }

                    @Override
                    public long getSeed() {
                        return 0;
                    }

                    @Override
                    public void reseed() {
                    }
                };
            } else
                // exponential distribution is used to model customer arrivals times, to get variability between programs runs, give a variable seed
                arrivalTime = new Negexp(10, Integer.toUnsignedLong(r.nextInt()));

            ContinuousGenerator serviceTime = null;
            if (FXIEDSERVICETIMES) {
                // make a special "random number distribution" which produces constant value for the service time in service points
                serviceTime = new ContinuousGenerator() {
                    @Override
                    public double sample() {
                        return 9;
                    }

                    @Override
                    public void setSeed(long seed) {
                    }

                    @Override
                    public long getSeed() {
                        return 0;
                    }

                    @Override
                    public void reseed() {
                    }
                };
            } else
                // normal distribution used to model service times
                serviceTime = new Normal(10, 6, Integer.toUnsignedLong(r.nextInt()));

            servicePoints[0] = new ServicePoint(serviceTime, eventList, EventType.CHECK_IN,2);
            servicePoints[1] = new ServicePoint(serviceTime, eventList, EventType.LUGGAGE_DROP,4);
            servicePoints[2] = new ServicePoint(serviceTime, eventList, EventType.LUGGAGE_DROP_PRIORITY,5);
            servicePoints[3] = new ServicePoint(serviceTime, eventList, EventType.SECURITY,6);
            servicePoints[4] = new ServicePoint(serviceTime, eventList, EventType.SECURITY_PRIORITY,7);
            servicePoints[5] = new ServicePoint(serviceTime, eventList, EventType.PASSPORT_CONTROL,1);
            servicePoints[6] = new ServicePoint(serviceTime, eventList, EventType.PASSPORT_CONTROL_PRIORITY,67);
            servicePoints[7] = new ServicePoint(serviceTime, eventList, EventType.GATE,4);

            arrivalProcess = new ArrivalProcess(arrivalTime, eventList, ARR1);
        } else {
            /* more realistic simulation case with variable customer arrival times and service times */
            servicePoints[0] = new ServicePoint(new LogNormal(2.3, 0.5), eventList, EventType.CHECK_IN,3);
            servicePoints[1] = new ServicePoint(new Gamma(2.0, 5.0), eventList, EventType.LUGGAGE_DROP,6);
            servicePoints[2] = new ServicePoint(new Gamma(2.0, 5.0), eventList, EventType.LUGGAGE_DROP_PRIORITY,5);
            servicePoints[3] = new ServicePoint(new TruncatedNormal(12, 6), eventList, EventType.SECURITY,4);
            servicePoints[4] = new ServicePoint(new Normal(8, 4), eventList, EventType.SECURITY_PRIORITY,2);
            servicePoints[5] = new ServicePoint(new LogNormal(2.1, 0.7), eventList, EventType.PASSPORT_CONTROL,3);
            servicePoints[6] = new ServicePoint(new LogNormal(2.1, 0.7), eventList, EventType.PASSPORT_CONTROL_PRIORITY,66);
            servicePoints[7] = new ServicePoint(new Normal(5, 1), eventList, EventType.GATE,5);

            arrivalProcess = new ArrivalProcess(new Negexp(15, 5), eventList, ARR1);
            /*
            OLI VALMIIKSI KOODISSA MUKANA
			servicePoints[0] = new ServicePoint(new Normal(10, 6), eventList, EventType.DEP1);
			servicePoints[1] = new ServicePoint(new Normal(10, 10), eventList, EventType.DEP2);
			servicePoints[2] = new ServicePoint(new Normal(5, 3), eventList, EventType.DEP3);

			arrivalProcess = new ArrivalProcess(new Negexp(15, 5), eventList, EventType.ARR1);*/
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
        System.out.printf("%-8s %-12s %-12s %-12s%n", "Queue", "Max", "Min", "Average");
        for (int i = 0; i < servicePoints.length; i++) {
            ServicePoint s = servicePoints[i];
            System.out.printf("%-8s %-12d %-12d %-12.2f%n",
                    "Queue " + (i + 1),
                    s.getMaxLength(),
                    s.getMinLength(),
                    s.getAverageLength());
        }
        System.out.printf("Simulation ended at %.2f%n", Clock.getInstance().getTime());
    }
}