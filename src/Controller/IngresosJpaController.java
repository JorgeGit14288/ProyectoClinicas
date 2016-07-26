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
import Model.Medicos;
import Model.Pacientes;
import Model.Analisis;
import Model.Ingresos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jorge
 */
public class IngresosJpaController implements Serializable {

    public IngresosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ingresos ingresos) {
        if (ingresos.getAnalisisCollection() == null) {
            ingresos.setAnalisisCollection(new ArrayList<Analisis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicos idMedico = ingresos.getIdMedico();
            if (idMedico != null) {
                idMedico = em.getReference(idMedico.getClass(), idMedico.getIdMedico());
                ingresos.setIdMedico(idMedico);
            }
            Pacientes idPaciente = ingresos.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getIdPaciente());
                ingresos.setIdPaciente(idPaciente);
            }
            Collection<Analisis> attachedAnalisisCollection = new ArrayList<Analisis>();
            for (Analisis analisisCollectionAnalisisToAttach : ingresos.getAnalisisCollection()) {
                analisisCollectionAnalisisToAttach = em.getReference(analisisCollectionAnalisisToAttach.getClass(), analisisCollectionAnalisisToAttach.getIdAnalisis());
                attachedAnalisisCollection.add(analisisCollectionAnalisisToAttach);
            }
            ingresos.setAnalisisCollection(attachedAnalisisCollection);
            em.persist(ingresos);
            if (idMedico != null) {
                idMedico.getIngresosCollection().add(ingresos);
                idMedico = em.merge(idMedico);
            }
            if (idPaciente != null) {
                idPaciente.getIngresosCollection().add(ingresos);
                idPaciente = em.merge(idPaciente);
            }
            for (Analisis analisisCollectionAnalisis : ingresos.getAnalisisCollection()) {
                Ingresos oldIdIngresoOfAnalisisCollectionAnalisis = analisisCollectionAnalisis.getIdIngreso();
                analisisCollectionAnalisis.setIdIngreso(ingresos);
                analisisCollectionAnalisis = em.merge(analisisCollectionAnalisis);
                if (oldIdIngresoOfAnalisisCollectionAnalisis != null) {
                    oldIdIngresoOfAnalisisCollectionAnalisis.getAnalisisCollection().remove(analisisCollectionAnalisis);
                    oldIdIngresoOfAnalisisCollectionAnalisis = em.merge(oldIdIngresoOfAnalisisCollectionAnalisis);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ingresos ingresos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingresos persistentIngresos = em.find(Ingresos.class, ingresos.getIdIngreso());
            Medicos idMedicoOld = persistentIngresos.getIdMedico();
            Medicos idMedicoNew = ingresos.getIdMedico();
            Pacientes idPacienteOld = persistentIngresos.getIdPaciente();
            Pacientes idPacienteNew = ingresos.getIdPaciente();
            Collection<Analisis> analisisCollectionOld = persistentIngresos.getAnalisisCollection();
            Collection<Analisis> analisisCollectionNew = ingresos.getAnalisisCollection();
            List<String> illegalOrphanMessages = null;
            for (Analisis analisisCollectionOldAnalisis : analisisCollectionOld) {
                if (!analisisCollectionNew.contains(analisisCollectionOldAnalisis)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Analisis " + analisisCollectionOldAnalisis + " since its idIngreso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idMedicoNew != null) {
                idMedicoNew = em.getReference(idMedicoNew.getClass(), idMedicoNew.getIdMedico());
                ingresos.setIdMedico(idMedicoNew);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getIdPaciente());
                ingresos.setIdPaciente(idPacienteNew);
            }
            Collection<Analisis> attachedAnalisisCollectionNew = new ArrayList<Analisis>();
            for (Analisis analisisCollectionNewAnalisisToAttach : analisisCollectionNew) {
                analisisCollectionNewAnalisisToAttach = em.getReference(analisisCollectionNewAnalisisToAttach.getClass(), analisisCollectionNewAnalisisToAttach.getIdAnalisis());
                attachedAnalisisCollectionNew.add(analisisCollectionNewAnalisisToAttach);
            }
            analisisCollectionNew = attachedAnalisisCollectionNew;
            ingresos.setAnalisisCollection(analisisCollectionNew);
            ingresos = em.merge(ingresos);
            if (idMedicoOld != null && !idMedicoOld.equals(idMedicoNew)) {
                idMedicoOld.getIngresosCollection().remove(ingresos);
                idMedicoOld = em.merge(idMedicoOld);
            }
            if (idMedicoNew != null && !idMedicoNew.equals(idMedicoOld)) {
                idMedicoNew.getIngresosCollection().add(ingresos);
                idMedicoNew = em.merge(idMedicoNew);
            }
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getIngresosCollection().remove(ingresos);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getIngresosCollection().add(ingresos);
                idPacienteNew = em.merge(idPacienteNew);
            }
            for (Analisis analisisCollectionNewAnalisis : analisisCollectionNew) {
                if (!analisisCollectionOld.contains(analisisCollectionNewAnalisis)) {
                    Ingresos oldIdIngresoOfAnalisisCollectionNewAnalisis = analisisCollectionNewAnalisis.getIdIngreso();
                    analisisCollectionNewAnalisis.setIdIngreso(ingresos);
                    analisisCollectionNewAnalisis = em.merge(analisisCollectionNewAnalisis);
                    if (oldIdIngresoOfAnalisisCollectionNewAnalisis != null && !oldIdIngresoOfAnalisisCollectionNewAnalisis.equals(ingresos)) {
                        oldIdIngresoOfAnalisisCollectionNewAnalisis.getAnalisisCollection().remove(analisisCollectionNewAnalisis);
                        oldIdIngresoOfAnalisisCollectionNewAnalisis = em.merge(oldIdIngresoOfAnalisisCollectionNewAnalisis);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingresos.getIdIngreso();
                if (findIngresos(id) == null) {
                    throw new NonexistentEntityException("The ingresos with id " + id + " no longer exists.");
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
            Ingresos ingresos;
            try {
                ingresos = em.getReference(Ingresos.class, id);
                ingresos.getIdIngreso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingresos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Analisis> analisisCollectionOrphanCheck = ingresos.getAnalisisCollection();
            for (Analisis analisisCollectionOrphanCheckAnalisis : analisisCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ingresos (" + ingresos + ") cannot be destroyed since the Analisis " + analisisCollectionOrphanCheckAnalisis + " in its analisisCollection field has a non-nullable idIngreso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medicos idMedico = ingresos.getIdMedico();
            if (idMedico != null) {
                idMedico.getIngresosCollection().remove(ingresos);
                idMedico = em.merge(idMedico);
            }
            Pacientes idPaciente = ingresos.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getIngresosCollection().remove(ingresos);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(ingresos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ingresos> findIngresosEntities() {
        return findIngresosEntities(true, -1, -1);
    }

    public List<Ingresos> findIngresosEntities(int maxResults, int firstResult) {
        return findIngresosEntities(false, maxResults, firstResult);
    }

    private List<Ingresos> findIngresosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ingresos.class));
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

    public Ingresos findIngresos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ingresos.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngresosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ingresos> rt = cq.from(Ingresos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
