/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jorge
 */
@Entity
@Table(name = "medicamentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicamentos.findAll", query = "SELECT m FROM Medicamentos m"),
    @NamedQuery(name = "Medicamentos.findByIdMedicamento", query = "SELECT m FROM Medicamentos m WHERE m.idMedicamento = :idMedicamento"),
    @NamedQuery(name = "Medicamentos.findByNombre", query = "SELECT m FROM Medicamentos m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Medicamentos.findByPresentacion", query = "SELECT m FROM Medicamentos m WHERE m.presentacion = :presentacion"),
    @NamedQuery(name = "Medicamentos.findByCasaProductora", query = "SELECT m FROM Medicamentos m WHERE m.casaProductora = :casaProductora"),
    @NamedQuery(name = "Medicamentos.findByDescripcion", query = "SELECT m FROM Medicamentos m WHERE m.descripcion = :descripcion")})
public class Medicamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMedicamento")
    private Integer idMedicamento;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "Presentacion")
    private String presentacion;
    @Basic(optional = false)
    @Column(name = "casaProductora")
    private String casaProductora;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "idMedicamento")
    private Collection<Analisis> analisisCollection;

    public Medicamentos() {
    }

    public Medicamentos(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Medicamentos(Integer idMedicamento, String nombre, String presentacion, String casaProductora, String descripcion) {
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.casaProductora = casaProductora;
        this.descripcion = descripcion;
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getCasaProductora() {
        return casaProductora;
    }

    public void setCasaProductora(String casaProductora) {
        this.casaProductora = casaProductora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Analisis> getAnalisisCollection() {
        return analisisCollection;
    }

    public void setAnalisisCollection(Collection<Analisis> analisisCollection) {
        this.analisisCollection = analisisCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedicamento != null ? idMedicamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicamentos)) {
            return false;
        }
        Medicamentos other = (Medicamentos) object;
        if ((this.idMedicamento == null && other.idMedicamento != null) || (this.idMedicamento != null && !this.idMedicamento.equals(other.idMedicamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Medicamentos[ idMedicamento=" + idMedicamento + " ]";
    }
    
}
