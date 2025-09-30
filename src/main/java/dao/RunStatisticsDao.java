package dao;

import jakarta.persistence.EntityManager;
import simu.entity.RunStatistics;

import java.util.List;

public class RunStatisticsDao {
    public void persist(RunStatistics runS) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(runS);
        em.getTransaction().commit();
    }

    public RunStatistics find(int id) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        RunStatistics runS = em.find(RunStatistics.class, id);
        return runS;
    }

    public List<RunStatistics> findAll() {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        List<RunStatistics> runSList = em.createQuery("SELECT r FROM Run r").getResultList();
        return runSList;
    }

    public void update(RunStatistics runS) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(runS);
        em.getTransaction().commit();
    }

    public void delete(RunStatistics runS) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(runS);
        em.getTransaction().commit();
    }
}
