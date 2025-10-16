package simu.entity;


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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false, unique = true)
    private Run run;

    @Column(name = "check_in_queue_max_length", nullable = false)
    private int checkInQueueMaxLength;

    @Column(name = "check_in_queue_average_length", nullable = false)
    private double checkInQueueAverageLength;

    @Column(name = "luggage_drop_queue_max_length", nullable = false)
    private int luggageDropQueueMaxLength;

    @Column(name = "luggage_drop_queue_average_length", nullable = false)
    private double luggageDropQueueAverageLength;

    @Column(name = "priority_luggage_drop_queue_max_length", nullable = false)
    private int priorityLuggageDropQueueMaxLength;

    @Column(name = "priority_luggage_drop_queue_average_length", nullable = false)
    private double priorityLuggageDropQueueAverageLength;

    @Column(name = "security_queue_max_length", nullable = false)
    private int securityQueueMaxLength;

    @Column(name = "security_queue_average_length", nullable = false)
    private double securityQueueAverageLength;

    @Column(name = "priority_security_queue_max_length", nullable = false)
    private int prioritySecurityQueueMaxLength;

    @Column(name = "priority_security_queue_average_length", nullable = false)
    private double prioritySecurityQueueAverageLength;

    @Column(name = "passport_control_queue_max_length", nullable = false)
    private int passportControlQueueMaxLength;

    @Column(name = "passport_control_queue_average_length", nullable = false)
    private double passportControlQueueAverageLength;

    @Column(name = "priority_passport_control_queue_max_length", nullable = false)
    private int priorityPassportControlQueueMaxLength;

    @Column(name = "priority_passport_control_queue_average_length", nullable = false)
    private double priorityPassportControlQueueAverageLength;

    @Column(name = "gate_queue_max_length", nullable = false)
    private int gateQueueMaxLength;

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

    public RunStatistics() {}

    public Integer getId() { return id; }

    public int getCheckInQueueMaxLength() {
        return checkInQueueMaxLength;
    }

    public double getCheckInQueueAverageLength() {
        return checkInQueueAverageLength;
    }

    public int getLuggageDropQueueMaxLength() {
        return luggageDropQueueMaxLength;
    }

    public double getLuggageDropQueueAverageLength() {
        return luggageDropQueueAverageLength;
    }

    public int getPriorityLuggageDropQueueMaxLength() {
        return priorityLuggageDropQueueMaxLength;
    }

    public double getPriorityLuggageDropQueueAverageLength() {
        return priorityLuggageDropQueueAverageLength;
    }

    public int getSecurityQueueMaxLength() {
        return securityQueueMaxLength;
    }

    public double getSecurityQueueAverageLength() {
        return securityQueueAverageLength;
    }

    public int getPrioritySecurityQueueMaxLength() {
        return prioritySecurityQueueMaxLength;
    }

    public double getPrioritySecurityQueueAverageLength() {
        return prioritySecurityQueueAverageLength;
    }

    public int getPassportControlQueueMaxLength() {
        return passportControlQueueMaxLength;
    }

    public double getPassportControlQueueAverageLength() {
        return passportControlQueueAverageLength;
    }

    public int getPriorityPassportControlQueueMaxLength() {
        return priorityPassportControlQueueMaxLength;
    }

    public double getPriorityPassportControlQueueAverageLength() {
        return priorityPassportControlQueueAverageLength;
    }

    public int getGateQueueMaxLength() {
        return gateQueueMaxLength;
    }

    public double getGateQueueAverageLength() {
        return gateQueueAverageLength;
    }
}
