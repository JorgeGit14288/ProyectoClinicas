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
import Model.Ingresos;
import Model.Medicos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jorge
 */
public class MedicosJpaController implements Serializable {
    
    public MedicosJpaController()
    {
        emf=Persistence.createEntityManagerFactory("PUClinicas");
        emf.createEntityManager();
    }

    public MedicosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicos medicos) {
        if (medicos.getIngresosCollection() == null) {
            medicos.setIngresosCollection(new ArrayList<Ingresos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Ingresos> attachedIngresosCollection = new ArrayList<Ingresos>();
            for (Ingresos ingresosCollectionIngresosToAttach : medicos.getIngresosCollection()) {
                ingresosCollectionIngresosToAttach = em.getReference(ingresosCollectionIngresosToAttach.getClass(), ingresosCollectionIngresosToAttach.getIdIngreso());
                attachedIngresosCollection.add(ingresosCollectionIngresosToAttach);
            }
            medicos.setIngresosCollection(attachedIngresosCollection);
            em.persist(medicos);
            for (Ingresos ingresosCollectionIngresos : medicos.getIngresosCollection()) {
                Medicos oldIdMedicoOfIngresosCollectionIngresos = ingresosCollectionIngresos.getIdMedico();
                ingresosCollectionIngresos.setIdMedico(medicos);
                ingresosCollectionIngresos = em.merge(ingresosCollectionIngresos);
                if (oldIdMedicoOfIngresosCollectionIngresos != null) {
                    oldIdMedicoOfIngresosCollectionIngresos.getIngresosCollection().remove(ingresosCollectionIngresos);
                    oldIdMedicoOfIngresosCollectionIngresos = em.merge(oldIdMedicoOfIngresosCollectionIngresos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medicos medicos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicos persistentMedicos = em.find(Medicos.class, medicos.getIdMedico());
            Collection<Ingresos> ingresosCollectionOld = persistentMedicos.getIngresosCollection();
            Collection<Ingresos> ingresosCollectionNew = medicos.getIngresosCollection();
            List<String> illegalOrphanMessages = null;
            for (Ingresos ingresosCollectionOldIngresos : ingresosCollectionOld) {
                if (!ingresosCollectionNew.contains(ingresosCollectionOldIngresos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ingresos " + ingresosCollectionOldIngresos + " since its idMedico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Ingresos> attachedIngresosCollectionNew = new ArrayList<Ingresos>();
            for (Ingresos ingresosCollectionNewIngresosToAttach : ingresosCollectionNew) {
                ingresosCollectionNewIngresosToAttach = em.getReference(ingresosCollectionNewIngresosToAttach.getClass(), ingresosCollectionNewIngresosToAttach.getIdIngreso());
                attachedIngresosCollectionNew.add(ingresosCollectionNewIngresosToAttach);
            }
            ingresosCollectionNew = attachedIngresosCollectionNew;
            medicos.setIngresosCollection(ingresosCollectionNew);
            medicos = em.merge(medicos);
            for (Ingresos ingresosCollectionNewIngresos : ingresosCollectionNew) {
                if (!ingresosCollectionOld.contains(ingresosCollectionNewIngresos)) {
                    Medicos oldIdMedicoOfIngresosCollectionNewIngresos = ingresosCollectionNewIngresos.getIdMedico();
                    ingresosCollectionNewIngresos.setIdMedico(medicos);
                    ingresosCollectionNewIngresos = em.merge(ingresosCollectionNewIngresos);
                    if (oldIdMedicoOfIngresosCollectionNewIngresos != null && !oldIdMedicoOfIngresosCollectionNewIngresos.equals(medicos)) {
                        oldIdMedicoOfIngresosCollectionNewIngresos.getIngresosCollection().remove(ingresosCollectionNewIngresos);
                        oldIdMedicoOfIngresosCollectionNewIngresos = em.merge(oldIdMedicoOfIngresosCollectionNewIngresos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medicos.getIdMedico();
                if (findMedicos(id) == null) {
                    throw new NonexistentEntityException("The medicos with id " + id + " no longer exists.");
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
            Medicos medicos;
            try {
                medicos = em.getReference(Medicos.class, id);
                medicos.getIdMedico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ingresos> ingresosCollectionOrphanCheck = medicos.getIngresosCollection();
            for (Ingresos ingresosCollectionOrphanCheckIngresos : ingresosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicos (" + medicos + ") cannot be destroyed since the Ingresos " + ingresosCollectionOrphanCheckIngresos + " in its ingresosCollection field has a non-nullable idMedico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(medicos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medicos> findMedicosEntities() {
        return findMedicosEntities(true, -1, -1);
    }

    public List<Medicos> findMedicosEntities(int maxResults, int firstResult) {
        return findMedicosEntities(false, maxResults, firstResult);
    }

    private List<Medicos> findMedicosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicos.class));
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

    public Medicos findMedicos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicos.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicos> rt = cq.from(Medicos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
