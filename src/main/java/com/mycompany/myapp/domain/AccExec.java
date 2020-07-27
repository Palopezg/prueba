package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AccExec.
 */
@Entity
@Table(name = "acc_exec")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accexec")
public class AccExec implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "celular")
    private String celular;

    @Column(name = "mail")
    private String mail;

    @Column(name = "repcom_1")
    private String repcom1;

    @Column(name = "repcom_2")
    private String repcom2;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "acc_exec_region",
               joinColumns = @JoinColumn(name = "acc_exec_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "region_id", referencedColumnName = "id"))
    private Set<Region> regions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public AccExec nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public AccExec apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public AccExec telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public AccExec celular(String celular) {
        this.celular = celular;
        return this;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getMail() {
        return mail;
    }

    public AccExec mail(String mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRepcom1() {
        return repcom1;
    }

    public AccExec repcom1(String repcom1) {
        this.repcom1 = repcom1;
        return this;
    }

    public void setRepcom1(String repcom1) {
        this.repcom1 = repcom1;
    }

    public String getRepcom2() {
        return repcom2;
    }

    public AccExec repcom2(String repcom2) {
        this.repcom2 = repcom2;
        return this;
    }

    public void setRepcom2(String repcom2) {
        this.repcom2 = repcom2;
    }

    public Set<Region> getRegions() {
        return regions;
    }

    public AccExec regions(Set<Region> regions) {
        this.regions = regions;
        return this;
    }

    public AccExec addRegion(Region region) {
        this.regions.add(region);
        region.getAccExecs().add(this);
        return this;
    }

    public AccExec removeRegion(Region region) {
        this.regions.remove(region);
        region.getAccExecs().remove(this);
        return this;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccExec)) {
            return false;
        }
        return id != null && id.equals(((AccExec) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccExec{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", celular='" + getCelular() + "'" +
            ", mail='" + getMail() + "'" +
            ", repcom1='" + getRepcom1() + "'" +
            ", repcom2='" + getRepcom2() + "'" +
            "}";
    }
}
