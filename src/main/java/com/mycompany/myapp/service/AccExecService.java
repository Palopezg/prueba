package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AccExec;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AccExec}.
 */
public interface AccExecService {

    /**
     * Save a accExec.
     *
     * @param accExec the entity to save.
     * @return the persisted entity.
     */
    AccExec save(AccExec accExec);

    /**
     * Get all the accExecs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccExec> findAll(Pageable pageable);

    /**
     * Get all the accExecs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<AccExec> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" accExec.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccExec> findOne(Long id);

    /**
     * Delete the "id" accExec.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accExec corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccExec> search(String query, Pageable pageable);
}
