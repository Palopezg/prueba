package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Segmento;
import com.mycompany.myapp.service.SegmentoService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SegmentoCriteria;
import com.mycompany.myapp.service.SegmentoQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Segmento}.
 */
@RestController
@RequestMapping("/api")
public class SegmentoResource {

    private final Logger log = LoggerFactory.getLogger(SegmentoResource.class);

    private static final String ENTITY_NAME = "segmento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SegmentoService segmentoService;

    private final SegmentoQueryService segmentoQueryService;

    public SegmentoResource(SegmentoService segmentoService, SegmentoQueryService segmentoQueryService) {
        this.segmentoService = segmentoService;
        this.segmentoQueryService = segmentoQueryService;
    }

    /**
     * {@code POST  /segmentos} : Create a new segmento.
     *
     * @param segmento the segmento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new segmento, or with status {@code 400 (Bad Request)} if the segmento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/segmentos")
    public ResponseEntity<Segmento> createSegmento(@RequestBody Segmento segmento) throws URISyntaxException {
        log.debug("REST request to save Segmento : {}", segmento);
        if (segmento.getId() != null) {
            throw new BadRequestAlertException("A new segmento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Segmento result = segmentoService.save(segmento);
        return ResponseEntity.created(new URI("/api/segmentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /segmentos} : Updates an existing segmento.
     *
     * @param segmento the segmento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated segmento,
     * or with status {@code 400 (Bad Request)} if the segmento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the segmento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/segmentos")
    public ResponseEntity<Segmento> updateSegmento(@RequestBody Segmento segmento) throws URISyntaxException {
        log.debug("REST request to update Segmento : {}", segmento);
        if (segmento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Segmento result = segmentoService.save(segmento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, segmento.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /segmentos} : get all the segmentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of segmentos in body.
     */
    @GetMapping("/segmentos")
    public ResponseEntity<List<Segmento>> getAllSegmentos(SegmentoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Segmentos by criteria: {}", criteria);
        Page<Segmento> page = segmentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /segmentos/count} : count all the segmentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/segmentos/count")
    public ResponseEntity<Long> countSegmentos(SegmentoCriteria criteria) {
        log.debug("REST request to count Segmentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(segmentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /segmentos/:id} : get the "id" segmento.
     *
     * @param id the id of the segmento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the segmento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/segmentos/{id}")
    public ResponseEntity<Segmento> getSegmento(@PathVariable Long id) {
        log.debug("REST request to get Segmento : {}", id);
        Optional<Segmento> segmento = segmentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(segmento);
    }

    /**
     * {@code DELETE  /segmentos/:id} : delete the "id" segmento.
     *
     * @param id the id of the segmento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/segmentos/{id}")
    public ResponseEntity<Void> deleteSegmento(@PathVariable Long id) {
        log.debug("REST request to delete Segmento : {}", id);
        segmentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/segmentos?query=:query} : search for the segmento corresponding
     * to the query.
     *
     * @param query the query of the segmento search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/segmentos")
    public ResponseEntity<List<Segmento>> searchSegmentos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Segmentos for query {}", query);
        Page<Segmento> page = segmentoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
