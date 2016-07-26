/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import Model.Analisis;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Ingresos;
import Model.Padecimientos;
import Model.Medicamentos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jorge
 */
public class AnalisisJpaController implements Serializable {

    public AnalisisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Analisis analisis) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingresos idIngreso = analisis.getIdIngreso();
            if (idIngreso != null) {
                idIngreso = em.getReference(idIngreso.getClass(), idIngreso.getIdIngreso());
                analisis.setIdIngreso(idIngreso);
            }
            Padecimientos idPadecimiento = analisis.getIdPadecimiento();
            if (idPadecimiento != null) {
                idPadecimiento = em.getReference(idPadecimiento.getClass(), idPadecimiento.getIdPadecimiento());
                analisis.setIdPadecimiento(idPadecimiento);
            }
            Medicamentos idMedicamento = analisis.getIdMedicamento();
            if (idMedicamento != null) {
                idMedicamento = em.getReference(idMedicamento.getClass(), idMedicamento.getIdMedicamento());
                analisis.setIdMedicamento(idMedicamento);
            }
            em.persist(analisis);
            if (idIngreso != null) {
                idIngreso.getAnalisisCollection().add(analisis);
                idIngreso = em.merge(idIngreso);
            }
            if (idPadecimiento != null) {
                idPadecimiento.getAnalisisCollection().add(analisis);
                idPadecimiento = em.merge(idPadecimiento);
            }
            if (idMedicamento != null) {
                idMedicamento.getAnalisisCollection().add(analisis);
                idMedicamento = em.merge(idMedicamento);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnalisis(analisis.getIdAnalisis()) != null) {
                throw new PreexistingEntityException("Analisis " + analisis + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Analisis analisis) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Analisis persistentAnalisis = em.find(Analisis.class, analisis.getIdAnalisis());
            Ingresos idIngresoOld = persistentAnalisis.getIdIngreso();
            Ingresos idIngresoNew = analisis.getIdIngreso();
            Padecimientos idPadecimientoOld = persistentAnalisis.getIdPadecimiento();
            Padecimientos idPadecimientoNew = analisis.getIdPadecimiento();
            Medicamentos idMedicamentoOld = persistentAnalisis.getIdMedicamento();
            Medicamentos idMedicamentoNew = analisis.getIdMedicamento();
            if (idIngresoNew != null) {
                idIngresoNew = em.getReference(idIngresoNew.getClass(), idIngresoNew.getIdIngreso());
                analisis.setIdIngreso(idIngresoNew);
            }
            if (idPadecimientoNew != null) {
                idPadecimientoNew = em.getReference(idPadecimientoNew.getClass(), idPadecimientoNew.getIdPadecimiento());
                analisis.setIdPadecimiento(idPadecimientoNew);
            }
            if (idMedicamentoNew != null) {
                idMedicamentoNew = em.getReference(idMedicamentoNew.getClass(), idMedicamentoNew.getIdMedicamento());
                analisis.setIdMedicamento(idMedicamentoNew);
            }
            analisis = em.merge(analisis);
            if (idIngresoOld != null && !idIngresoOld.equals(idIngresoNew)) {
                idIngresoOld.getAnalisisCollection().remove(analisis);
                idIngresoOld = em.merge(idIngresoOld);
            }
            if (idIngresoNew != null && !idIngresoNew.equals(idIngresoOld)) {
                idIngresoNew.getAnalisisCollection().add(analisis);
                idIngresoNew = em.merge(idIngresoNew);
            }
            if (idPadecimientoOld != null && !idPadecimientoOld.equals(idPadecimientoNew)) {
                idPadecimientoOld.getAnalisisCollection().remove(analisis);
                idPadecimientoOld = em.merge(idPadecimientoOld);
            }
            if (idPadecimientoNew != null && !idPadecimientoNew.equals(idPadecimientoOld)) {
                idPadecimientoNew.getAnalisisCollection().add(analisis);
                idPadecimientoNew = em.merge(idPadecimientoNew);
            }
            if (idMedicamentoOld != null && !idMedicamentoOld.equals(idMedicamentoNew)) {
                idMedicamentoOld.getAnalisisCollection().remove(analisis);
                idMedicamentoOld = em.merge(idMedicamentoOld);
            }
            if (idMedicamentoNew != null && !idMedicamentoNew.equals(idMedicamentoOld)) {
                idMedicamentoNew.getAnalisisCollection().add(analisis);
                idMedicamentoNew = em.merge(idMedicamentoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = analisis.getIdAnalisis();
                if (findAnalisis(id) == null) {
                    throw new NonexistentEntityException("The analisis with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Analisis analisis;
            try {
                analisis = em.getReference(Analisis.class, id);
                analisis.getIdAnalisis();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The analisis with id " + id + " no longer exists.", enfe);
            }
            Ingresos idIngreso = analisis.getIdIngreso();
            if (idIngreso != null) {
                idIngreso.getAnalisisCollection().remove(analisis);
                idIngreso = em.merge(idIngreso);
            }
            Padecimientos idPadecimiento = analisis.getIdPadecimiento();
            if (idPadecimiento != null) {
                idPadecimiento.getAnalisisCollection().remove(analisis);
                idPadecimiento = em.merge(idPadecimiento);
            }
            Medicamentos idMedicamento = analisis.getIdMedicamento();
            if (idMedicamento != null) {
                idMedicamento.getAnalisisCollection().remove(analisis);
                idMedicamento = em.merge(idMedicamento);
            }
            em.remove(analisis);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Analisis> findAnalisisEntities() {
        return findAnalisisEntities(true, -1, -1);
    }

    public List<Analisis> findAnalisisEntities(int maxResults, int firstResult) {
        return findAnalisisEntities(false, maxResults, firstResult);
    }

    private List<Analisis> findAnalisisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Analisis.class));
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

    public Analisis findAnalisis(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Analisis.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnalisisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Analisis> rt = cq.from(Analisis.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
