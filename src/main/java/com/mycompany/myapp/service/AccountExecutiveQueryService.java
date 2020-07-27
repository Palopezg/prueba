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

import com.mycompany.myapp.domain.AccountExecutive;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.AccountExecutiveRepository;
import com.mycompany.myapp.repository.search.AccountExecutiveSearchRepository;
import com.mycompany.myapp.service.dto.AccountExecutiveCriteria;

/**
 * Service for executing complex queries for {@link AccountExecutive} entities in the database.
 * The main input is a {@link AccountExecutiveCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccountExecutive} or a {@link Page} of {@link AccountExecutive} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountExecutiveQueryService extends QueryService<AccountExecutive> {

    private final Logger log = LoggerFactory.getLogger(AccountExecutiveQueryService.class);

    private final AccountExecutiveRepository accountExecutiveRepository;

    private final AccountExecutiveSearchRepository accountExecutiveSearchRepository;

    public AccountExecutiveQueryService(AccountExecutiveRepository accountExecutiveRepository, AccountExecutiveSearchRepository accountExecutiveSearchRepository) {
        this.accountExecutiveRepository = accountExecutiveRepository;
        this.accountExecutiveSearchRepository = accountExecutiveSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AccountExecutive} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccountExecutive> findByCriteria(AccountExecutiveCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccountExecutive> specification = createSpecification(criteria);
        return accountExecutiveRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AccountExecutive} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountExecutive> findByCriteria(AccountExecutiveCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountExecutive> specification = createSpecification(criteria);
        return accountExecutiveRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountExecutiveCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccountExecutive> specification = createSpecification(criteria);
        return accountExecutiveRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountExecutiveCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountExecutive> createSpecification(AccountExecutiveCriteria criteria) {
        Specification<AccountExecutive> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccountExecutive_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), AccountExecutive_.nombre));
            }
            if (criteria.getApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellido(), AccountExecutive_.apellido));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), AccountExecutive_.telefono));
            }
            if (criteria.getCelular() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCelular(), AccountExecutive_.celular));
            }
            if (criteria.getMail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMail(), AccountExecutive_.mail));
            }
            if (criteria.getRepcom1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRepcom1(), AccountExecutive_.repcom1));
            }
            if (criteria.getRepcom2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRepcom2(), AccountExecutive_.repcom2));
            }
        }
        return specification;
    }
}
