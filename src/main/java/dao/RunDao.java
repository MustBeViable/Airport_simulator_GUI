package dao;


import jakarta.persistence.EntityManager;
import simu.entity.Run;

import java.util.Currency;
import java.util.List;

public class RunDao {

    public void persist(Run run) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();
    }

    public Run find(int id) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        Run run = em.find(Run.class, id);
        return run;
    }

    public List<Run> findAll() {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        List<Run> runList = em.createQuery("SELECT r FROM Run r").getResultList();
        return runList;
    }

    public void update(Run r) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(r);
        em.getTransaction().commit();
    }

    public void delete(Run r) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(r);
        em.getTransaction().commit();
    }
}
