package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.SegmentoService;
import com.mycompany.myapp.domain.Segmento;
import com.mycompany.myapp.repository.SegmentoRepository;
import com.mycompany.myapp.repository.search.SegmentoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Segmento}.
 */
@Service
@Transactional
public class SegmentoServiceImpl implements SegmentoService {

    private final Logger log = LoggerFactory.getLogger(SegmentoServiceImpl.class);

    private final SegmentoRepository segmentoRepository;

    private final SegmentoSearchRepository segmentoSearchRepository;

    public SegmentoServiceImpl(SegmentoRepository segmentoRepository, SegmentoSearchRepository segmentoSearchRepository) {
        this.segmentoRepository = segmentoRepository;
        this.segmentoSearchRepository = segmentoSearchRepository;
    }

    @Override
    public Segmento save(Segmento segmento) {
        log.debug("Request to save Segmento : {}", segmento);
        Segmento result = segmentoRepository.save(segmento);
        segmentoSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Segmento> findAll(Pageable pageable) {
        log.debug("Request to get all Segmentos");
        return segmentoRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Segmento> findOne(Long id) {
        log.debug("Request to get Segmento : {}", id);
        return segmentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Segmento : {}", id);
        segmentoRepository.deleteById(id);
        segmentoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Segmento> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Segmentos for query {}", query);
        return segmentoSearchRepository.search(queryStringQuery(query), pageable);    }
}
