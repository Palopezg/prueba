package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestElasticSearchApp;
import com.mycompany.myapp.domain.AccExec;
import com.mycompany.myapp.domain.Segmento;
import com.mycompany.myapp.domain.Region;
import com.mycompany.myapp.repository.AccExecRepository;
import com.mycompany.myapp.repository.search.AccExecSearchRepository;
import com.mycompany.myapp.service.AccExecService;
import com.mycompany.myapp.service.dto.AccExecCriteria;
import com.mycompany.myapp.service.AccExecQueryService;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AccExecResource} REST controller.
 */
@SpringBootTest(classes = TestElasticSearchApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AccExecResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_REPCOM_1 = "AAAAAAAAAA";
    private static final String UPDATED_REPCOM_1 = "BBBBBBBBBB";

    private static final String DEFAULT_REPCOM_2 = "AAAAAAAAAA";
    private static final String UPDATED_REPCOM_2 = "BBBBBBBBBB";

    @Autowired
    private AccExecRepository accExecRepository;

    @Mock
    private AccExecRepository accExecRepositoryMock;

    @Mock
    private AccExecService accExecServiceMock;

    @Autowired
    private AccExecService accExecService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.AccExecSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccExecSearchRepository mockAccExecSearchRepository;

    @Autowired
    private AccExecQueryService accExecQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccExecMockMvc;

    private AccExec accExec;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccExec createEntity(EntityManager em) {
        AccExec accExec = new AccExec()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .telefono(DEFAULT_TELEFONO)
            .celular(DEFAULT_CELULAR)
            .mail(DEFAULT_MAIL)
            .repcom1(DEFAULT_REPCOM_1)
            .repcom2(DEFAULT_REPCOM_2);
        return accExec;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccExec createUpdatedEntity(EntityManager em) {
        AccExec accExec = new AccExec()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .telefono(UPDATED_TELEFONO)
            .celular(UPDATED_CELULAR)
            .mail(UPDATED_MAIL)
            .repcom1(UPDATED_REPCOM_1)
            .repcom2(UPDATED_REPCOM_2);
        return accExec;
    }

    @BeforeEach
    public void initTest() {
        accExec = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccExec() throws Exception {
        int databaseSizeBeforeCreate = accExecRepository.findAll().size();
        // Create the AccExec
        restAccExecMockMvc.perform(post("/api/acc-execs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(accExec)))
            .andExpect(status().isCreated());

        // Validate the AccExec in the database
        List<AccExec> accExecList = accExecRepository.findAll();
        assertThat(accExecList).hasSize(databaseSizeBeforeCreate + 1);
        AccExec testAccExec = accExecList.get(accExecList.size() - 1);
        assertThat(testAccExec.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAccExec.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testAccExec.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testAccExec.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testAccExec.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testAccExec.getRepcom1()).isEqualTo(DEFAULT_REPCOM_1);
        assertThat(testAccExec.getRepcom2()).isEqualTo(DEFAULT_REPCOM_2);

        // Validate the AccExec in Elasticsearch
        verify(mockAccExecSearchRepository, times(1)).save(testAccExec);
    }

    @Test
    @Transactional
    public void createAccExecWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accExecRepository.findAll().size();

        // Create the AccExec with an existing ID
        accExec.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccExecMockMvc.perform(post("/api/acc-execs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(accExec)))
            .andExpect(status().isBadRequest());

        // Validate the AccExec in the database
        List<AccExec> accExecList = accExecRepository.findAll();
        assertThat(accExecList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccExec in Elasticsearch
        verify(mockAccExecSearchRepository, times(0)).save(accExec);
    }


    @Test
    @Transactional
    public void getAllAccExecs() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList
        restAccExecMockMvc.perform(get("/api/acc-execs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accExec.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].repcom1").value(hasItem(DEFAULT_REPCOM_1)))
            .andExpect(jsonPath("$.[*].repcom2").value(hasItem(DEFAULT_REPCOM_2)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllAccExecsWithEagerRelationshipsIsEnabled() throws Exception {
        when(accExecServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAccExecMockMvc.perform(get("/api/acc-execs?eagerload=true"))
            .andExpect(status().isOk());

        verify(accExecServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllAccExecsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(accExecServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAccExecMockMvc.perform(get("/api/acc-execs?eagerload=true"))
            .andExpect(status().isOk());

        verify(accExecServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getAccExec() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get the accExec
        restAccExecMockMvc.perform(get("/api/acc-execs/{id}", accExec.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accExec.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.repcom1").value(DEFAULT_REPCOM_1))
            .andExpect(jsonPath("$.repcom2").value(DEFAULT_REPCOM_2));
    }


    @Test
    @Transactional
    public void getAccExecsByIdFiltering() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        Long id = accExec.getId();

        defaultAccExecShouldBeFound("id.equals=" + id);
        defaultAccExecShouldNotBeFound("id.notEquals=" + id);

        defaultAccExecShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccExecShouldNotBeFound("id.greaterThan=" + id);

        defaultAccExecShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccExecShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAccExecsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where nombre equals to DEFAULT_NOMBRE
        defaultAccExecShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the accExecList where nombre equals to UPDATED_NOMBRE
        defaultAccExecShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAccExecsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where nombre not equals to DEFAULT_NOMBRE
        defaultAccExecShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the accExecList where nombre not equals to UPDATED_NOMBRE
        defaultAccExecShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAccExecsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultAccExecShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the accExecList where nombre equals to UPDATED_NOMBRE
        defaultAccExecShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAccExecsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where nombre is not null
        defaultAccExecShouldBeFound("nombre.specified=true");

        // Get all the accExecList where nombre is null
        defaultAccExecShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccExecsByNombreContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where nombre contains DEFAULT_NOMBRE
        defaultAccExecShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the accExecList where nombre contains UPDATED_NOMBRE
        defaultAccExecShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAccExecsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where nombre does not contain DEFAULT_NOMBRE
        defaultAccExecShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the accExecList where nombre does not contain UPDATED_NOMBRE
        defaultAccExecShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllAccExecsByApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where apellido equals to DEFAULT_APELLIDO
        defaultAccExecShouldBeFound("apellido.equals=" + DEFAULT_APELLIDO);

        // Get all the accExecList where apellido equals to UPDATED_APELLIDO
        defaultAccExecShouldNotBeFound("apellido.equals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllAccExecsByApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where apellido not equals to DEFAULT_APELLIDO
        defaultAccExecShouldNotBeFound("apellido.notEquals=" + DEFAULT_APELLIDO);

        // Get all the accExecList where apellido not equals to UPDATED_APELLIDO
        defaultAccExecShouldBeFound("apellido.notEquals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllAccExecsByApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where apellido in DEFAULT_APELLIDO or UPDATED_APELLIDO
        defaultAccExecShouldBeFound("apellido.in=" + DEFAULT_APELLIDO + "," + UPDATED_APELLIDO);

        // Get all the accExecList where apellido equals to UPDATED_APELLIDO
        defaultAccExecShouldNotBeFound("apellido.in=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllAccExecsByApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where apellido is not null
        defaultAccExecShouldBeFound("apellido.specified=true");

        // Get all the accExecList where apellido is null
        defaultAccExecShouldNotBeFound("apellido.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccExecsByApellidoContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where apellido contains DEFAULT_APELLIDO
        defaultAccExecShouldBeFound("apellido.contains=" + DEFAULT_APELLIDO);

        // Get all the accExecList where apellido contains UPDATED_APELLIDO
        defaultAccExecShouldNotBeFound("apellido.contains=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllAccExecsByApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where apellido does not contain DEFAULT_APELLIDO
        defaultAccExecShouldNotBeFound("apellido.doesNotContain=" + DEFAULT_APELLIDO);

        // Get all the accExecList where apellido does not contain UPDATED_APELLIDO
        defaultAccExecShouldBeFound("apellido.doesNotContain=" + UPDATED_APELLIDO);
    }


    @Test
    @Transactional
    public void getAllAccExecsByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where telefono equals to DEFAULT_TELEFONO
        defaultAccExecShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the accExecList where telefono equals to UPDATED_TELEFONO
        defaultAccExecShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllAccExecsByTelefonoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where telefono not equals to DEFAULT_TELEFONO
        defaultAccExecShouldNotBeFound("telefono.notEquals=" + DEFAULT_TELEFONO);

        // Get all the accExecList where telefono not equals to UPDATED_TELEFONO
        defaultAccExecShouldBeFound("telefono.notEquals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllAccExecsByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultAccExecShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the accExecList where telefono equals to UPDATED_TELEFONO
        defaultAccExecShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllAccExecsByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where telefono is not null
        defaultAccExecShouldBeFound("telefono.specified=true");

        // Get all the accExecList where telefono is null
        defaultAccExecShouldNotBeFound("telefono.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccExecsByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where telefono contains DEFAULT_TELEFONO
        defaultAccExecShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the accExecList where telefono contains UPDATED_TELEFONO
        defaultAccExecShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllAccExecsByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where telefono does not contain DEFAULT_TELEFONO
        defaultAccExecShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the accExecList where telefono does not contain UPDATED_TELEFONO
        defaultAccExecShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }


    @Test
    @Transactional
    public void getAllAccExecsByCelularIsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where celular equals to DEFAULT_CELULAR
        defaultAccExecShouldBeFound("celular.equals=" + DEFAULT_CELULAR);

        // Get all the accExecList where celular equals to UPDATED_CELULAR
        defaultAccExecShouldNotBeFound("celular.equals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllAccExecsByCelularIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where celular not equals to DEFAULT_CELULAR
        defaultAccExecShouldNotBeFound("celular.notEquals=" + DEFAULT_CELULAR);

        // Get all the accExecList where celular not equals to UPDATED_CELULAR
        defaultAccExecShouldBeFound("celular.notEquals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllAccExecsByCelularIsInShouldWork() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where celular in DEFAULT_CELULAR or UPDATED_CELULAR
        defaultAccExecShouldBeFound("celular.in=" + DEFAULT_CELULAR + "," + UPDATED_CELULAR);

        // Get all the accExecList where celular equals to UPDATED_CELULAR
        defaultAccExecShouldNotBeFound("celular.in=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllAccExecsByCelularIsNullOrNotNull() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where celular is not null
        defaultAccExecShouldBeFound("celular.specified=true");

        // Get all the accExecList where celular is null
        defaultAccExecShouldNotBeFound("celular.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccExecsByCelularContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where celular contains DEFAULT_CELULAR
        defaultAccExecShouldBeFound("celular.contains=" + DEFAULT_CELULAR);

        // Get all the accExecList where celular contains UPDATED_CELULAR
        defaultAccExecShouldNotBeFound("celular.contains=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllAccExecsByCelularNotContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where celular does not contain DEFAULT_CELULAR
        defaultAccExecShouldNotBeFound("celular.doesNotContain=" + DEFAULT_CELULAR);

        // Get all the accExecList where celular does not contain UPDATED_CELULAR
        defaultAccExecShouldBeFound("celular.doesNotContain=" + UPDATED_CELULAR);
    }


    @Test
    @Transactional
    public void getAllAccExecsByMailIsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where mail equals to DEFAULT_MAIL
        defaultAccExecShouldBeFound("mail.equals=" + DEFAULT_MAIL);

        // Get all the accExecList where mail equals to UPDATED_MAIL
        defaultAccExecShouldNotBeFound("mail.equals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void getAllAccExecsByMailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where mail not equals to DEFAULT_MAIL
        defaultAccExecShouldNotBeFound("mail.notEquals=" + DEFAULT_MAIL);

        // Get all the accExecList where mail not equals to UPDATED_MAIL
        defaultAccExecShouldBeFound("mail.notEquals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void getAllAccExecsByMailIsInShouldWork() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where mail in DEFAULT_MAIL or UPDATED_MAIL
        defaultAccExecShouldBeFound("mail.in=" + DEFAULT_MAIL + "," + UPDATED_MAIL);

        // Get all the accExecList where mail equals to UPDATED_MAIL
        defaultAccExecShouldNotBeFound("mail.in=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void getAllAccExecsByMailIsNullOrNotNull() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where mail is not null
        defaultAccExecShouldBeFound("mail.specified=true");

        // Get all the accExecList where mail is null
        defaultAccExecShouldNotBeFound("mail.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccExecsByMailContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where mail contains DEFAULT_MAIL
        defaultAccExecShouldBeFound("mail.contains=" + DEFAULT_MAIL);

        // Get all the accExecList where mail contains UPDATED_MAIL
        defaultAccExecShouldNotBeFound("mail.contains=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void getAllAccExecsByMailNotContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where mail does not contain DEFAULT_MAIL
        defaultAccExecShouldNotBeFound("mail.doesNotContain=" + DEFAULT_MAIL);

        // Get all the accExecList where mail does not contain UPDATED_MAIL
        defaultAccExecShouldBeFound("mail.doesNotContain=" + UPDATED_MAIL);
    }


    @Test
    @Transactional
    public void getAllAccExecsByRepcom1IsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom1 equals to DEFAULT_REPCOM_1
        defaultAccExecShouldBeFound("repcom1.equals=" + DEFAULT_REPCOM_1);

        // Get all the accExecList where repcom1 equals to UPDATED_REPCOM_1
        defaultAccExecShouldNotBeFound("repcom1.equals=" + UPDATED_REPCOM_1);
    }

    @Test
    @Transactional
    public void getAllAccExecsByRepcom1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom1 not equals to DEFAULT_REPCOM_1
        defaultAccExecShouldNotBeFound("repcom1.notEquals=" + DEFAULT_REPCOM_1);

        // Get all the accExecList where repcom1 not equals to UPDATED_REPCOM_1
        defaultAccExecShouldBeFound("repcom1.notEquals=" + UPDATED_REPCOM_1);
    }

    @Test
    @Transactional
    public void getAllAccExecsByRepcom1IsInShouldWork() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom1 in DEFAULT_REPCOM_1 or UPDATED_REPCOM_1
        defaultAccExecShouldBeFound("repcom1.in=" + DEFAULT_REPCOM_1 + "," + UPDATED_REPCOM_1);

        // Get all the accExecList where repcom1 equals to UPDATED_REPCOM_1
        defaultAccExecShouldNotBeFound("repcom1.in=" + UPDATED_REPCOM_1);
    }

    @Test
    @Transactional
    public void getAllAccExecsByRepcom1IsNullOrNotNull() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom1 is not null
        defaultAccExecShouldBeFound("repcom1.specified=true");

        // Get all the accExecList where repcom1 is null
        defaultAccExecShouldNotBeFound("repcom1.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccExecsByRepcom1ContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom1 contains DEFAULT_REPCOM_1
        defaultAccExecShouldBeFound("repcom1.contains=" + DEFAULT_REPCOM_1);

        // Get all the accExecList where repcom1 contains UPDATED_REPCOM_1
        defaultAccExecShouldNotBeFound("repcom1.contains=" + UPDATED_REPCOM_1);
    }

    @Test
    @Transactional
    public void getAllAccExecsByRepcom1NotContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom1 does not contain DEFAULT_REPCOM_1
        defaultAccExecShouldNotBeFound("repcom1.doesNotContain=" + DEFAULT_REPCOM_1);

        // Get all the accExecList where repcom1 does not contain UPDATED_REPCOM_1
        defaultAccExecShouldBeFound("repcom1.doesNotContain=" + UPDATED_REPCOM_1);
    }


    @Test
    @Transactional
    public void getAllAccExecsByRepcom2IsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom2 equals to DEFAULT_REPCOM_2
        defaultAccExecShouldBeFound("repcom2.equals=" + DEFAULT_REPCOM_2);

        // Get all the accExecList where repcom2 equals to UPDATED_REPCOM_2
        defaultAccExecShouldNotBeFound("repcom2.equals=" + UPDATED_REPCOM_2);
    }

    @Test
    @Transactional
    public void getAllAccExecsByRepcom2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom2 not equals to DEFAULT_REPCOM_2
        defaultAccExecShouldNotBeFound("repcom2.notEquals=" + DEFAULT_REPCOM_2);

        // Get all the accExecList where repcom2 not equals to UPDATED_REPCOM_2
        defaultAccExecShouldBeFound("repcom2.notEquals=" + UPDATED_REPCOM_2);
    }

    @Test
    @Transactional
    public void getAllAccExecsByRepcom2IsInShouldWork() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom2 in DEFAULT_REPCOM_2 or UPDATED_REPCOM_2
        defaultAccExecShouldBeFound("repcom2.in=" + DEFAULT_REPCOM_2 + "," + UPDATED_REPCOM_2);

        // Get all the accExecList where repcom2 equals to UPDATED_REPCOM_2
        defaultAccExecShouldNotBeFound("repcom2.in=" + UPDATED_REPCOM_2);
    }

    @Test
    @Transactional
    public void getAllAccExecsByRepcom2IsNullOrNotNull() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom2 is not null
        defaultAccExecShouldBeFound("repcom2.specified=true");

        // Get all the accExecList where repcom2 is null
        defaultAccExecShouldNotBeFound("repcom2.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccExecsByRepcom2ContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom2 contains DEFAULT_REPCOM_2
        defaultAccExecShouldBeFound("repcom2.contains=" + DEFAULT_REPCOM_2);

        // Get all the accExecList where repcom2 contains UPDATED_REPCOM_2
        defaultAccExecShouldNotBeFound("repcom2.contains=" + UPDATED_REPCOM_2);
    }

    @Test
    @Transactional
    public void getAllAccExecsByRepcom2NotContainsSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);

        // Get all the accExecList where repcom2 does not contain DEFAULT_REPCOM_2
        defaultAccExecShouldNotBeFound("repcom2.doesNotContain=" + DEFAULT_REPCOM_2);

        // Get all the accExecList where repcom2 does not contain UPDATED_REPCOM_2
        defaultAccExecShouldBeFound("repcom2.doesNotContain=" + UPDATED_REPCOM_2);
    }


    @Test
    @Transactional
    public void getAllAccExecsBySegmentoIsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);
        Segmento segmento = SegmentoResourceIT.createEntity(em);
        em.persist(segmento);
        em.flush();
        accExec.addSegmento(segmento);
        accExecRepository.saveAndFlush(accExec);
        Long segmentoId = segmento.getId();

        // Get all the accExecList where segmento equals to segmentoId
        defaultAccExecShouldBeFound("segmentoId.equals=" + segmentoId);

        // Get all the accExecList where segmento equals to segmentoId + 1
        defaultAccExecShouldNotBeFound("segmentoId.equals=" + (segmentoId + 1));
    }


    @Test
    @Transactional
    public void getAllAccExecsByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        accExecRepository.saveAndFlush(accExec);
        Region region = RegionResourceIT.createEntity(em);
        em.persist(region);
        em.flush();
        accExec.addRegion(region);
        accExecRepository.saveAndFlush(accExec);
        Long regionId = region.getId();

        // Get all the accExecList where region equals to regionId
        defaultAccExecShouldBeFound("regionId.equals=" + regionId);

        // Get all the accExecList where region equals to regionId + 1
        defaultAccExecShouldNotBeFound("regionId.equals=" + (regionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccExecShouldBeFound(String filter) throws Exception {
        restAccExecMockMvc.perform(get("/api/acc-execs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accExec.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].repcom1").value(hasItem(DEFAULT_REPCOM_1)))
            .andExpect(jsonPath("$.[*].repcom2").value(hasItem(DEFAULT_REPCOM_2)));

        // Check, that the count call also returns 1
        restAccExecMockMvc.perform(get("/api/acc-execs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccExecShouldNotBeFound(String filter) throws Exception {
        restAccExecMockMvc.perform(get("/api/acc-execs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccExecMockMvc.perform(get("/api/acc-execs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAccExec() throws Exception {
        // Get the accExec
        restAccExecMockMvc.perform(get("/api/acc-execs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccExec() throws Exception {
        // Initialize the database
        accExecService.save(accExec);

        int databaseSizeBeforeUpdate = accExecRepository.findAll().size();

        // Update the accExec
        AccExec updatedAccExec = accExecRepository.findById(accExec.getId()).get();
        // Disconnect from session so that the updates on updatedAccExec are not directly saved in db
        em.detach(updatedAccExec);
        updatedAccExec
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .telefono(UPDATED_TELEFONO)
            .celular(UPDATED_CELULAR)
            .mail(UPDATED_MAIL)
            .repcom1(UPDATED_REPCOM_1)
            .repcom2(UPDATED_REPCOM_2);

        restAccExecMockMvc.perform(put("/api/acc-execs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccExec)))
            .andExpect(status().isOk());

        // Validate the AccExec in the database
        List<AccExec> accExecList = accExecRepository.findAll();
        assertThat(accExecList).hasSize(databaseSizeBeforeUpdate);
        AccExec testAccExec = accExecList.get(accExecList.size() - 1);
        assertThat(testAccExec.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAccExec.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testAccExec.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testAccExec.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testAccExec.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testAccExec.getRepcom1()).isEqualTo(UPDATED_REPCOM_1);
        assertThat(testAccExec.getRepcom2()).isEqualTo(UPDATED_REPCOM_2);

        // Validate the AccExec in Elasticsearch
        verify(mockAccExecSearchRepository, times(2)).save(testAccExec);
    }

    @Test
    @Transactional
    public void updateNonExistingAccExec() throws Exception {
        int databaseSizeBeforeUpdate = accExecRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccExecMockMvc.perform(put("/api/acc-execs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(accExec)))
            .andExpect(status().isBadRequest());

        // Validate the AccExec in the database
        List<AccExec> accExecList = accExecRepository.findAll();
        assertThat(accExecList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccExec in Elasticsearch
        verify(mockAccExecSearchRepository, times(0)).save(accExec);
    }

    @Test
    @Transactional
    public void deleteAccExec() throws Exception {
        // Initialize the database
        accExecService.save(accExec);

        int databaseSizeBeforeDelete = accExecRepository.findAll().size();

        // Delete the accExec
        restAccExecMockMvc.perform(delete("/api/acc-execs/{id}", accExec.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccExec> accExecList = accExecRepository.findAll();
        assertThat(accExecList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccExec in Elasticsearch
        verify(mockAccExecSearchRepository, times(1)).deleteById(accExec.getId());
    }

    @Test
    @Transactional
    public void searchAccExec() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accExecService.save(accExec);
        when(mockAccExecSearchRepository.search(queryStringQuery("id:" + accExec.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accExec), PageRequest.of(0, 1), 1));

        // Search the accExec
        restAccExecMockMvc.perform(get("/api/_search/acc-execs?query=id:" + accExec.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accExec.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].repcom1").value(hasItem(DEFAULT_REPCOM_1)))
            .andExpect(jsonPath("$.[*].repcom2").value(hasItem(DEFAULT_REPCOM_2)));
    }
}
