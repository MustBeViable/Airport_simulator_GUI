package simu.entity;


import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

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

    @Column(name = "check_in_queue_min_length", nullable = false)
    private int checkInQueueMinLength;

    @Column(name = "check_in_queue_average_length", precision = 10, scale = 3, nullable = false)
    private double checkInQueueAverageLength;

    @Column(name = "luggage_drop_queue_max_length", nullable = false)
    private int luggageDropQueueMaxLength;

    @Column(name = "luggage_drop_queue_min_length", nullable = false)
    private int luggageDropQueueMinLength;

    @Column(name = "luggage_drop_queue_average_length", precision = 10, scale = 3, nullable = false)
    private double luggageDropQueueAverageLength;

    @Column(name = "priority_luggage_drop_queue_max_length", nullable = false)
    private int priorityLuggageDropQueueMaxLength;

    @Column(name = "priority_luggage_drop_queue_min_length", nullable = false)
    private int priorityLuggageDropQueueMinLength;

    @Column(name = "priority_luggage_drop_queue_average_length", precision = 10, scale = 3, nullable = false)
    private double priorityLuggageDropQueueAverageLength;

    @Column(name = "security_queue_max_length", nullable = false)
    private int securityQueueMaxLength;

    @Column(name = "security_queue_min_length", nullable = false)
    private int securityQueueMinLength;

    @Column(name = "security_queue_average_length", precision = 10, scale = 3, nullable = false)
    private double securityQueueAverageLength;

    @Column(name = "priority_security_queue_max_length", nullable = false)
    private int prioritySecurityQueueMaxLength;

    @Column(name = "priority_security_queue_min_length", nullable = false)
    private int prioritySecurityQueueMinLength;

    @Column(name = "priority_security_queue_average_length", precision = 10, scale = 3, nullable = false)
    private double prioritySecurityQueueAverageLength;

    @Column(name = "passport_control_queue_max_length", nullable = false)
    private int passportControlQueueMaxLength;

    @Column(name = "passport_control_queue_min_length", nullable = false)
    private int passportControlQueueMinLength;

    @Column(name = "passport_control_queue_average_length", precision = 10, scale = 3, nullable = false)
    private double passportControlQueueAverageLength;

    @Column(name = "priority_passport_control_queue_max_length", nullable = false)
    private int priorityPassportControlQueueMaxLength;

    @Column(name = "priority_passport_control_queue_min_length", nullable = false)
    private int priorityPassportControlQueueMinLength;

    @Column(name = "priority_passport_control_queue_average_length", precision = 10, scale = 3, nullable = false)
    private double priorityPassportControlQueueAverageLength;

    @Column(name = "gate_queue_max_length", nullable = false)
    private int gateQueueMaxLength;

    @Column(name = "gate_queue_min_length", nullable = false)
    private int gateQueueMinLength;

    @Column(name = "gate_queue_average_length", precision = 10, scale = 3, nullable = false)
    private double gateQueueAverageLength;

    public RunStatistics() {}

    public Integer getId() { return id; }

}
