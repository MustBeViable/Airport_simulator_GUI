package dao;


import jakarta.persistence.EntityManager;
import simu.model.entity.Run;

import java.util.List;

/**
 * Dao class for simulation run settings.
 * Meant to save, update, find and delete Run instance {@link Run}
 * from the database.
 * @author Elias Rinne
 */

public class RunDao {

    /**
     * Method for saving simulation run information such as queue lengths to the database
     * @param run is current instance of simulation run to be saved to the database
     * @author Elias Rinne
     */
    public void persist(Run run) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();
    }

    /**
     * Method to find a specific run based on its id
     * @param id primarykey for the run that is needed to find
     * @return Run instance {@link Run}
     * @author Elias Rinne
     */
    public Run find(int id) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        Run run = em.find(Run.class, id);
        return run;
    }
    /**
     * Method to find all runs from the database
     * @return List of Run instances {@link Run}
     * @author Elias Rinne
     */
    public List<Run> findAll() {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        List<Run> runList = em.createQuery("SELECT r FROM Run r").getResultList();
        return runList;
    }
    /**
     * Method to update Run table in the database, no usages yet
     * @param r is Run instance that is wanted to upgrade
     * @author Elias Rinne
     */
    public void update(Run r) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(r);
        em.getTransaction().commit();
    }

    /**
     * Method to delete run from the database, no usages yet
     * @param r is Run instance that is wanted to upgrade
     * @author Elias Rinne
     */
    public void delete(Run r) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(r);
        em.getTransaction().commit();
    }
}
