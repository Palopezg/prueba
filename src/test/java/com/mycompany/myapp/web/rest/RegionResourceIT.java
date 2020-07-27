package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestElasticSearchApp;
import com.mycompany.myapp.domain.Region;
import com.mycompany.myapp.repository.RegionRepository;
import com.mycompany.myapp.repository.search.RegionSearchRepository;
import com.mycompany.myapp.service.RegionService;
import com.mycompany.myapp.service.dto.RegionCriteria;
import com.mycompany.myapp.service.RegionQueryService;

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
 * Integration tests for the {@link RegionResource} REST controller.
 */
@SpringBootTest(classes = TestElasticSearchApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RegionResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionService regionService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.RegionSearchRepositoryMockConfiguration
     */
    @Autowired
    private RegionSearchRepository mockRegionSearchRepository;

    @Autowired
    private RegionQueryService regionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegionMockMvc;

    private Region region;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createEntity(EntityManager em) {
        Region region = new Region()
            .descripcion(DEFAULT_DESCRIPCION)
            .valor(DEFAULT_VALOR);
        return region;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createUpdatedEntity(EntityManager em) {
        Region region = new Region()
            .descripcion(UPDATED_DESCRIPCION)
            .valor(UPDATED_VALOR);
        return region;
    }

    @BeforeEach
    public void initTest() {
        region = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegion() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();
        // Create the Region
        restRegionMockMvc.perform(post("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(region)))
            .andExpect(status().isCreated());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate + 1);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testRegion.getValor()).isEqualTo(DEFAULT_VALOR);

        // Validate the Region in Elasticsearch
        verify(mockRegionSearchRepository, times(1)).save(testRegion);
    }

    @Test
    @Transactional
    public void createRegionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        // Create the Region with an existing ID
        region.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegionMockMvc.perform(post("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(region)))
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Region in Elasticsearch
        verify(mockRegionSearchRepository, times(0)).save(region);
    }


    @Test
    @Transactional
    public void getAllRegions() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList
        restRegionMockMvc.perform(get("/api/regions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));
    }
    
    @Test
    @Transactional
    public void getRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get the region
        restRegionMockMvc.perform(get("/api/regions/{id}", region.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(region.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR));
    }


    @Test
    @Transactional
    public void getRegionsByIdFiltering() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        Long id = region.getId();

        defaultRegionShouldBeFound("id.equals=" + id);
        defaultRegionShouldNotBeFound("id.notEquals=" + id);

        defaultRegionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegionShouldNotBeFound("id.greaterThan=" + id);

        defaultRegionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRegionsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where descripcion equals to DEFAULT_DESCRIPCION
        defaultRegionShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the regionList where descripcion equals to UPDATED_DESCRIPCION
        defaultRegionShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllRegionsByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultRegionShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the regionList where descripcion not equals to UPDATED_DESCRIPCION
        defaultRegionShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllRegionsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultRegionShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the regionList where descripcion equals to UPDATED_DESCRIPCION
        defaultRegionShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllRegionsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where descripcion is not null
        defaultRegionShouldBeFound("descripcion.specified=true");

        // Get all the regionList where descripcion is null
        defaultRegionShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegionsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where descripcion contains DEFAULT_DESCRIPCION
        defaultRegionShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the regionList where descripcion contains UPDATED_DESCRIPCION
        defaultRegionShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllRegionsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultRegionShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the regionList where descripcion does not contain UPDATED_DESCRIPCION
        defaultRegionShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }


    @Test
    @Transactional
    public void getAllRegionsByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where valor equals to DEFAULT_VALOR
        defaultRegionShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the regionList where valor equals to UPDATED_VALOR
        defaultRegionShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllRegionsByValorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where valor not equals to DEFAULT_VALOR
        defaultRegionShouldNotBeFound("valor.notEquals=" + DEFAULT_VALOR);

        // Get all the regionList where valor not equals to UPDATED_VALOR
        defaultRegionShouldBeFound("valor.notEquals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllRegionsByValorIsInShouldWork() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultRegionShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the regionList where valor equals to UPDATED_VALOR
        defaultRegionShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllRegionsByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where valor is not null
        defaultRegionShouldBeFound("valor.specified=true");

        // Get all the regionList where valor is null
        defaultRegionShouldNotBeFound("valor.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegionsByValorContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where valor contains DEFAULT_VALOR
        defaultRegionShouldBeFound("valor.contains=" + DEFAULT_VALOR);

        // Get all the regionList where valor contains UPDATED_VALOR
        defaultRegionShouldNotBeFound("valor.contains=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllRegionsByValorNotContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where valor does not contain DEFAULT_VALOR
        defaultRegionShouldNotBeFound("valor.doesNotContain=" + DEFAULT_VALOR);

        // Get all the regionList where valor does not contain UPDATED_VALOR
        defaultRegionShouldBeFound("valor.doesNotContain=" + UPDATED_VALOR);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegionShouldBeFound(String filter) throws Exception {
        restRegionMockMvc.perform(get("/api/regions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));

        // Check, that the count call also returns 1
        restRegionMockMvc.perform(get("/api/regions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegionShouldNotBeFound(String filter) throws Exception {
        restRegionMockMvc.perform(get("/api/regions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegionMockMvc.perform(get("/api/regions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRegion() throws Exception {
        // Get the region
        restRegionMockMvc.perform(get("/api/regions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegion() throws Exception {
        // Initialize the database
        regionService.save(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region
        Region updatedRegion = regionRepository.findById(region.getId()).get();
        // Disconnect from session so that the updates on updatedRegion are not directly saved in db
        em.detach(updatedRegion);
        updatedRegion
            .descripcion(UPDATED_DESCRIPCION)
            .valor(UPDATED_VALOR);

        restRegionMockMvc.perform(put("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegion)))
            .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testRegion.getValor()).isEqualTo(UPDATED_VALOR);

        // Validate the Region in Elasticsearch
        verify(mockRegionSearchRepository, times(2)).save(testRegion);
    }

    @Test
    @Transactional
    public void updateNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionMockMvc.perform(put("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(region)))
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Region in Elasticsearch
        verify(mockRegionSearchRepository, times(0)).save(region);
    }

    @Test
    @Transactional
    public void deleteRegion() throws Exception {
        // Initialize the database
        regionService.save(region);

        int databaseSizeBeforeDelete = regionRepository.findAll().size();

        // Delete the region
        restRegionMockMvc.perform(delete("/api/regions/{id}", region.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Region in Elasticsearch
        verify(mockRegionSearchRepository, times(1)).deleteById(region.getId());
    }

    @Test
    @Transactional
    public void searchRegion() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        regionService.save(region);
        when(mockRegionSearchRepository.search(queryStringQuery("id:" + region.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(region), PageRequest.of(0, 1), 1));

        // Search the region
        restRegionMockMvc.perform(get("/api/_search/regions?query=id:" + region.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)));
    }
}
