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

import com.mycompany.myapp.domain.AccExec;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.AccExecRepository;
import com.mycompany.myapp.repository.search.AccExecSearchRepository;
import com.mycompany.myapp.service.dto.AccExecCriteria;

/**
 * Service for executing complex queries for {@link AccExec} entities in the database.
 * The main input is a {@link AccExecCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccExec} or a {@link Page} of {@link AccExec} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccExecQueryService extends QueryService<AccExec> {

    private final Logger log = LoggerFactory.getLogger(AccExecQueryService.class);

    private final AccExecRepository accExecRepository;

    private final AccExecSearchRepository accExecSearchRepository;

    public AccExecQueryService(AccExecRepository accExecRepository, AccExecSearchRepository accExecSearchRepository) {
        this.accExecRepository = accExecRepository;
        this.accExecSearchRepository = accExecSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AccExec} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccExec> findByCriteria(AccExecCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccExec> specification = createSpecification(criteria);
        return accExecRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AccExec} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccExec> findByCriteria(AccExecCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccExec> specification = createSpecification(criteria);
        return accExecRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccExecCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccExec> specification = createSpecification(criteria);
        return accExecRepository.count(specification);
    }

    /**
     * Function to convert {@link AccExecCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccExec> createSpecification(AccExecCriteria criteria) {
        Specification<AccExec> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccExec_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), AccExec_.nombre));
            }
            if (criteria.getApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellido(), AccExec_.apellido));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), AccExec_.telefono));
            }
            if (criteria.getCelular() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCelular(), AccExec_.celular));
            }
            if (criteria.getMail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMail(), AccExec_.mail));
            }
            if (criteria.getRepcom1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRepcom1(), AccExec_.repcom1));
            }
            if (criteria.getRepcom2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRepcom2(), AccExec_.repcom2));
            }
            if (criteria.getSegmentoId() != null) {
                specification = specification.and(buildSpecification(criteria.getSegmentoId(),
                    root -> root.join(AccExec_.segmentos, JoinType.LEFT).get(Segmento_.id)));
            }
            if (criteria.getRegionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRegionId(),
                    root -> root.join(AccExec_.regions, JoinType.LEFT).get(Region_.id)));
            }
        }
        return specification;
    }
}
