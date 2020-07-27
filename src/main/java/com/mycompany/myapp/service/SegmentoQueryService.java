package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Segmento;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.SegmentoRepository;
import com.mycompany.myapp.repository.search.SegmentoSearchRepository;
import com.mycompany.myapp.service.dto.SegmentoCriteria;

/**
 * Service for executing complex queries for {@link Segmento} entities in the database.
 * The main input is a {@link SegmentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Segmento} or a {@link Page} of {@link Segmento} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SegmentoQueryService extends QueryService<Segmento> {

    private final Logger log = LoggerFactory.getLogger(SegmentoQueryService.class);

    private final SegmentoRepository segmentoRepository;

    private final SegmentoSearchRepository segmentoSearchRepository;

    public SegmentoQueryService(SegmentoRepository segmentoRepository, SegmentoSearchRepository segmentoSearchRepository) {
        this.segmentoRepository = segmentoRepository;
        this.segmentoSearchRepository = segmentoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Segmento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Segmento> findByCriteria(SegmentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Segmento> specification = createSpecification(criteria);
        return segmentoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Segmento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Segmento> findByCriteria(SegmentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Segmento> specification = createSpecification(criteria);
        return segmentoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SegmentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Segmento> specification = createSpecification(criteria);
        return segmentoRepository.count(specification);
    }

    /**
     * Function to convert {@link SegmentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Segmento> createSpecification(SegmentoCriteria criteria) {
        Specification<Segmento> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Segmento_.id));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Segmento_.descripcion));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValor(), Segmento_.valor));
            }
            if (criteria.getAccExecId() != null) {
                specification = specification.and(buildSpecification(criteria.getAccExecId(),
                    root -> root.join(Segmento_.accExec, JoinType.LEFT).get(AccExec_.id)));
            }
        }
        return specification;
    }
}
