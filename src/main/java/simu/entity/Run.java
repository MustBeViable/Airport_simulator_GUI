package simu.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "run")
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "check_in_queues_count", nullable = false)
    private int checkInQueuesCount;

    @Column(name = "luggage_drop_count", nullable = false)
    private int luggageDropCount;

    @Column(name = "priority_luggage_drop_count", nullable = false)
    private int priorityLuggageDropCount;

    @Column(name = "security_count", nullable = false)
    private int securityCount;

    @Column(name = "priority_security_count", nullable = false)
    private int prioritySecurityCount;

    @Column(name = "passport_control_count", nullable = false)
    private int passportControlCount;

    @Column(name = "priority_passport_control_count", nullable = false)
    private int priorityPassportControlCount;

    @Column(name = "gate_count", nullable = false)
    private int gateCount;

    @OneToOne(mappedBy = "run", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RunStatistics statistics;

    public Run() {}


    public Integer getId() {
        return id;
    }

    public int getCheckInQueuesCount() {
        return checkInQueuesCount;
    }

    public int getLuggageDropCount() {
        return luggageDropCount;
    }

    public int getPriorityLuggageDropCount() {
        return priorityLuggageDropCount;
    }

    public int getSecurityCount() {
        return securityCount;
    }

    public int getPrioritySecurityCount() {
        return prioritySecurityCount;
    }

    public int getPassportControlCount() {
        return passportControlCount;
    }

    public int getPriorityPassportControlCount() {
        return priorityPassportControlCount;
    }

    public int getGateCount() {
        return gateCount;
    }

    public RunStatistics getStatistics() {
        return statistics;
    }
}
