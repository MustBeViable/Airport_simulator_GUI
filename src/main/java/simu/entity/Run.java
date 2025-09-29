package simu.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "run")
public class Run implements Serializable {

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

    // Bidirectional 1–1; Run "omistaa" tilastonsa loogisesti
    @OneToOne(mappedBy = "run", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RunStatistics statistics;

    public Run() {}

    // --- helpers for bidirectional sync ---
    public void setStatistics(RunStatistics statistics) {
        if (this.statistics != null) {
            this.statistics.setRun(null);
        }
        this.statistics = statistics;
        if (statistics != null) {
            statistics.setRun(this);
        }
    }

    // --- getters & setters ---
    public Integer getId() { return id; }
    public int getCheckInQueuesCount() { return checkInQueuesCount; }
    public void setCheckInQueuesCount(int v) { this.checkInQueuesCount = v; }
    public int getLuggageDropCount() { return luggageDropCount; }
    public void setLuggageDropCount(int v) { this.luggageDropCount = v; }
    public int getPriorityLuggageDropCount() { return priorityLuggageDropCount; }
    public void setPriorityLuggageDropCount(int v) { this.priorityLuggageDropCount = v; }
    public int getSecurityCount() { return securityCount; }
    public void setSecurityCount(int v) { this.securityCount = v; }
    public int getPrioritySecurityCount() { return prioritySecurityCount; }
    public void setPrioritySecurityCount(int v) { this.prioritySecurityCount = v; }
    public int getPassportControlCount() { return passportControlCount; }
    public void setPassportControlCount(int v) { this.passportControlCount = v; }
    public int getPriorityPassportControlCount() { return priorityPassportControlCount; }
    public void setPriorityPassportControlCount(int v) { this.priorityPassportControlCount = v; }
    public int getGateCount() { return gateCount; }
    public void setGateCount(int v) { this.gateCount = v; }
    public RunStatistics getStatistics() { return statistics; }
}
