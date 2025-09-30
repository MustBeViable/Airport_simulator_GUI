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

    public Run(int luggageDropCount, int priorityLuggageDropCount, int securityCount, int checkInQueuesCount, int prioritySecurityCount, int passportControlCount, int priorityPassportControlCount, int gateCount) {
        this.luggageDropCount = luggageDropCount;
        this.priorityLuggageDropCount = priorityLuggageDropCount;
        this.securityCount = securityCount;
        this.checkInQueuesCount = checkInQueuesCount;
        this.prioritySecurityCount = prioritySecurityCount;
        this.passportControlCount = passportControlCount;
        this.priorityPassportControlCount = priorityPassportControlCount;
        this.gateCount = gateCount;
    }

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

}
