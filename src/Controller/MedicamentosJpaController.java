/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Analisis;
import Model.Medicamentos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jorge
 */
public class MedicamentosJpaController implements Serializable {

    public MedicamentosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicamentos medicamentos) {
        if (medicamentos.getAnalisisCollection() == null) {
            medicamentos.setAnalisisCollection(new ArrayList<Analisis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Analisis> attachedAnalisisCollection = new ArrayList<Analisis>();
            for (Analisis analisisCollectionAnalisisToAttach : medicamentos.getAnalisisCollection()) {
                analisisCollectionAnalisisToAttach = em.getReference(analisisCollectionAnalisisToAttach.getClass(), analisisCollectionAnalisisToAttach.getIdAnalisis());
                attachedAnalisisCollection.add(analisisCollectionAnalisisToAttach);
            }
            medicamentos.setAnalisisCollection(attachedAnalisisCollection);
            em.persist(medicamentos);
            for (Analisis analisisCollectionAnalisis : medicamentos.getAnalisisCollection()) {
                Medicamentos oldIdMedicamentoOfAnalisisCollectionAnalisis = analisisCollectionAnalisis.getIdMedicamento();
                analisisCollectionAnalisis.setIdMedicamento(medicamentos);
                analisisCollectionAnalisis = em.merge(analisisCollectionAnalisis);
                if (oldIdMedicamentoOfAnalisisCollectionAnalisis != null) {
                    oldIdMedicamentoOfAnalisisCollectionAnalisis.getAnalisisCollection().remove(analisisCollectionAnalisis);
                    oldIdMedicamentoOfAnalisisCollectionAnalisis = em.merge(oldIdMedicamentoOfAnalisisCollectionAnalisis);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medicamentos medicamentos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicamentos persistentMedicamentos = em.find(Medicamentos.class, medicamentos.getIdMedicamento());
            Collection<Analisis> analisisCollectionOld = persistentMedicamentos.getAnalisisCollection();
            Collection<Analisis> analisisCollectionNew = medicamentos.getAnalisisCollection();
            Collection<Analisis> attachedAnalisisCollectionNew = new ArrayList<Analisis>();
            for (Analisis analisisCollectionNewAnalisisToAttach : analisisCollectionNew) {
                analisisCollectionNewAnalisisToAttach = em.getReference(analisisCollectionNewAnalisisToAttach.getClass(), analisisCollectionNewAnalisisToAttach.getIdAnalisis());
                attachedAnalisisCollectionNew.add(analisisCollectionNewAnalisisToAttach);
            }
            analisisCollectionNew = attachedAnalisisCollectionNew;
            medicamentos.setAnalisisCollection(analisisCollectionNew);
            medicamentos = em.merge(medicamentos);
            for (Analisis analisisCollectionOldAnalisis : analisisCollectionOld) {
                if (!analisisCollectionNew.contains(analisisCollectionOldAnalisis)) {
                    analisisCollectionOldAnalisis.setIdMedicamento(null);
                    analisisCollectionOldAnalisis = em.merge(analisisCollectionOldAnalisis);
                }
            }
            for (Analisis analisisCollectionNewAnalisis : analisisCollectionNew) {
                if (!analisisCollectionOld.contains(analisisCollectionNewAnalisis)) {
                    Medicamentos oldIdMedicamentoOfAnalisisCollectionNewAnalisis = analisisCollectionNewAnalisis.getIdMedicamento();
                    analisisCollectionNewAnalisis.setIdMedicamento(medicamentos);
                    analisisCollectionNewAnalisis = em.merge(analisisCollectionNewAnalisis);
                    if (oldIdMedicamentoOfAnalisisCollectionNewAnalisis != null && !oldIdMedicamentoOfAnalisisCollectionNewAnalisis.equals(medicamentos)) {
                        oldIdMedicamentoOfAnalisisCollectionNewAnalisis.getAnalisisCollection().remove(analisisCollectionNewAnalisis);
                        oldIdMedicamentoOfAnalisisCollectionNewAnalisis = em.merge(oldIdMedicamentoOfAnalisisCollectionNewAnalisis);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medicamentos.getIdMedicamento();
                if (findMedicamentos(id) == null) {
                    throw new NonexistentEntityException("The medicamentos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicamentos medicamentos;
            try {
                medicamentos = em.getReference(Medicamentos.class, id);
                medicamentos.getIdMedicamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicamentos with id " + id + " no longer exists.", enfe);
            }
            Collection<Analisis> analisisCollection = medicamentos.getAnalisisCollection();
            for (Analisis analisisCollectionAnalisis : analisisCollection) {
                analisisCollectionAnalisis.setIdMedicamento(null);
                analisisCollectionAnalisis = em.merge(analisisCollectionAnalisis);
            }
            em.remove(medicamentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medicamentos> findMedicamentosEntities() {
        return findMedicamentosEntities(true, -1, -1);
    }

    public List<Medicamentos> findMedicamentosEntities(int maxResults, int firstResult) {
        return findMedicamentosEntities(false, maxResults, firstResult);
    }

    private List<Medicamentos> findMedicamentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicamentos.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Medicamentos findMedicamentos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicamentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicamentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicamentos> rt = cq.from(Medicamentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
