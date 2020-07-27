package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AccExec;
import com.mycompany.myapp.service.AccExecService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.AccExecCriteria;
import com.mycompany.myapp.service.AccExecQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.AccExec}.
 */
@RestController
@RequestMapping("/api")
public class AccExecResource {

    private final Logger log = LoggerFactory.getLogger(AccExecResource.class);

    private static final String ENTITY_NAME = "accExec";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccExecService accExecService;

    private final AccExecQueryService accExecQueryService;

    public AccExecResource(AccExecService accExecService, AccExecQueryService accExecQueryService) {
        this.accExecService = accExecService;
        this.accExecQueryService = accExecQueryService;
    }

    /**
     * {@code POST  /acc-execs} : Create a new accExec.
     *
     * @param accExec the accExec to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accExec, or with status {@code 400 (Bad Request)} if the accExec has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acc-execs")
    public ResponseEntity<AccExec> createAccExec(@RequestBody AccExec accExec) throws URISyntaxException {
        log.debug("REST request to save AccExec : {}", accExec);
        if (accExec.getId() != null) {
            throw new BadRequestAlertException("A new accExec cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccExec result = accExecService.save(accExec);
        return ResponseEntity.created(new URI("/api/acc-execs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acc-execs} : Updates an existing accExec.
     *
     * @param accExec the accExec to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accExec,
     * or with status {@code 400 (Bad Request)} if the accExec is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accExec couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acc-execs")
    public ResponseEntity<AccExec> updateAccExec(@RequestBody AccExec accExec) throws URISyntaxException {
        log.debug("REST request to update AccExec : {}", accExec);
        if (accExec.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccExec result = accExecService.save(accExec);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accExec.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /acc-execs} : get all the accExecs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accExecs in body.
     */
    @GetMapping("/acc-execs")
    public ResponseEntity<List<AccExec>> getAllAccExecs(AccExecCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AccExecs by criteria: {}", criteria);
        Page<AccExec> page = accExecQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acc-execs/count} : count all the accExecs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/acc-execs/count")
    public ResponseEntity<Long> countAccExecs(AccExecCriteria criteria) {
        log.debug("REST request to count AccExecs by criteria: {}", criteria);
        return ResponseEntity.ok().body(accExecQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /acc-execs/:id} : get the "id" accExec.
     *
     * @param id the id of the accExec to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accExec, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acc-execs/{id}")
    public ResponseEntity<AccExec> getAccExec(@PathVariable Long id) {
        log.debug("REST request to get AccExec : {}", id);
        Optional<AccExec> accExec = accExecService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accExec);
    }

    /**
     * {@code DELETE  /acc-execs/:id} : delete the "id" accExec.
     *
     * @param id the id of the accExec to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acc-execs/{id}")
    public ResponseEntity<Void> deleteAccExec(@PathVariable Long id) {
        log.debug("REST request to delete AccExec : {}", id);
        accExecService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/acc-execs?query=:query} : search for the accExec corresponding
     * to the query.
     *
     * @param query the query of the accExec search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/acc-execs")
    public ResponseEntity<List<AccExec>> searchAccExecs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccExecs for query {}", query);
        Page<AccExec> page = accExecService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
