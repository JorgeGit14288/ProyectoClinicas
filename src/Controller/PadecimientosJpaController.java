/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.IllegalOrphanException;
import Controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Analisis;
import Model.Padecimientos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jorge
 */
public class PadecimientosJpaController implements Serializable {

    public PadecimientosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Padecimientos padecimientos) {
        if (padecimientos.getAnalisisCollection() == null) {
            padecimientos.setAnalisisCollection(new ArrayList<Analisis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Analisis> attachedAnalisisCollection = new ArrayList<Analisis>();
            for (Analisis analisisCollectionAnalisisToAttach : padecimientos.getAnalisisCollection()) {
                analisisCollectionAnalisisToAttach = em.getReference(analisisCollectionAnalisisToAttach.getClass(), analisisCollectionAnalisisToAttach.getIdAnalisis());
                attachedAnalisisCollection.add(analisisCollectionAnalisisToAttach);
            }
            padecimientos.setAnalisisCollection(attachedAnalisisCollection);
            em.persist(padecimientos);
            for (Analisis analisisCollectionAnalisis : padecimientos.getAnalisisCollection()) {
                Padecimientos oldIdPadecimientoOfAnalisisCollectionAnalisis = analisisCollectionAnalisis.getIdPadecimiento();
                analisisCollectionAnalisis.setIdPadecimiento(padecimientos);
                analisisCollectionAnalisis = em.merge(analisisCollectionAnalisis);
                if (oldIdPadecimientoOfAnalisisCollectionAnalisis != null) {
                    oldIdPadecimientoOfAnalisisCollectionAnalisis.getAnalisisCollection().remove(analisisCollectionAnalisis);
                    oldIdPadecimientoOfAnalisisCollectionAnalisis = em.merge(oldIdPadecimientoOfAnalisisCollectionAnalisis);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Padecimientos padecimientos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Padecimientos persistentPadecimientos = em.find(Padecimientos.class, padecimientos.getIdPadecimiento());
            Collection<Analisis> analisisCollectionOld = persistentPadecimientos.getAnalisisCollection();
            Collection<Analisis> analisisCollectionNew = padecimientos.getAnalisisCollection();
            List<String> illegalOrphanMessages = null;
            for (Analisis analisisCollectionOldAnalisis : analisisCollectionOld) {
                if (!analisisCollectionNew.contains(analisisCollectionOldAnalisis)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Analisis " + analisisCollectionOldAnalisis + " since its idPadecimiento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Analisis> attachedAnalisisCollectionNew = new ArrayList<Analisis>();
            for (Analisis analisisCollectionNewAnalisisToAttach : analisisCollectionNew) {
                analisisCollectionNewAnalisisToAttach = em.getReference(analisisCollectionNewAnalisisToAttach.getClass(), analisisCollectionNewAnalisisToAttach.getIdAnalisis());
                attachedAnalisisCollectionNew.add(analisisCollectionNewAnalisisToAttach);
            }
            analisisCollectionNew = attachedAnalisisCollectionNew;
            padecimientos.setAnalisisCollection(analisisCollectionNew);
            padecimientos = em.merge(padecimientos);
            for (Analisis analisisCollectionNewAnalisis : analisisCollectionNew) {
                if (!analisisCollectionOld.contains(analisisCollectionNewAnalisis)) {
                    Padecimientos oldIdPadecimientoOfAnalisisCollectionNewAnalisis = analisisCollectionNewAnalisis.getIdPadecimiento();
                    analisisCollectionNewAnalisis.setIdPadecimiento(padecimientos);
                    analisisCollectionNewAnalisis = em.merge(analisisCollectionNewAnalisis);
                    if (oldIdPadecimientoOfAnalisisCollectionNewAnalisis != null && !oldIdPadecimientoOfAnalisisCollectionNewAnalisis.equals(padecimientos)) {
                        oldIdPadecimientoOfAnalisisCollectionNewAnalisis.getAnalisisCollection().remove(analisisCollectionNewAnalisis);
                        oldIdPadecimientoOfAnalisisCollectionNewAnalisis = em.merge(oldIdPadecimientoOfAnalisisCollectionNewAnalisis);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = padecimientos.getIdPadecimiento();
                if (findPadecimientos(id) == null) {
                    throw new NonexistentEntityException("The padecimientos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Padecimientos padecimientos;
            try {
                padecimientos = em.getReference(Padecimientos.class, id);
                padecimientos.getIdPadecimiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The padecimientos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Analisis> analisisCollectionOrphanCheck = padecimientos.getAnalisisCollection();
            for (Analisis analisisCollectionOrphanCheckAnalisis : analisisCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Padecimientos (" + padecimientos + ") cannot be destroyed since the Analisis " + analisisCollectionOrphanCheckAnalisis + " in its analisisCollection field has a non-nullable idPadecimiento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(padecimientos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Padecimientos> findPadecimientosEntities() {
        return findPadecimientosEntities(true, -1, -1);
    }

    public List<Padecimientos> findPadecimientosEntities(int maxResults, int firstResult) {
        return findPadecimientosEntities(false, maxResults, firstResult);
    }

    private List<Padecimientos> findPadecimientosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Padecimientos.class));
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

    public Padecimientos findPadecimientos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Padecimientos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPadecimientosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Padecimientos> rt = cq.from(Padecimientos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
