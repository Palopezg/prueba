package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Region;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Region}.
 */
public interface RegionService {

    /**
     * Save a region.
     *
     * @param region the entity to save.
     * @return the persisted entity.
     */
    Region save(Region region);

    /**
     * Get all the regions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Region> findAll(Pageable pageable);


    /**
     * Get the "id" region.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Region> findOne(Long id);

    /**
     * Delete the "id" region.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the region corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Region> search(String query, Pageable pageable);
}
