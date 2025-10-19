package dao;

import jakarta.persistence.EntityManager;
import simu.model.entity.RunStatistics;

import java.util.List;

/**
 * Dao class for simulation run statistic settings.
 * Meant to save, update, find and delete Run instance {@link RunStatistics}
 * from the database.
 * @author Elias Rinne
 */
public class RunStatisticsDao {

    /**
     * Method for saving simulation run information such as max and average queue lengths to the database
     * @param runS is current instance of simulation run statistics to be saved to the database
     * @author Elias Rinne
     */
    public void persist(RunStatistics runS) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(runS);
        em.getTransaction().commit();
    }

    /**
     * Method to find a specific run's statistics based on its id
     * @param id primarykey for the run_statistic table that is needed to find
     * @return Run instance {@link RunStatistics}
     * @author Elias Rinne
     */

    public RunStatistics find(int id) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        RunStatistics runS = em.find(RunStatistics.class, id);
        return runS;
    }

    /**
     * Method to find all runs' statistics from the database, no usages yet
     * @return List of RunStatistic instances {@link RunStatistics}
     * @author Elias Rinne
     */

    public List<RunStatistics> findAll() {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        List<RunStatistics> runSList = em.createQuery("SELECT r FROM Run r").getResultList();
        return runSList;
    }
    /**
     * Method to update run statistic table in the database, no usages yet
     * @param runS is RunStatistic instance that is wanted to upgrade
     * @author Elias Rinne
     */

    public void update(RunStatistics runS) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(runS);
        em.getTransaction().commit();
    }

    /**
     * Method to delete run statistic from the database, no usages yet
     * @param runS is RunStatistic instance that is wanted to upgrade
     * @author Elias Rinne
     */
    public void delete(RunStatistics runS) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(runS);
        em.getTransaction().commit();
    }
}
