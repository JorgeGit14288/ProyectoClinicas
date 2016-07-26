/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jorge
 */
@Entity
@Table(name = "ingresos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingresos.findAll", query = "SELECT i FROM Ingresos i"),
    @NamedQuery(name = "Ingresos.findByIdIngreso", query = "SELECT i FROM Ingresos i WHERE i.idIngreso = :idIngreso"),
    @NamedQuery(name = "Ingresos.findByDescripcion", query = "SELECT i FROM Ingresos i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "Ingresos.findByFecha", query = "SELECT i FROM Ingresos i WHERE i.fecha = :fecha"),
    @NamedQuery(name = "Ingresos.findByHora", query = "SELECT i FROM Ingresos i WHERE i.hora = :hora"),
    @NamedQuery(name = "Ingresos.findByCosto", query = "SELECT i FROM Ingresos i WHERE i.costo = :costo"),
    @NamedQuery(name = "Ingresos.findByStatus", query = "SELECT i FROM Ingresos i WHERE i.status = :status"),
    @NamedQuery(name = "Ingresos.findByTipoIngreso", query = "SELECT i FROM Ingresos i WHERE i.tipoIngreso = :tipoIngreso"),
    @NamedQuery(name = "Ingresos.findByHabitacion", query = "SELECT i FROM Ingresos i WHERE i.habitacion = :habitacion"),
    @NamedQuery(name = "Ingresos.findByCama", query = "SELECT i FROM Ingresos i WHERE i.cama = :cama"),
    @NamedQuery(name = "Ingresos.findByFechaAlta", query = "SELECT i FROM Ingresos i WHERE i.fechaAlta = :fechaAlta")})
public class Ingresos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idIngreso")
    private Integer idIngreso;
    @Column(name = "Descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "costo")
    private Long costo;
    @Column(name = "Status")
    private String status;
    @Column(name = "TipoIngreso")
    private String tipoIngreso;
    @Column(name = "Habitacion")
    private Integer habitacion;
    @Column(name = "Cama")
    private Integer cama;
    @Column(name = "FechaAlta")
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;
    @JoinColumn(name = "idMedico", referencedColumnName = "idMedico")
    @ManyToOne(optional = false)
    private Medicos idMedico;
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente")
    @ManyToOne(optional = false)
    private Pacientes idPaciente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idIngreso")
    private Collection<Analisis> analisisCollection;

    public Ingresos() {
    }

    public Ingresos(Integer idIngreso) {
        this.idIngreso = idIngreso;
    }

    public Ingresos(Integer idIngreso, Date fecha, Date hora) {
        this.idIngreso = idIngreso;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Integer getIdIngreso() {
        return idIngreso;
    }

    public void setIdIngreso(Integer idIngreso) {
        this.idIngreso = idIngreso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Long getCosto() {
        return costo;
    }

    public void setCosto(Long costo) {
        this.costo = costo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(String tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    public Integer getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Integer habitacion) {
        this.habitacion = habitacion;
    }

    public Integer getCama() {
        return cama;
    }

    public void setCama(Integer cama) {
        this.cama = cama;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Medicos getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Medicos idMedico) {
        this.idMedico = idMedico;
    }

    public Pacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Pacientes idPaciente) {
        this.idPaciente = idPaciente;
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
        hash += (idIngreso != null ? idIngreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingresos)) {
            return false;
        }
        Ingresos other = (Ingresos) object;
        if ((this.idIngreso == null && other.idIngreso != null) || (this.idIngreso != null && !this.idIngreso.equals(other.idIngreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Ingresos[ idIngreso=" + idIngreso + " ]";
    }
    
}
