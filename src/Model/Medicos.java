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
@Table(name = "medicos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicos.findAll", query = "SELECT m FROM Medicos m"),
    @NamedQuery(name = "Medicos.findByIdMedico", query = "SELECT m FROM Medicos m WHERE m.idMedico = :idMedico"),
    @NamedQuery(name = "Medicos.findByNombres", query = "SELECT m FROM Medicos m WHERE m.nombres = :nombres"),
    @NamedQuery(name = "Medicos.findByApellidos", query = "SELECT m FROM Medicos m WHERE m.apellidos = :apellidos"),
    @NamedQuery(name = "Medicos.findByDireccion", query = "SELECT m FROM Medicos m WHERE m.direccion = :direccion"),
    @NamedQuery(name = "Medicos.findByTelefono", query = "SELECT m FROM Medicos m WHERE m.telefono = :telefono"),
    @NamedQuery(name = "Medicos.findByCelular", query = "SELECT m FROM Medicos m WHERE m.celular = :celular"),
    @NamedQuery(name = "Medicos.findByEmail", query = "SELECT m FROM Medicos m WHERE m.email = :email"),
    @NamedQuery(name = "Medicos.findByEspecializacion", query = "SELECT m FROM Medicos m WHERE m.especializacion = :especializacion")})
public class Medicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMedico")
    private Integer idMedico;
    @Basic(optional = false)
    @Column(name = "Nombres")
    private String nombres;
    @Basic(optional = false)
    @Column(name = "Apellidos")
    private String apellidos;
    @Column(name = "Direccion")
    private String direccion;
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "celular")
    private String celular;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "Especializacion")
    private String especializacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMedico")
    private Collection<Ingresos> ingresosCollection;

    public Medicos() {
    }

    public Medicos(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public Medicos(Integer idMedico, String nombres, String apellidos, String celular, String especializacion) {
        this.idMedico = idMedico;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.especializacion = especializacion;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
    }

    @XmlTransient
    public Collection<Ingresos> getIngresosCollection() {
        return ingresosCollection;
    }

    public void setIngresosCollection(Collection<Ingresos> ingresosCollection) {
        this.ingresosCollection = ingresosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedico != null ? idMedico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicos)) {
            return false;
        }
        Medicos other = (Medicos) object;
        if ((this.idMedico == null && other.idMedico != null) || (this.idMedico != null && !this.idMedico.equals(other.idMedico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Medicos[ idMedico=" + idMedico + " ]";
    }
    
}
