package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AccountExecutive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AccountExecutive}.
 */
public interface AccountExecutiveService {

    /**
     * Save a accountExecutive.
     *
     * @param accountExecutive the entity to save.
     * @return the persisted entity.
     */
    AccountExecutive save(AccountExecutive accountExecutive);

    /**
     * Get all the accountExecutives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountExecutive> findAll(Pageable pageable);


    /**
     * Get the "id" accountExecutive.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountExecutive> findOne(Long id);

    /**
     * Delete the "id" accountExecutive.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accountExecutive corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountExecutive> search(String query, Pageable pageable);
}
