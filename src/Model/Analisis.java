/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jorge
 */
@Entity
@Table(name = "analisis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Analisis.findAll", query = "SELECT a FROM Analisis a"),
    @NamedQuery(name = "Analisis.findByIdAnalisis", query = "SELECT a FROM Analisis a WHERE a.idAnalisis = :idAnalisis"),
    @NamedQuery(name = "Analisis.findByDescripcion", query = "SELECT a FROM Analisis a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Analisis.findByExamenes", query = "SELECT a FROM Analisis a WHERE a.examenes = :examenes"),
    @NamedQuery(name = "Analisis.findByResultado", query = "SELECT a FROM Analisis a WHERE a.resultado = :resultado"),
    @NamedQuery(name = "Analisis.findByDosis", query = "SELECT a FROM Analisis a WHERE a.dosis = :dosis")})
public class Analisis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idAnalisis")
    private String idAnalisis;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "Examenes")
    private String examenes;
    @Column(name = "Resultado")
    private String resultado;
    @Column(name = "Dosis")
    private String dosis;
    @JoinColumn(name = "idIngreso", referencedColumnName = "idIngreso")
    @ManyToOne(optional = false)
    private Ingresos idIngreso;
    @JoinColumn(name = "idPadecimiento", referencedColumnName = "idPadecimiento")
    @ManyToOne(optional = false)
    private Padecimientos idPadecimiento;
    @JoinColumn(name = "idMedicamento", referencedColumnName = "idMedicamento")
    @ManyToOne
    private Medicamentos idMedicamento;

    public Analisis() {
    }

    public Analisis(String idAnalisis) {
        this.idAnalisis = idAnalisis;
    }

    public Analisis(String idAnalisis, String descripcion) {
        this.idAnalisis = idAnalisis;
        this.descripcion = descripcion;
    }

    public String getIdAnalisis() {
        return idAnalisis;
    }

    public void setIdAnalisis(String idAnalisis) {
        this.idAnalisis = idAnalisis;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getExamenes() {
        return examenes;
    }

    public void setExamenes(String examenes) {
        this.examenes = examenes;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public Ingresos getIdIngreso() {
        return idIngreso;
    }

    public void setIdIngreso(Ingresos idIngreso) {
        this.idIngreso = idIngreso;
    }

    public Padecimientos getIdPadecimiento() {
        return idPadecimiento;
    }

    public void setIdPadecimiento(Padecimientos idPadecimiento) {
        this.idPadecimiento = idPadecimiento;
    }

    public Medicamentos getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Medicamentos idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnalisis != null ? idAnalisis.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Analisis)) {
            return false;
        }
        Analisis other = (Analisis) object;
        if ((this.idAnalisis == null && other.idAnalisis != null) || (this.idAnalisis != null && !this.idAnalisis.equals(other.idAnalisis))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Analisis[ idAnalisis=" + idAnalisis + " ]";
    }
    
}
