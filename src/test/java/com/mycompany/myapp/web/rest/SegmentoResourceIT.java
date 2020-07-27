package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestElasticSearchApp;
import com.mycompany.myapp.domain.Segmento;
import com.mycompany.myapp.domain.AccExec;
import com.mycompany.myapp.repository.SegmentoRepository;
import com.mycompany.myapp.repository.search.SegmentoSearchRepository;
import com.mycompany.myapp.service.SegmentoService;
import com.mycompany.myapp.service.dto.SegmentoCriteria;
import com.mycompany.myapp.service.SegmentoQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SegmentoResource} REST controller.
 */
@SpringBootTest(classes = TestElasticSearchApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SegmentoResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    @Autowired
    private SegmentoRepository segmentoRepository;

    @Autowired
    private SegmentoService segmentoService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.SegmentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private SegmentoSearchRepository mockSegmentoSearchRepository;

    @Autowired
    private SegmentoQueryService segmentoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSegmentoMockMvc;

    private Segmento segmento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Segmento createEntity(EntityManager em) {
        Segmento segmento = new Segmento()
            .descripcion(DEFAULT_DESCRIPCION)
            .valor(DEFAULT_VALOR);
        return segmento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Segmento createUpdatedEntity(EntityManager em) {
        Segmento segmento = new Segmento()
            .descripcion(UPDATED_DESCRIPCION)
            .valor(UPDATED_VALOR);
        return segmento;
    }

    @BeforeEach
    public void initTest() {
        segmento = createEntity(em);
    }

    @Test
    @Transactional
    public void createSegmento() throws Exception {
        int databaseSizeBeforeCreate = segmentoRepository.findAll().size();
        // Create the Segmento
        restSegmentoMockMvc.perform(post("/api/segmentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(segmento)))
            .andExpect(status().isCreated());

        // Validate the Segmento in the database
        List<Segmento> segmentoList = segmentoRepository.findAll();
        assertThat(segmentoList).hasSize(databaseSizeBeforeCreate + 1);
        Segmento testSegmento = segmentoList.get(segmentoList.size() - 1);
        assertThat(testSegmento.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testSegmento.getValor()).isEqualTo(DEFAULT_VALOR);

        // Validate the Segmento in Elasticsearch
        verify(mockSegmentoSearchRepository, times(1)).save(testSegmento);
    }

    @Test
    @Transactional
    public void createSegmentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = segmentoRepository.findAll().size();

        // Create the Segmento with an existing ID
        segmento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSegmentoMockMvc.perform(post("/api/segmentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(segmento)))
            .andExpect(status().isBadRequest());

        // Validate the Segmento in the database
        List<Segmento> segmentoList = segmentoRepository.findAll();
        assertThat(segmentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Segmento in Elasticsearch
        verify(mockSegmentoSearchRepository, times(0)).save(segmento);
    }


    @Test
    @Transactional
    public void getAllSegmentos() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList
        restSegmentoMockMvc.perform(get("/api/segmentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segmento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));
    }
    
    @Test
    @Transactional
    public void getSegmento() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get the segmento
        restSegmentoMockMvc.perform(get("/api/segmentos/{id}", segmento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(segmento.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR));
    }


    @Test
    @Transactional
    public void getSegmentosByIdFiltering() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        Long id = segmento.getId();

        defaultSegmentoShouldBeFound("id.equals=" + id);
        defaultSegmentoShouldNotBeFound("id.notEquals=" + id);

        defaultSegmentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSegmentoShouldNotBeFound("id.greaterThan=" + id);

        defaultSegmentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSegmentoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSegmentosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where descripcion equals to DEFAULT_DESCRIPCION
        defaultSegmentoShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the segmentoList where descripcion equals to UPDATED_DESCRIPCION
        defaultSegmentoShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllSegmentosByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultSegmentoShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the segmentoList where descripcion not equals to UPDATED_DESCRIPCION
        defaultSegmentoShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllSegmentosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultSegmentoShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the segmentoList where descripcion equals to UPDATED_DESCRIPCION
        defaultSegmentoShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllSegmentosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where descripcion is not null
        defaultSegmentoShouldBeFound("descripcion.specified=true");

        // Get all the segmentoList where descripcion is null
        defaultSegmentoShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllSegmentosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where descripcion contains DEFAULT_DESCRIPCION
        defaultSegmentoShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the segmentoList where descripcion contains UPDATED_DESCRIPCION
        defaultSegmentoShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllSegmentosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultSegmentoShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the segmentoList where descripcion does not contain UPDATED_DESCRIPCION
        defaultSegmentoShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }


    @Test
    @Transactional
    public void getAllSegmentosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where valor equals to DEFAULT_VALOR
        defaultSegmentoShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the segmentoList where valor equals to UPDATED_VALOR
        defaultSegmentoShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllSegmentosByValorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where valor not equals to DEFAULT_VALOR
        defaultSegmentoShouldNotBeFound("valor.notEquals=" + DEFAULT_VALOR);

        // Get all the segmentoList where valor not equals to UPDATED_VALOR
        defaultSegmentoShouldBeFound("valor.notEquals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllSegmentosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultSegmentoShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the segmentoList where valor equals to UPDATED_VALOR
        defaultSegmentoShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllSegmentosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where valor is not null
        defaultSegmentoShouldBeFound("valor.specified=true");

        // Get all the segmentoList where valor is null
        defaultSegmentoShouldNotBeFound("valor.specified=false");
    }
                @Test
    @Transactional
    public void getAllSegmentosByValorContainsSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where valor contains DEFAULT_VALOR
        defaultSegmentoShouldBeFound("valor.contains=" + DEFAULT_VALOR);

        // Get all the segmentoList where valor contains UPDATED_VALOR
        defaultSegmentoShouldNotBeFound("valor.contains=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllSegmentosByValorNotContainsSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);

        // Get all the segmentoList where valor does not contain DEFAULT_VALOR
        defaultSegmentoShouldNotBeFound("valor.doesNotContain=" + DEFAULT_VALOR);

        // Get all the segmentoList where valor does not contain UPDATED_VALOR
        defaultSegmentoShouldBeFound("valor.doesNotContain=" + UPDATED_VALOR);
    }


    @Test
    @Transactional
    public void getAllSegmentosByAccExecIsEqualToSomething() throws Exception {
        // Initialize the database
        segmentoRepository.saveAndFlush(segmento);
        AccExec accExec = AccExecResourceIT.createEntity(em);
        em.persist(accExec);
        em.flush();
        segmento.setAccExec(accExec);
        segmentoRepository.saveAndFlush(segmento);
        Long accExecId = accExec.getId();

        // Get all the segmentoList where accExec equals to accExecId
        defaultSegmentoShouldBeFound("accExecId.equals=" + accExecId);

        // Get all the segmentoList where accExec equals to accExecId + 1
        defaultSegmentoShouldNotBeFound("accExecId.equals=" + (accExecId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSegmentoShouldBeFound(String filter) throws Exception {
        restSegmentoMockMvc.perform(get("/api/segmentos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segmento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));

        // Check, that the count call also returns 1
        restSegmentoMockMvc.perform(get("/api/segmentos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSegmentoShouldNotBeFound(String filter) throws Exception {
        restSegmentoMockMvc.perform(get("/api/segmentos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSegmentoMockMvc.perform(get("/api/segmentos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSegmento() throws Exception {
        // Get the segmento
        restSegmentoMockMvc.perform(get("/api/segmentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSegmento() throws Exception {
        // Initialize the database
        segmentoService.save(segmento);

        int databaseSizeBeforeUpdate = segmentoRepository.findAll().size();

        // Update the segmento
        Segmento updatedSegmento = segmentoRepository.findById(segmento.getId()).get();
        // Disconnect from session so that the updates on updatedSegmento are not directly saved in db
        em.detach(updatedSegmento);
        updatedSegmento
            .descripcion(UPDATED_DESCRIPCION)
            .valor(UPDATED_VALOR);

        restSegmentoMockMvc.perform(put("/api/segmentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSegmento)))
            .andExpect(status().isOk());

        // Validate the Segmento in the database
        List<Segmento> segmentoList = segmentoRepository.findAll();
        assertThat(segmentoList).hasSize(databaseSizeBeforeUpdate);
        Segmento testSegmento = segmentoList.get(segmentoList.size() - 1);
        assertThat(testSegmento.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testSegmento.getValor()).isEqualTo(UPDATED_VALOR);

        // Validate the Segmento in Elasticsearch
        verify(mockSegmentoSearchRepository, times(2)).save(testSegmento);
    }

    @Test
    @Transactional
    public void updateNonExistingSegmento() throws Exception {
        int databaseSizeBeforeUpdate = segmentoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSegmentoMockMvc.perform(put("/api/segmentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(segmento)))
            .andExpect(status().isBadRequest());

        // Validate the Segmento in the database
        List<Segmento> segmentoList = segmentoRepository.findAll();
        assertThat(segmentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Segmento in Elasticsearch
        verify(mockSegmentoSearchRepository, times(0)).save(segmento);
    }

    @Test
    @Transactional
    public void deleteSegmento() throws Exception {
        // Initialize the database
        segmentoService.save(segmento);

        int databaseSizeBeforeDelete = segmentoRepository.findAll().size();

        // Delete the segmento
        restSegmentoMockMvc.perform(delete("/api/segmentos/{id}", segmento.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Segmento> segmentoList = segmentoRepository.findAll();
        assertThat(segmentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Segmento in Elasticsearch
        verify(mockSegmentoSearchRepository, times(1)).deleteById(segmento.getId());
    }

    @Test
    @Transactional
    public void searchSegmento() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        segmentoService.save(segmento);
        when(mockSegmentoSearchRepository.search(queryStringQuery("id:" + segmento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(segmento), PageRequest.of(0, 1), 1));

        // Search the segmento
        restSegmentoMockMvc.perform(get("/api/_search/segmentos?query=id:" + segmento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(segmento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));
    }
}
