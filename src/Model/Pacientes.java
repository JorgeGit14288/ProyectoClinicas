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
@Table(name = "pacientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pacientes.findAll", query = "SELECT p FROM Pacientes p"),
    @NamedQuery(name = "Pacientes.findByIdPaciente", query = "SELECT p FROM Pacientes p WHERE p.idPaciente = :idPaciente"),
    @NamedQuery(name = "Pacientes.findByNombres", query = "SELECT p FROM Pacientes p WHERE p.nombres = :nombres"),
    @NamedQuery(name = "Pacientes.findByApellidos", query = "SELECT p FROM Pacientes p WHERE p.apellidos = :apellidos"),
    @NamedQuery(name = "Pacientes.findByTelefono", query = "SELECT p FROM Pacientes p WHERE p.telefono = :telefono"),
    @NamedQuery(name = "Pacientes.findByCelular", query = "SELECT p FROM Pacientes p WHERE p.celular = :celular"),
    @NamedQuery(name = "Pacientes.findByEmail", query = "SELECT p FROM Pacientes p WHERE p.email = :email"),
    @NamedQuery(name = "Pacientes.findByDireccion", query = "SELECT p FROM Pacientes p WHERE p.direccion = :direccion"),
    @NamedQuery(name = "Pacientes.findByFechaNacimiento", query = "SELECT p FROM Pacientes p WHERE p.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Pacientes.findByPeso", query = "SELECT p FROM Pacientes p WHERE p.peso = :peso"),
    @NamedQuery(name = "Pacientes.findByAltura", query = "SELECT p FROM Pacientes p WHERE p.altura = :altura"),
    @NamedQuery(name = "Pacientes.findByAlergias", query = "SELECT p FROM Pacientes p WHERE p.alergias = :alergias")})
public class Pacientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPaciente")
    private Integer idPaciente;
    @Basic(optional = false)
    @Column(name = "Nombres")
    private String nombres;
    @Basic(optional = false)
    @Column(name = "Apellidos")
    private String apellidos;
    @Column(name = "Telefono")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "Celular")
    private String celular;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "Direccion")
    private String direccion;
    @Column(name = "fechaNacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "Peso")
    private Long peso;
    @Column(name = "altura")
    private Long altura;
    @Column(name = "Alergias")
    private String alergias;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPaciente")
    private Collection<Ingresos> ingresosCollection;

    public Pacientes() {
    }

    public Pacientes(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Pacientes(Integer idPaciente, String nombres, String apellidos, String celular, String direccion) {
        this.idPaciente = idPaciente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.direccion = direccion;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getPeso() {
        return peso;
    }

    public void setPeso(Long peso) {
        this.peso = peso;
    }

    public Long getAltura() {
        return altura;
    }

    public void setAltura(Long altura) {
        this.altura = altura;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
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
        hash += (idPaciente != null ? idPaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pacientes)) {
            return false;
        }
        Pacientes other = (Pacientes) object;
        if ((this.idPaciente == null && other.idPaciente != null) || (this.idPaciente != null && !this.idPaciente.equals(other.idPaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Pacientes[ idPaciente=" + idPaciente + " ]";
    }
    
}
