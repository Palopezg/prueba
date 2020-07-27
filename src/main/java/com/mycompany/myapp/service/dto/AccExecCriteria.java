package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.AccExec} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AccExecResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /acc-execs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccExecCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter apellido;

    private StringFilter telefono;

    private StringFilter celular;

    private StringFilter mail;

    private StringFilter repcom1;

    private StringFilter repcom2;

    private LongFilter segmentoId;

    private LongFilter regionId;

    public AccExecCriteria() {
    }

    public AccExecCriteria(AccExecCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.apellido = other.apellido == null ? null : other.apellido.copy();
        this.telefono = other.telefono == null ? null : other.telefono.copy();
        this.celular = other.celular == null ? null : other.celular.copy();
        this.mail = other.mail == null ? null : other.mail.copy();
        this.repcom1 = other.repcom1 == null ? null : other.repcom1.copy();
        this.repcom2 = other.repcom2 == null ? null : other.repcom2.copy();
        this.segmentoId = other.segmentoId == null ? null : other.segmentoId.copy();
        this.regionId = other.regionId == null ? null : other.regionId.copy();
    }

    @Override
    public AccExecCriteria copy() {
        return new AccExecCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getApellido() {
        return apellido;
    }

    public void setApellido(StringFilter apellido) {
        this.apellido = apellido;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getCelular() {
        return celular;
    }

    public void setCelular(StringFilter celular) {
        this.celular = celular;
    }

    public StringFilter getMail() {
        return mail;
    }

    public void setMail(StringFilter mail) {
        this.mail = mail;
    }

    public StringFilter getRepcom1() {
        return repcom1;
    }

    public void setRepcom1(StringFilter repcom1) {
        this.repcom1 = repcom1;
    }

    public StringFilter getRepcom2() {
        return repcom2;
    }

    public void setRepcom2(StringFilter repcom2) {
        this.repcom2 = repcom2;
    }

    public LongFilter getSegmentoId() {
        return segmentoId;
    }

    public void setSegmentoId(LongFilter segmentoId) {
        this.segmentoId = segmentoId;
    }

    public LongFilter getRegionId() {
        return regionId;
    }

    public void setRegionId(LongFilter regionId) {
        this.regionId = regionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AccExecCriteria that = (AccExecCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellido, that.apellido) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(celular, that.celular) &&
            Objects.equals(mail, that.mail) &&
            Objects.equals(repcom1, that.repcom1) &&
            Objects.equals(repcom2, that.repcom2) &&
            Objects.equals(segmentoId, that.segmentoId) &&
            Objects.equals(regionId, that.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        apellido,
        telefono,
        celular,
        mail,
        repcom1,
        repcom2,
        segmentoId,
        regionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccExecCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (apellido != null ? "apellido=" + apellido + ", " : "") +
                (telefono != null ? "telefono=" + telefono + ", " : "") +
                (celular != null ? "celular=" + celular + ", " : "") +
                (mail != null ? "mail=" + mail + ", " : "") +
                (repcom1 != null ? "repcom1=" + repcom1 + ", " : "") +
                (repcom2 != null ? "repcom2=" + repcom2 + ", " : "") +
                (segmentoId != null ? "segmentoId=" + segmentoId + ", " : "") +
                (regionId != null ? "regionId=" + regionId + ", " : "") +
            "}";
    }

}
