package simu.model;

import simu.framework.Clock;
import simu.framework.Trace;

import java.util.Random;

public class Passenger {
    private double arrivalTime;
    private double removalTime;
    private int id;
    private static int i = 1;
    private static double sum = 0;
    private boolean isPriority;
    private boolean checkIn;
    private boolean luggage;
    private boolean euCitizen;

    private static double priorityFraction = 0.2;
    private static double luggageFraction = 0.7;
    private static double euCitizenFraction = 0.9;
    private static double checkInFraction = 0.8;

    private static final Random RAND = new Random();

    public Passenger() {
        id = i++;
        this.isPriority = decideByFraction(priorityFraction);
        this.checkIn = decideByFraction(checkInFraction);
        this.luggage = decideByFraction(luggageFraction);
        this.euCitizen = decideByFraction(euCitizenFraction);
        arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "New  #" + id + " arrived at  " + arrivalTime);
    }

    private static boolean decideByFraction(double fraction) {
        if (fraction <= 0.0) return false;
        if (fraction >= 1.0) return true;
        return RAND.nextDouble() < fraction;
    }

    public static void setPriorityFraction(double f) {
        if (f < 0.0 || f > 1.0) throw new IllegalArgumentException("priorityFraction must be between 0.0 and 1.0");
        priorityFraction = f;
    }

    public static void setLuggageFraction(double f) {
        if (f < 0.0 || f > 1.0) throw new IllegalArgumentException("luggageFraction must be between 0.0 and 1.0");
        luggageFraction = f;
    }

    public static void setEuCitizenFraction(double f) {
        if (f < 0.0 || f > 1.0) throw new IllegalArgumentException("euCitizenFraction must be between 0.0 and 1.0");
        euCitizenFraction = f;
    }

    public static void setCheckInFraction(double f) {
        if (f < 0.0 || f > 1.0) throw new IllegalArgumentException("checkInFraction must be between 0.0 and 1.0");
        checkInFraction = f;
    }

    public static double getPriorityFraction() { return priorityFraction; }
    public static double getLuggageFraction() { return luggageFraction; }
    public static double getEuCitizenFraction() { return euCitizenFraction; }
    public static double getCheckInFraction() { return checkInFraction; }

    public double getRemovalTime() {
        return removalTime;
    }

    public boolean getIsPriority(){
        return isPriority;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public boolean isLuggage() {
        return luggage;
    }

    public boolean isEuCitizen() {
        return euCitizen;
    }

    public void setRemovalTime(double removalTime) {
        this.removalTime = removalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

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
