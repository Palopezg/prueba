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
 * Criteria class for the {@link com.mycompany.myapp.domain.Segmento} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SegmentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /segmentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SegmentoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descripcion;

    private StringFilter valor;

    private LongFilter accExecId;

    public SegmentoCriteria() {
    }

    public SegmentoCriteria(SegmentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.accExecId = other.accExecId == null ? null : other.accExecId.copy();
    }

    @Override
    public SegmentoCriteria copy() {
        return new SegmentoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getValor() {
        return valor;
    }

    public void setValor(StringFilter valor) {
        this.valor = valor;
    }

    public LongFilter getAccExecId() {
        return accExecId;
    }

    public void setAccExecId(LongFilter accExecId) {
        this.accExecId = accExecId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SegmentoCriteria that = (SegmentoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(accExecId, that.accExecId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        descripcion,
        valor,
        accExecId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SegmentoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
                (valor != null ? "valor=" + valor + ", " : "") +
                (accExecId != null ? "accExecId=" + accExecId + ", " : "") +
            "}";
    }

}
