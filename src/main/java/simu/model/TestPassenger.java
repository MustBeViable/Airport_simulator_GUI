package simu.model;

import simu.framework.Clock;
import simu.framework.Trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestPassenger {
    private double arrivalTime;
    private double removalTime;
    private final int id;
    private static int NEXT_ID = 1;

    private boolean isPriority;
    private boolean checkIn;
    private boolean luggage;
    private boolean euCitizen;

    // ====== Tasajako kaikille reiteille: 16 kombon "korttipakka" ======
    // Yksi jokaista yhdistelmää (priority ∈ {0,1}, checkIn ∈ {0,1}, luggage ∈ {0,1}, eu ∈ {0,1})
    private static final List<Route> DECK = new ArrayList<>(16);
    private static int deckIndex = 0;

    // Reprottava sekoitus: vaihda siementä halutessasi (tai tee setter)
    private static final Random SHUFFLER = new Random(12345L);

    static {
        refillDeck(); // luodaan ensimmäinen pakka ja sekoitetaan
    }

    private static synchronized void refillDeck() {
        DECK.clear();
        for (int prio = 0; prio <= 1; prio++) {
            for (int ci = 0; ci <= 1; ci++) {
                for (int lug = 0; lug <= 1; lug++) {
                    for (int eu = 0; eu <= 1; eu++) {
                        DECK.add(new Route(prio == 1, ci == 1, lug == 1, eu == 1));
                    }
                }
            }
        }
        Collections.shuffle(DECK, SHUFFLER); // satunnainen järjestys, mutta joka blokissa tasajako
        deckIndex = 0;
    }

    private static synchronized Route nextRouteCard() {
        if (deckIndex >= DECK.size()) {
            refillDeck();
        }
        return DECK.get(deckIndex++);
    }

    private record Route(boolean priority, boolean checkIn, boolean luggage, boolean eu) {}

    /**
     * Luo matkustajan ja arpoo reitin tasajakokorttipakasta (16 kombon kierros).
     */
    public TestPassenger() {
        this.id = NEXT_ID++;

        // Ota seuraava "kortti" pakasta -> takaa tasajaon joka 16:lle matkustajalle
        Route r = nextRouteCard();
        this.isPriority = r.priority();
        this.checkIn    = r.checkIn();
        this.luggage    = r.luggage();
        this.euCitizen  = r.eu();

        this.arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "New #" + id + " arrived at " + arrivalTime
                + " [prio=" + isPriority + ", checkIn=" + checkIn
                + ", luggage=" + luggage + ", eu=" + euCitizen + "]");
    }

    // ====== Getterit/Setterit ja raportointi kuten ennen ======
    public double getRemovalTime() { return removalTime; }
    public void setRemovalTime(double t) { this.removalTime = t; }
    public double getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(double t) { this.arrivalTime = t; }
    public int getId() { return id; }

    public boolean getIsPriority() { return isPriority; }
    public boolean isCheckIn() { return checkIn; }
    public boolean isLuggage() { return luggage; }
    public boolean isEuCitizen() { return euCitizen; }

    private static double sumStay = 0;
    public void reportResults() {
        Trace.out(Trace.Level.INFO, "\nPassenger " + id + " ready!");
        Trace.out(Trace.Level.INFO, "Passenger " + id + " arrived: " + arrivalTime);
        Trace.out(Trace.Level.INFO, "Passenger " + id + " removed: " + removalTime);
        Trace.out(Trace.Level.INFO, "Passenger " + id + " stayed: " + (removalTime - arrivalTime));

        sumStay += (removalTime - arrivalTime);
        double mean = sumStay / id;
        System.out.println("Current mean of the Passenger service times " + mean);
    }

    // (Valinnainen) jos haluat vaihtaa sekoituksen siementä ajon alussa:
    public static void reseedDeck(long seed) {
        synchronized (Passenger.class) {
            SHUFFLER.setSeed(seed);
            refillDeck();
        }
    }
}
