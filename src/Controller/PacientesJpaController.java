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
import Model.Pacientes;
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
public class PacientesJpaController implements Serializable {

    public PacientesJpaController() {
        emf = Persistence.createEntityManagerFactory("PUClinicas");
        emf.createEntityManager();
    }

    public PacientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pacientes pacientes) {
        if (pacientes.getIngresosCollection() == null) {
            pacientes.setIngresosCollection(new ArrayList<Ingresos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Ingresos> attachedIngresosCollection = new ArrayList<Ingresos>();
            for (Ingresos ingresosCollectionIngresosToAttach : pacientes.getIngresosCollection()) {
                ingresosCollectionIngresosToAttach = em.getReference(ingresosCollectionIngresosToAttach.getClass(), ingresosCollectionIngresosToAttach.getIdIngreso());
                attachedIngresosCollection.add(ingresosCollectionIngresosToAttach);
            }
            pacientes.setIngresosCollection(attachedIngresosCollection);
            em.persist(pacientes);
            for (Ingresos ingresosCollectionIngresos : pacientes.getIngresosCollection()) {
                Pacientes oldIdPacienteOfIngresosCollectionIngresos = ingresosCollectionIngresos.getIdPaciente();
                ingresosCollectionIngresos.setIdPaciente(pacientes);
                ingresosCollectionIngresos = em.merge(ingresosCollectionIngresos);
                if (oldIdPacienteOfIngresosCollectionIngresos != null) {
                    oldIdPacienteOfIngresosCollectionIngresos.getIngresosCollection().remove(ingresosCollectionIngresos);
                    oldIdPacienteOfIngresosCollectionIngresos = em.merge(oldIdPacienteOfIngresosCollectionIngresos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pacientes pacientes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes persistentPacientes = em.find(Pacientes.class, pacientes.getIdPaciente());
            Collection<Ingresos> ingresosCollectionOld = persistentPacientes.getIngresosCollection();
            Collection<Ingresos> ingresosCollectionNew = pacientes.getIngresosCollection();
            List<String> illegalOrphanMessages = null;
            for (Ingresos ingresosCollectionOldIngresos : ingresosCollectionOld) {
                if (!ingresosCollectionNew.contains(ingresosCollectionOldIngresos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ingresos " + ingresosCollectionOldIngresos + " since its idPaciente field is not nullable.");
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
            pacientes.setIngresosCollection(ingresosCollectionNew);
            pacientes = em.merge(pacientes);
            for (Ingresos ingresosCollectionNewIngresos : ingresosCollectionNew) {
                if (!ingresosCollectionOld.contains(ingresosCollectionNewIngresos)) {
                    Pacientes oldIdPacienteOfIngresosCollectionNewIngresos = ingresosCollectionNewIngresos.getIdPaciente();
                    ingresosCollectionNewIngresos.setIdPaciente(pacientes);
                    ingresosCollectionNewIngresos = em.merge(ingresosCollectionNewIngresos);
                    if (oldIdPacienteOfIngresosCollectionNewIngresos != null && !oldIdPacienteOfIngresosCollectionNewIngresos.equals(pacientes)) {
                        oldIdPacienteOfIngresosCollectionNewIngresos.getIngresosCollection().remove(ingresosCollectionNewIngresos);
                        oldIdPacienteOfIngresosCollectionNewIngresos = em.merge(oldIdPacienteOfIngresosCollectionNewIngresos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pacientes.getIdPaciente();
                if (findPacientes(id) == null) {
                    throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.");
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
            Pacientes pacientes;
            try {
                pacientes = em.getReference(Pacientes.class, id);
                pacientes.getIdPaciente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ingresos> ingresosCollectionOrphanCheck = pacientes.getIngresosCollection();
            for (Ingresos ingresosCollectionOrphanCheckIngresos : ingresosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pacientes (" + pacientes + ") cannot be destroyed since the Ingresos " + ingresosCollectionOrphanCheckIngresos + " in its ingresosCollection field has a non-nullable idPaciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pacientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pacientes> findPacientesEntities() {
        return findPacientesEntities(true, -1, -1);
    }

    public List<Pacientes> findPacientesEntities(int maxResults, int firstResult) {
        return findPacientesEntities(false, maxResults, firstResult);
    }

    private List<Pacientes> findPacientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pacientes.class));
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

    public Pacientes findPacientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pacientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pacientes> rt = cq.from(Pacientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
