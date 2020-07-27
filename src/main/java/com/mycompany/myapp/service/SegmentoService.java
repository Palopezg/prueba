package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Segmento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Segmento}.
 */
public interface SegmentoService {

    /**
     * Save a segmento.
     *
     * @param segmento the entity to save.
     * @return the persisted entity.
     */
    Segmento save(Segmento segmento);

    /**
     * Get all the segmentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Segmento> findAll(Pageable pageable);


    /**
     * Get the "id" segmento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Segmento> findOne(Long id);

    /**
     * Delete the "id" segmento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the segmento corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Segmento> search(String query, Pageable pageable);
}
