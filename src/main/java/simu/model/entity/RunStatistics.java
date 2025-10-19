package simu.model.entity;


import jakarta.persistence.*;

/**
 * JPA entity that stores aggregated queue statistics for a single simulation {@link Run}.
 * Mapped to the table {@code run_statistics}. Each row is associated 1:1 with a {@link Run}
 * via a foreign key column {@code run_id} (unique, non-null). All metrics are stored as either
 * integer maxima or double-precision averages per queue type.
 * @author Elias Rinne
 */

@Entity
@Table(name = "run_statistics")
public class RunStatistics {

    /** Database-generated surrogate key (IDENTITY); {@code null} until persisted. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Owning {@link Run}. Mapped via {@code run_id} with a strict 1:1 relationship.
     * <p><b>Fetch:</b> LAZY — access within an open persistence context to avoid proxy issues.</p>
     */
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false, unique = true)
    private Run run;

    /** Maximum observed length of the check-in queue. */
    @Column(name = "check_in_queue_max_length", nullable = false)
    private int checkInQueueMaxLength;

    /** Average observed length of the check-in queue. */
    @Column(name = "check_in_queue_average_length", nullable = false)
    private double checkInQueueAverageLength;

    /** Maximum observed length of the luggage-drop queue. */
    @Column(name = "luggage_drop_queue_max_length", nullable = false)
    private int luggageDropQueueMaxLength;

    /** Average observed length of the luggage-drop queue. */
    @Column(name = "luggage_drop_queue_average_length", nullable = false)
    private double luggageDropQueueAverageLength;

    /** Maximum observed length of the priority luggage-drop queue. */
    @Column(name = "priority_luggage_drop_queue_max_length", nullable = false)
    private int priorityLuggageDropQueueMaxLength;

    /** Average observed length of the priority luggage-drop queue. */
    @Column(name = "priority_luggage_drop_queue_average_length", nullable = false)
    private double priorityLuggageDropQueueAverageLength;

    /** Maximum observed length of the security queue. */
    @Column(name = "security_queue_max_length", nullable = false)
    private int securityQueueMaxLength;

    /** Average observed length of the security queue. */
    @Column(name = "security_queue_average_length", nullable = false)
    private double securityQueueAverageLength;

    /** Maximum observed length of the priority security queue. */
    @Column(name = "priority_security_queue_max_length", nullable = false)
    private int prioritySecurityQueueMaxLength;

    /** Average observed length of the priority security queue. */
    @Column(name = "priority_security_queue_average_length", nullable = false)
    private double prioritySecurityQueueAverageLength;

    /** Maximum observed length of the passport control queue. */
    @Column(name = "passport_control_queue_max_length", nullable = false)
    private int passportControlQueueMaxLength;

    /** Average observed length of the passport control queue. */
    @Column(name = "passport_control_queue_average_length", nullable = false)
    private double passportControlQueueAverageLength;

    /** Maximum observed length of the priority passport control queue. */
    @Column(name = "priority_passport_control_queue_max_length", nullable = false)
    private int priorityPassportControlQueueMaxLength;

    /** Average observed length of the priority passport control queue. */
    @Column(name = "priority_passport_control_queue_average_length", nullable = false)
    private double priorityPassportControlQueueAverageLength;

    /** Maximum observed length of the gate queue. */
    @Column(name = "gate_queue_max_length", nullable = false)
    private int gateQueueMaxLength;

    /** Average observed length of the gate queue. */
    @Column(name = "gate_queue_average_length", nullable = false)
    private double gateQueueAverageLength;

    /**
     * Full-argument constructor used to populate all metrics for a given run.
     *
     * @param run owning run (non-null)
     * @param checkInQueueMaxLength max length of check-in queue
     * @param checkInQueueAverageLength average length of check-in queue
     * @param luggageDropQueueMaxLength max length of luggage-drop queue
     * @param luggageDropQueueAverageLength average length of luggage-drop queue
     * @param priorityLuggageDropQueueMaxLength max length of priority luggage-drop queue
     * @param priorityLuggageDropQueueAverageLength average length of priority luggage-drop queue
     * @param securityQueueMaxLength max length of security queue
     * @param securityQueueAverageLength average length of security queue
     * @param prioritySecurityQueueMaxLength max length of priority security queue
     * @param prioritySecurityQueueAverageLength average length of priority security queue
     * @param passportControlQueueMaxLength max length of passport control queue
     * @param passportControlQueueAverageLength average length of passport control queue
     * @param priorityPassportControlQueueMaxLength max length of priority passport control queue
     * @param priorityPassportControlQueueAverageLength average length of priority passport control queue
     * @param gateQueueMaxLength max length of gate queue
     * @param gateQueueAverageLength average length of gate queue
     *
     * @author Elias Rinne
     */

    public RunStatistics(Run run, int checkInQueueMaxLength, double checkInQueueAverageLength,
                         int luggageDropQueueMaxLength, double luggageDropQueueAverageLength,
                         int priorityLuggageDropQueueMaxLength, double priorityLuggageDropQueueAverageLength,
                         int securityQueueMaxLength, double securityQueueAverageLength,
                         int prioritySecurityQueueMaxLength, double prioritySecurityQueueAverageLength,
                         int passportControlQueueMaxLength, double passportControlQueueAverageLength,
                         int priorityPassportControlQueueMaxLength, double priorityPassportControlQueueAverageLength,
                         int gateQueueMaxLength, double gateQueueAverageLength) {
        this.run = run;
        this.checkInQueueMaxLength = checkInQueueMaxLength;
        this.checkInQueueAverageLength = checkInQueueAverageLength;
        this.luggageDropQueueMaxLength = luggageDropQueueMaxLength;
        this.luggageDropQueueAverageLength = luggageDropQueueAverageLength;
        this.priorityLuggageDropQueueMaxLength = priorityLuggageDropQueueMaxLength;
        this.priorityLuggageDropQueueAverageLength = priorityLuggageDropQueueAverageLength;
        this.securityQueueMaxLength = securityQueueMaxLength;
        this.securityQueueAverageLength = securityQueueAverageLength;
        this.prioritySecurityQueueMaxLength = prioritySecurityQueueMaxLength;
        this.prioritySecurityQueueAverageLength = prioritySecurityQueueAverageLength;
        this.passportControlQueueMaxLength = passportControlQueueMaxLength;
        this.passportControlQueueAverageLength = passportControlQueueAverageLength;
        this.priorityPassportControlQueueMaxLength = priorityPassportControlQueueMaxLength;
        this.priorityPassportControlQueueAverageLength = priorityPassportControlQueueAverageLength;
        this.gateQueueMaxLength = gateQueueMaxLength;
        this.gateQueueAverageLength = gateQueueAverageLength;
    }

    /**
     * JPA-required no-arg constructor.
     * Used by the persistence provider to instantiate the entity via reflection.
     */
    public RunStatistics() {}

    /**
     * @return database-generated identifier, or {@code null} if not yet persisted
     */
    public Integer getId() { return id; }

    /**
     * @return maximum observed length of the check-in queue
     */
    public int getCheckInQueueMaxLength() {
        return checkInQueueMaxLength;
    }

    /**
     * @return average observed length of the check-in queue
     */
    public double getCheckInQueueAverageLength() {
        return checkInQueueAverageLength;
    }

    /**
     * @return maximum observed length of the luggage-drop queue
     */
    public int getLuggageDropQueueMaxLength() {
        return luggageDropQueueMaxLength;
    }

    /**
     * @return average observed length of the luggage-drop queue
     */
    public double getLuggageDropQueueAverageLength() {
        return luggageDropQueueAverageLength;
    }

    /**
     * @return maximum observed length of the priority luggage-drop queue
     */
    public int getPriorityLuggageDropQueueMaxLength() {
        return priorityLuggageDropQueueMaxLength;
    }

    /**
     * @return average observed length of the priority luggage-drop queue
     */
    public double getPriorityLuggageDropQueueAverageLength() {
        return priorityLuggageDropQueueAverageLength;
    }

    /**
     * @return maximum observed length of the security queue
     */
    public int getSecurityQueueMaxLength() {
        return securityQueueMaxLength;
    }

    /**
     * @return average observed length of the security queue
     */
    public double getSecurityQueueAverageLength() {
        return securityQueueAverageLength;
    }

    /**
     * @return maximum observed length of the priority security queue
     */
    public int getPrioritySecurityQueueMaxLength() {
        return prioritySecurityQueueMaxLength;
    }

    /**
     * @return average observed length of the priority security queue
     */
    public double getPrioritySecurityQueueAverageLength() {
        return prioritySecurityQueueAverageLength;
    }

    /**
     * @return maximum observed length of the passport control queue
     */
    public int getPassportControlQueueMaxLength() {
        return passportControlQueueMaxLength;
    }

    /**
     * @return average observed length of the passport control queue
     */
    public double getPassportControlQueueAverageLength() {
        return passportControlQueueAverageLength;
    }

    /**
     * @return maximum observed length of the priority passport control queue
     */
    public int getPriorityPassportControlQueueMaxLength() {
        return priorityPassportControlQueueMaxLength;
    }

    /**
     * @return average observed length of the priority passport control queue
     */
    public double getPriorityPassportControlQueueAverageLength() {
        return priorityPassportControlQueueAverageLength;
    }

    /**
     * @return maximum observed length of the gate queue
     */
    public int getGateQueueMaxLength() {
        return gateQueueMaxLength;
    }

    /**
     * @return average observed length of the gate queue
     */
    public double getGateQueueAverageLength() {
        return gateQueueAverageLength;
    }
}
