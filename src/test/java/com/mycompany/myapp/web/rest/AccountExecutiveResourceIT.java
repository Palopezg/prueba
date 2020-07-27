package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestElasticSearchApp;
import com.mycompany.myapp.domain.AccountExecutive;
import com.mycompany.myapp.repository.AccountExecutiveRepository;
import com.mycompany.myapp.repository.search.AccountExecutiveSearchRepository;
import com.mycompany.myapp.service.AccountExecutiveService;
import com.mycompany.myapp.service.dto.AccountExecutiveCriteria;
import com.mycompany.myapp.service.AccountExecutiveQueryService;

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
 * Integration tests for the {@link AccountExecutiveResource} REST controller.
 */
@SpringBootTest(classes = TestElasticSearchApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AccountExecutiveResourceIT {

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
    private AccountExecutiveRepository accountExecutiveRepository;

    @Autowired
    private AccountExecutiveService accountExecutiveService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.AccountExecutiveSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountExecutiveSearchRepository mockAccountExecutiveSearchRepository;

    @Autowired
    private AccountExecutiveQueryService accountExecutiveQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountExecutiveMockMvc;

    private AccountExecutive accountExecutive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountExecutive createEntity(EntityManager em) {
        AccountExecutive accountExecutive = new AccountExecutive()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .telefono(DEFAULT_TELEFONO)
            .celular(DEFAULT_CELULAR)
            .mail(DEFAULT_MAIL)
            .repcom1(DEFAULT_REPCOM_1)
            .repcom2(DEFAULT_REPCOM_2);
        return accountExecutive;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountExecutive createUpdatedEntity(EntityManager em) {
        AccountExecutive accountExecutive = new AccountExecutive()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .telefono(UPDATED_TELEFONO)
            .celular(UPDATED_CELULAR)
            .mail(UPDATED_MAIL)
            .repcom1(UPDATED_REPCOM_1)
            .repcom2(UPDATED_REPCOM_2);
        return accountExecutive;
    }

    @BeforeEach
    public void initTest() {
        accountExecutive = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountExecutive() throws Exception {
        int databaseSizeBeforeCreate = accountExecutiveRepository.findAll().size();
        // Create the AccountExecutive
        restAccountExecutiveMockMvc.perform(post("/api/account-executives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(accountExecutive)))
            .andExpect(status().isCreated());

        // Validate the AccountExecutive in the database
        List<AccountExecutive> accountExecutiveList = accountExecutiveRepository.findAll();
        assertThat(accountExecutiveList).hasSize(databaseSizeBeforeCreate + 1);
        AccountExecutive testAccountExecutive = accountExecutiveList.get(accountExecutiveList.size() - 1);
        assertThat(testAccountExecutive.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAccountExecutive.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testAccountExecutive.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testAccountExecutive.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testAccountExecutive.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testAccountExecutive.getRepcom1()).isEqualTo(DEFAULT_REPCOM_1);
        assertThat(testAccountExecutive.getRepcom2()).isEqualTo(DEFAULT_REPCOM_2);

        // Validate the AccountExecutive in Elasticsearch
        verify(mockAccountExecutiveSearchRepository, times(1)).save(testAccountExecutive);
    }

    @Test
    @Transactional
    public void createAccountExecutiveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountExecutiveRepository.findAll().size();

        // Create the AccountExecutive with an existing ID
        accountExecutive.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountExecutiveMockMvc.perform(post("/api/account-executives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(accountExecutive)))
            .andExpect(status().isBadRequest());

        // Validate the AccountExecutive in the database
        List<AccountExecutive> accountExecutiveList = accountExecutiveRepository.findAll();
        assertThat(accountExecutiveList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccountExecutive in Elasticsearch
        verify(mockAccountExecutiveSearchRepository, times(0)).save(accountExecutive);
    }


    @Test
    @Transactional
    public void getAllAccountExecutives() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList
        restAccountExecutiveMockMvc.perform(get("/api/account-executives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountExecutive.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].repcom1").value(hasItem(DEFAULT_REPCOM_1)))
            .andExpect(jsonPath("$.[*].repcom2").value(hasItem(DEFAULT_REPCOM_2)));
    }
    
    @Test
    @Transactional
    public void getAccountExecutive() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get the accountExecutive
        restAccountExecutiveMockMvc.perform(get("/api/account-executives/{id}", accountExecutive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountExecutive.getId().intValue()))
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
    public void getAccountExecutivesByIdFiltering() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        Long id = accountExecutive.getId();

        defaultAccountExecutiveShouldBeFound("id.equals=" + id);
        defaultAccountExecutiveShouldNotBeFound("id.notEquals=" + id);

        defaultAccountExecutiveShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccountExecutiveShouldNotBeFound("id.greaterThan=" + id);

        defaultAccountExecutiveShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccountExecutiveShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAccountExecutivesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where nombre equals to DEFAULT_NOMBRE
        defaultAccountExecutiveShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the accountExecutiveList where nombre equals to UPDATED_NOMBRE
        defaultAccountExecutiveShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where nombre not equals to DEFAULT_NOMBRE
        defaultAccountExecutiveShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the accountExecutiveList where nombre not equals to UPDATED_NOMBRE
        defaultAccountExecutiveShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultAccountExecutiveShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the accountExecutiveList where nombre equals to UPDATED_NOMBRE
        defaultAccountExecutiveShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where nombre is not null
        defaultAccountExecutiveShouldBeFound("nombre.specified=true");

        // Get all the accountExecutiveList where nombre is null
        defaultAccountExecutiveShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccountExecutivesByNombreContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where nombre contains DEFAULT_NOMBRE
        defaultAccountExecutiveShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the accountExecutiveList where nombre contains UPDATED_NOMBRE
        defaultAccountExecutiveShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where nombre does not contain DEFAULT_NOMBRE
        defaultAccountExecutiveShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the accountExecutiveList where nombre does not contain UPDATED_NOMBRE
        defaultAccountExecutiveShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllAccountExecutivesByApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where apellido equals to DEFAULT_APELLIDO
        defaultAccountExecutiveShouldBeFound("apellido.equals=" + DEFAULT_APELLIDO);

        // Get all the accountExecutiveList where apellido equals to UPDATED_APELLIDO
        defaultAccountExecutiveShouldNotBeFound("apellido.equals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where apellido not equals to DEFAULT_APELLIDO
        defaultAccountExecutiveShouldNotBeFound("apellido.notEquals=" + DEFAULT_APELLIDO);

        // Get all the accountExecutiveList where apellido not equals to UPDATED_APELLIDO
        defaultAccountExecutiveShouldBeFound("apellido.notEquals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where apellido in DEFAULT_APELLIDO or UPDATED_APELLIDO
        defaultAccountExecutiveShouldBeFound("apellido.in=" + DEFAULT_APELLIDO + "," + UPDATED_APELLIDO);

        // Get all the accountExecutiveList where apellido equals to UPDATED_APELLIDO
        defaultAccountExecutiveShouldNotBeFound("apellido.in=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where apellido is not null
        defaultAccountExecutiveShouldBeFound("apellido.specified=true");

        // Get all the accountExecutiveList where apellido is null
        defaultAccountExecutiveShouldNotBeFound("apellido.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccountExecutivesByApellidoContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where apellido contains DEFAULT_APELLIDO
        defaultAccountExecutiveShouldBeFound("apellido.contains=" + DEFAULT_APELLIDO);

        // Get all the accountExecutiveList where apellido contains UPDATED_APELLIDO
        defaultAccountExecutiveShouldNotBeFound("apellido.contains=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where apellido does not contain DEFAULT_APELLIDO
        defaultAccountExecutiveShouldNotBeFound("apellido.doesNotContain=" + DEFAULT_APELLIDO);

        // Get all the accountExecutiveList where apellido does not contain UPDATED_APELLIDO
        defaultAccountExecutiveShouldBeFound("apellido.doesNotContain=" + UPDATED_APELLIDO);
    }


    @Test
    @Transactional
    public void getAllAccountExecutivesByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where telefono equals to DEFAULT_TELEFONO
        defaultAccountExecutiveShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the accountExecutiveList where telefono equals to UPDATED_TELEFONO
        defaultAccountExecutiveShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByTelefonoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where telefono not equals to DEFAULT_TELEFONO
        defaultAccountExecutiveShouldNotBeFound("telefono.notEquals=" + DEFAULT_TELEFONO);

        // Get all the accountExecutiveList where telefono not equals to UPDATED_TELEFONO
        defaultAccountExecutiveShouldBeFound("telefono.notEquals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultAccountExecutiveShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the accountExecutiveList where telefono equals to UPDATED_TELEFONO
        defaultAccountExecutiveShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where telefono is not null
        defaultAccountExecutiveShouldBeFound("telefono.specified=true");

        // Get all the accountExecutiveList where telefono is null
        defaultAccountExecutiveShouldNotBeFound("telefono.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccountExecutivesByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where telefono contains DEFAULT_TELEFONO
        defaultAccountExecutiveShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the accountExecutiveList where telefono contains UPDATED_TELEFONO
        defaultAccountExecutiveShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where telefono does not contain DEFAULT_TELEFONO
        defaultAccountExecutiveShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the accountExecutiveList where telefono does not contain UPDATED_TELEFONO
        defaultAccountExecutiveShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }


    @Test
    @Transactional
    public void getAllAccountExecutivesByCelularIsEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where celular equals to DEFAULT_CELULAR
        defaultAccountExecutiveShouldBeFound("celular.equals=" + DEFAULT_CELULAR);

        // Get all the accountExecutiveList where celular equals to UPDATED_CELULAR
        defaultAccountExecutiveShouldNotBeFound("celular.equals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByCelularIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where celular not equals to DEFAULT_CELULAR
        defaultAccountExecutiveShouldNotBeFound("celular.notEquals=" + DEFAULT_CELULAR);

        // Get all the accountExecutiveList where celular not equals to UPDATED_CELULAR
        defaultAccountExecutiveShouldBeFound("celular.notEquals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByCelularIsInShouldWork() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where celular in DEFAULT_CELULAR or UPDATED_CELULAR
        defaultAccountExecutiveShouldBeFound("celular.in=" + DEFAULT_CELULAR + "," + UPDATED_CELULAR);

        // Get all the accountExecutiveList where celular equals to UPDATED_CELULAR
        defaultAccountExecutiveShouldNotBeFound("celular.in=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByCelularIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where celular is not null
        defaultAccountExecutiveShouldBeFound("celular.specified=true");

        // Get all the accountExecutiveList where celular is null
        defaultAccountExecutiveShouldNotBeFound("celular.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccountExecutivesByCelularContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where celular contains DEFAULT_CELULAR
        defaultAccountExecutiveShouldBeFound("celular.contains=" + DEFAULT_CELULAR);

        // Get all the accountExecutiveList where celular contains UPDATED_CELULAR
        defaultAccountExecutiveShouldNotBeFound("celular.contains=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByCelularNotContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where celular does not contain DEFAULT_CELULAR
        defaultAccountExecutiveShouldNotBeFound("celular.doesNotContain=" + DEFAULT_CELULAR);

        // Get all the accountExecutiveList where celular does not contain UPDATED_CELULAR
        defaultAccountExecutiveShouldBeFound("celular.doesNotContain=" + UPDATED_CELULAR);
    }


    @Test
    @Transactional
    public void getAllAccountExecutivesByMailIsEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where mail equals to DEFAULT_MAIL
        defaultAccountExecutiveShouldBeFound("mail.equals=" + DEFAULT_MAIL);

        // Get all the accountExecutiveList where mail equals to UPDATED_MAIL
        defaultAccountExecutiveShouldNotBeFound("mail.equals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByMailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where mail not equals to DEFAULT_MAIL
        defaultAccountExecutiveShouldNotBeFound("mail.notEquals=" + DEFAULT_MAIL);

        // Get all the accountExecutiveList where mail not equals to UPDATED_MAIL
        defaultAccountExecutiveShouldBeFound("mail.notEquals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByMailIsInShouldWork() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where mail in DEFAULT_MAIL or UPDATED_MAIL
        defaultAccountExecutiveShouldBeFound("mail.in=" + DEFAULT_MAIL + "," + UPDATED_MAIL);

        // Get all the accountExecutiveList where mail equals to UPDATED_MAIL
        defaultAccountExecutiveShouldNotBeFound("mail.in=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByMailIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where mail is not null
        defaultAccountExecutiveShouldBeFound("mail.specified=true");

        // Get all the accountExecutiveList where mail is null
        defaultAccountExecutiveShouldNotBeFound("mail.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccountExecutivesByMailContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where mail contains DEFAULT_MAIL
        defaultAccountExecutiveShouldBeFound("mail.contains=" + DEFAULT_MAIL);

        // Get all the accountExecutiveList where mail contains UPDATED_MAIL
        defaultAccountExecutiveShouldNotBeFound("mail.contains=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByMailNotContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where mail does not contain DEFAULT_MAIL
        defaultAccountExecutiveShouldNotBeFound("mail.doesNotContain=" + DEFAULT_MAIL);

        // Get all the accountExecutiveList where mail does not contain UPDATED_MAIL
        defaultAccountExecutiveShouldBeFound("mail.doesNotContain=" + UPDATED_MAIL);
    }


    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom1IsEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom1 equals to DEFAULT_REPCOM_1
        defaultAccountExecutiveShouldBeFound("repcom1.equals=" + DEFAULT_REPCOM_1);

        // Get all the accountExecutiveList where repcom1 equals to UPDATED_REPCOM_1
        defaultAccountExecutiveShouldNotBeFound("repcom1.equals=" + UPDATED_REPCOM_1);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom1 not equals to DEFAULT_REPCOM_1
        defaultAccountExecutiveShouldNotBeFound("repcom1.notEquals=" + DEFAULT_REPCOM_1);

        // Get all the accountExecutiveList where repcom1 not equals to UPDATED_REPCOM_1
        defaultAccountExecutiveShouldBeFound("repcom1.notEquals=" + UPDATED_REPCOM_1);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom1IsInShouldWork() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom1 in DEFAULT_REPCOM_1 or UPDATED_REPCOM_1
        defaultAccountExecutiveShouldBeFound("repcom1.in=" + DEFAULT_REPCOM_1 + "," + UPDATED_REPCOM_1);

        // Get all the accountExecutiveList where repcom1 equals to UPDATED_REPCOM_1
        defaultAccountExecutiveShouldNotBeFound("repcom1.in=" + UPDATED_REPCOM_1);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom1IsNullOrNotNull() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom1 is not null
        defaultAccountExecutiveShouldBeFound("repcom1.specified=true");

        // Get all the accountExecutiveList where repcom1 is null
        defaultAccountExecutiveShouldNotBeFound("repcom1.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom1ContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom1 contains DEFAULT_REPCOM_1
        defaultAccountExecutiveShouldBeFound("repcom1.contains=" + DEFAULT_REPCOM_1);

        // Get all the accountExecutiveList where repcom1 contains UPDATED_REPCOM_1
        defaultAccountExecutiveShouldNotBeFound("repcom1.contains=" + UPDATED_REPCOM_1);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom1NotContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom1 does not contain DEFAULT_REPCOM_1
        defaultAccountExecutiveShouldNotBeFound("repcom1.doesNotContain=" + DEFAULT_REPCOM_1);

        // Get all the accountExecutiveList where repcom1 does not contain UPDATED_REPCOM_1
        defaultAccountExecutiveShouldBeFound("repcom1.doesNotContain=" + UPDATED_REPCOM_1);
    }


    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom2IsEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom2 equals to DEFAULT_REPCOM_2
        defaultAccountExecutiveShouldBeFound("repcom2.equals=" + DEFAULT_REPCOM_2);

        // Get all the accountExecutiveList where repcom2 equals to UPDATED_REPCOM_2
        defaultAccountExecutiveShouldNotBeFound("repcom2.equals=" + UPDATED_REPCOM_2);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom2 not equals to DEFAULT_REPCOM_2
        defaultAccountExecutiveShouldNotBeFound("repcom2.notEquals=" + DEFAULT_REPCOM_2);

        // Get all the accountExecutiveList where repcom2 not equals to UPDATED_REPCOM_2
        defaultAccountExecutiveShouldBeFound("repcom2.notEquals=" + UPDATED_REPCOM_2);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom2IsInShouldWork() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom2 in DEFAULT_REPCOM_2 or UPDATED_REPCOM_2
        defaultAccountExecutiveShouldBeFound("repcom2.in=" + DEFAULT_REPCOM_2 + "," + UPDATED_REPCOM_2);

        // Get all the accountExecutiveList where repcom2 equals to UPDATED_REPCOM_2
        defaultAccountExecutiveShouldNotBeFound("repcom2.in=" + UPDATED_REPCOM_2);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom2IsNullOrNotNull() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom2 is not null
        defaultAccountExecutiveShouldBeFound("repcom2.specified=true");

        // Get all the accountExecutiveList where repcom2 is null
        defaultAccountExecutiveShouldNotBeFound("repcom2.specified=false");
    }
                @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom2ContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom2 contains DEFAULT_REPCOM_2
        defaultAccountExecutiveShouldBeFound("repcom2.contains=" + DEFAULT_REPCOM_2);

        // Get all the accountExecutiveList where repcom2 contains UPDATED_REPCOM_2
        defaultAccountExecutiveShouldNotBeFound("repcom2.contains=" + UPDATED_REPCOM_2);
    }

    @Test
    @Transactional
    public void getAllAccountExecutivesByRepcom2NotContainsSomething() throws Exception {
        // Initialize the database
        accountExecutiveRepository.saveAndFlush(accountExecutive);

        // Get all the accountExecutiveList where repcom2 does not contain DEFAULT_REPCOM_2
        defaultAccountExecutiveShouldNotBeFound("repcom2.doesNotContain=" + DEFAULT_REPCOM_2);

        // Get all the accountExecutiveList where repcom2 does not contain UPDATED_REPCOM_2
        defaultAccountExecutiveShouldBeFound("repcom2.doesNotContain=" + UPDATED_REPCOM_2);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountExecutiveShouldBeFound(String filter) throws Exception {
        restAccountExecutiveMockMvc.perform(get("/api/account-executives?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountExecutive.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].repcom1").value(hasItem(DEFAULT_REPCOM_1)))
            .andExpect(jsonPath("$.[*].repcom2").value(hasItem(DEFAULT_REPCOM_2)));

        // Check, that the count call also returns 1
        restAccountExecutiveMockMvc.perform(get("/api/account-executives/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountExecutiveShouldNotBeFound(String filter) throws Exception {
        restAccountExecutiveMockMvc.perform(get("/api/account-executives?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountExecutiveMockMvc.perform(get("/api/account-executives/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAccountExecutive() throws Exception {
        // Get the accountExecutive
        restAccountExecutiveMockMvc.perform(get("/api/account-executives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountExecutive() throws Exception {
        // Initialize the database
        accountExecutiveService.save(accountExecutive);

        int databaseSizeBeforeUpdate = accountExecutiveRepository.findAll().size();

        // Update the accountExecutive
        AccountExecutive updatedAccountExecutive = accountExecutiveRepository.findById(accountExecutive.getId()).get();
        // Disconnect from session so that the updates on updatedAccountExecutive are not directly saved in db
        em.detach(updatedAccountExecutive);
        updatedAccountExecutive
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .telefono(UPDATED_TELEFONO)
            .celular(UPDATED_CELULAR)
            .mail(UPDATED_MAIL)
            .repcom1(UPDATED_REPCOM_1)
            .repcom2(UPDATED_REPCOM_2);

        restAccountExecutiveMockMvc.perform(put("/api/account-executives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountExecutive)))
            .andExpect(status().isOk());

        // Validate the AccountExecutive in the database
        List<AccountExecutive> accountExecutiveList = accountExecutiveRepository.findAll();
        assertThat(accountExecutiveList).hasSize(databaseSizeBeforeUpdate);
        AccountExecutive testAccountExecutive = accountExecutiveList.get(accountExecutiveList.size() - 1);
        assertThat(testAccountExecutive.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAccountExecutive.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testAccountExecutive.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testAccountExecutive.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testAccountExecutive.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testAccountExecutive.getRepcom1()).isEqualTo(UPDATED_REPCOM_1);
        assertThat(testAccountExecutive.getRepcom2()).isEqualTo(UPDATED_REPCOM_2);

        // Validate the AccountExecutive in Elasticsearch
        verify(mockAccountExecutiveSearchRepository, times(2)).save(testAccountExecutive);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountExecutive() throws Exception {
        int databaseSizeBeforeUpdate = accountExecutiveRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountExecutiveMockMvc.perform(put("/api/account-executives")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(accountExecutive)))
            .andExpect(status().isBadRequest());

        // Validate the AccountExecutive in the database
        List<AccountExecutive> accountExecutiveList = accountExecutiveRepository.findAll();
        assertThat(accountExecutiveList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountExecutive in Elasticsearch
        verify(mockAccountExecutiveSearchRepository, times(0)).save(accountExecutive);
    }

    @Test
    @Transactional
    public void deleteAccountExecutive() throws Exception {
        // Initialize the database
        accountExecutiveService.save(accountExecutive);

        int databaseSizeBeforeDelete = accountExecutiveRepository.findAll().size();

        // Delete the accountExecutive
        restAccountExecutiveMockMvc.perform(delete("/api/account-executives/{id}", accountExecutive.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountExecutive> accountExecutiveList = accountExecutiveRepository.findAll();
        assertThat(accountExecutiveList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccountExecutive in Elasticsearch
        verify(mockAccountExecutiveSearchRepository, times(1)).deleteById(accountExecutive.getId());
    }

    @Test
    @Transactional
    public void searchAccountExecutive() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accountExecutiveService.save(accountExecutive);
        when(mockAccountExecutiveSearchRepository.search(queryStringQuery("id:" + accountExecutive.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountExecutive), PageRequest.of(0, 1), 1));

        // Search the accountExecutive
        restAccountExecutiveMockMvc.perform(get("/api/_search/account-executives?query=id:" + accountExecutive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountExecutive.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].repcom1").value(hasItem(DEFAULT_REPCOM_1)))
            .andExpect(jsonPath("$.[*].repcom2").value(hasItem(DEFAULT_REPCOM_2)));
    }
}
