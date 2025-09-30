DROP DATABASE IF EXISTS airport_simulator;
CREATE DATABASE airport_simulator;
USE airport_simulator;


DROP TABLE IF EXISTS run_statistics;
DROP TABLE IF EXISTS run;

CREATE TABLE run (
    id INT NOT NULL AUTO_INCREMENT,
    check_in_queues_count INT NOT NULL,
    luggage_drop_count INT NOT NULL,
    priority_luggage_drop_count INT NOT NULL,
    security_count INT NOT NULL,
    priority_security_count INT NOT NULL,
    passport_control_count INT NOT NULL,
    priority_passport_control_count INT NOT NULL,
    gate_count INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE run_statistics (
    id INT NOT NULL AUTO_INCREMENT,
    run_id INT NOT NULL,
    check_in_queue_max_length INT NOT NULL,
    check_in_queue_average_length DECIMAL(10,3) NOT NULL,

    luggage_drop_queue_max_length INT NOT NULL,
    luggage_drop_queue_average_length DECIMAL(10,3) NOT NULL,

    priority_luggage_drop_queue_max_length INT NOT NULL,
    priority_luggage_drop_queue_average_length DECIMAL(10,3) NOT NULL,

    security_queue_max_length INT NOT NULL,
    security_queue_average_length DECIMAL(10,3) NOT NULL,

    priority_security_queue_max_length INT NOT NULL,
    priority_security_queue_average_length DECIMAL(10,3) NOT NULL,

    passport_control_queue_max_length INT NOT NULL,
    passport_control_queue_average_length DECIMAL(10,3) NOT NULL,

    priority_passport_control_queue_max_length INT NOT NULL,
    priority_passport_control_queue_average_length DECIMAL(10,3) NOT NULL,

    gate_queue_max_length INT NOT NULL,
    gate_queue_average_length DECIMAL(10,3) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_run_stats_run
        FOREIGN KEY (run_id) REFERENCES run(id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (run_id)
);


DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, ALTER ON airport_simulator.* TO 'appuser'@'localhost';
GRANT CREATE, DROP ON airport_simulator.* TO 'appuser2'@'localhost';