/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "padecimientos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Padecimientos.findAll", query = "SELECT p FROM Padecimientos p"),
    @NamedQuery(name = "Padecimientos.findByIdPadecimiento", query = "SELECT p FROM Padecimientos p WHERE p.idPadecimiento = :idPadecimiento"),
    @NamedQuery(name = "Padecimientos.findByNombre", query = "SELECT p FROM Padecimientos p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Padecimientos.findByDescripcion", query = "SELECT p FROM Padecimientos p WHERE p.descripcion = :descripcion")})
public class Padecimientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPadecimiento")
    private Integer idPadecimiento;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPadecimiento")
    private Collection<Analisis> analisisCollection;

    public Padecimientos() {
    }

    public Padecimientos(Integer idPadecimiento) {
        this.idPadecimiento = idPadecimiento;
    }

    public Padecimientos(Integer idPadecimiento, String nombre, String descripcion) {
        this.idPadecimiento = idPadecimiento;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdPadecimiento() {
        return idPadecimiento;
    }

    public void setIdPadecimiento(Integer idPadecimiento) {
        this.idPadecimiento = idPadecimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        hash += (idPadecimiento != null ? idPadecimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Padecimientos)) {
            return false;
        }
        Padecimientos other = (Padecimientos) object;
        if ((this.idPadecimiento == null && other.idPadecimiento != null) || (this.idPadecimiento != null && !this.idPadecimiento.equals(other.idPadecimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Padecimientos[ idPadecimiento=" + idPadecimiento + " ]";
    }
    
}
