package datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Database connection class for simulation run statistic settings.
 * Build as singleton to ensure one instance per program.
 * @author Elias Rinne
 */

public class MariaDbJpaConnection {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    /**
     * Returns EntityManager instance. If not made, makes it.
     * @return EntityManager instance
     * @author Elias Rinne
     */

    public static EntityManager getInstance() {

        if (em==null) {
            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("AirportSimulator");
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}