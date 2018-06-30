package io.dmt.moneyxchange.web.rest;

import io.dmt.moneyxchange.MoneyxchangeApp;

import io.dmt.moneyxchange.domain.SpotExchange;
import io.dmt.moneyxchange.domain.Currency;
import io.dmt.moneyxchange.domain.Currency;
import io.dmt.moneyxchange.repository.SpotExchangeRepository;
import io.dmt.moneyxchange.service.SpotExchangeService;
import io.dmt.moneyxchange.repository.search.SpotExchangeSearchRepository;
import io.dmt.moneyxchange.service.dto.SpotExchangeDTO;
import io.dmt.moneyxchange.service.mapper.SpotExchangeMapper;
import io.dmt.moneyxchange.web.rest.errors.ExceptionTranslator;
import io.dmt.moneyxchange.service.dto.SpotExchangeCriteria;
import io.dmt.moneyxchange.service.SpotExchangeQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.dmt.moneyxchange.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.dmt.moneyxchange.domain.enumeration.Operation;
/**
 * Test class for the SpotExchangeResource REST controller.
 *
 * @see SpotExchangeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoneyxchangeApp.class)
public class SpotExchangeResourceIntTest {

    private static final Instant DEFAULT_FROM_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Operation DEFAULT_OPERATION = Operation.MULTIPLY;
    private static final Operation UPDATED_OPERATION = Operation.DIVIDE;

    @Autowired
    private SpotExchangeRepository spotExchangeRepository;

    @Autowired
    private SpotExchangeMapper spotExchangeMapper;

    @Autowired
    private SpotExchangeService spotExchangeService;

    @Autowired
    private SpotExchangeSearchRepository spotExchangeSearchRepository;

    @Autowired
    private SpotExchangeQueryService spotExchangeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSpotExchangeMockMvc;

    private SpotExchange spotExchange;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SpotExchangeResource spotExchangeResource = new SpotExchangeResource(spotExchangeService, spotExchangeQueryService);
        this.restSpotExchangeMockMvc = MockMvcBuilders.standaloneSetup(spotExchangeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpotExchange createEntity(EntityManager em) {
        SpotExchange spotExchange = new SpotExchange()
            .fromInstant(DEFAULT_FROM_INSTANT)
            .operation(DEFAULT_OPERATION);
        // Add required entity
        Currency sourceCurrency = CurrencyResourceIntTest.createEntity(em);
        em.persist(sourceCurrency);
        em.flush();
        spotExchange.setSourceCurrency(sourceCurrency);
        // Add required entity
        Currency targetCurrency = CurrencyResourceIntTest.createEntity(em);
        em.persist(targetCurrency);
        em.flush();
        spotExchange.setTargetCurrency(targetCurrency);
        return spotExchange;
    }

    @Before
    public void initTest() {
        spotExchangeSearchRepository.deleteAll();
        spotExchange = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpotExchange() throws Exception {
        int databaseSizeBeforeCreate = spotExchangeRepository.findAll().size();

        // Create the SpotExchange
        SpotExchangeDTO spotExchangeDTO = spotExchangeMapper.toDto(spotExchange);
        restSpotExchangeMockMvc.perform(post("/api/spot-exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spotExchangeDTO)))
            .andExpect(status().isCreated());

        // Validate the SpotExchange in the database
        List<SpotExchange> spotExchangeList = spotExchangeRepository.findAll();
        assertThat(spotExchangeList).hasSize(databaseSizeBeforeCreate + 1);
        SpotExchange testSpotExchange = spotExchangeList.get(spotExchangeList.size() - 1);
        assertThat(testSpotExchange.getFromInstant()).isEqualTo(DEFAULT_FROM_INSTANT);
        assertThat(testSpotExchange.getOperation()).isEqualTo(DEFAULT_OPERATION);

        // Validate the SpotExchange in Elasticsearch
        SpotExchange spotExchangeEs = spotExchangeSearchRepository.findOne(testSpotExchange.getId());
        assertThat(spotExchangeEs).isEqualToIgnoringGivenFields(testSpotExchange);
    }

    @Test
    @Transactional
    public void createSpotExchangeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = spotExchangeRepository.findAll().size();

        // Create the SpotExchange with an existing ID
        spotExchange.setId(1L);
        SpotExchangeDTO spotExchangeDTO = spotExchangeMapper.toDto(spotExchange);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpotExchangeMockMvc.perform(post("/api/spot-exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spotExchangeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SpotExchange in the database
        List<SpotExchange> spotExchangeList = spotExchangeRepository.findAll();
        assertThat(spotExchangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFromInstantIsRequired() throws Exception {
        int databaseSizeBeforeTest = spotExchangeRepository.findAll().size();
        // set the field null
        spotExchange.setFromInstant(null);

        // Create the SpotExchange, which fails.
        SpotExchangeDTO spotExchangeDTO = spotExchangeMapper.toDto(spotExchange);

        restSpotExchangeMockMvc.perform(post("/api/spot-exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spotExchangeDTO)))
            .andExpect(status().isBadRequest());

        List<SpotExchange> spotExchangeList = spotExchangeRepository.findAll();
        assertThat(spotExchangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = spotExchangeRepository.findAll().size();
        // set the field null
        spotExchange.setOperation(null);

        // Create the SpotExchange, which fails.
        SpotExchangeDTO spotExchangeDTO = spotExchangeMapper.toDto(spotExchange);

        restSpotExchangeMockMvc.perform(post("/api/spot-exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spotExchangeDTO)))
            .andExpect(status().isBadRequest());

        List<SpotExchange> spotExchangeList = spotExchangeRepository.findAll();
        assertThat(spotExchangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpotExchanges() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);

        // Get all the spotExchangeList
        restSpotExchangeMockMvc.perform(get("/api/spot-exchanges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spotExchange.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromInstant").value(hasItem(DEFAULT_FROM_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())));
    }

    @Test
    @Transactional
    public void getSpotExchange() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);

        // Get the spotExchange
        restSpotExchangeMockMvc.perform(get("/api/spot-exchanges/{id}", spotExchange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spotExchange.getId().intValue()))
            .andExpect(jsonPath("$.fromInstant").value(DEFAULT_FROM_INSTANT.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()));
    }

    @Test
    @Transactional
    public void getAllSpotExchangesByFromInstantIsEqualToSomething() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);

        // Get all the spotExchangeList where fromInstant equals to DEFAULT_FROM_INSTANT
        defaultSpotExchangeShouldBeFound("fromInstant.equals=" + DEFAULT_FROM_INSTANT);

        // Get all the spotExchangeList where fromInstant equals to UPDATED_FROM_INSTANT
        defaultSpotExchangeShouldNotBeFound("fromInstant.equals=" + UPDATED_FROM_INSTANT);
    }

    @Test
    @Transactional
    public void getAllSpotExchangesByFromInstantIsInShouldWork() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);

        // Get all the spotExchangeList where fromInstant in DEFAULT_FROM_INSTANT or UPDATED_FROM_INSTANT
        defaultSpotExchangeShouldBeFound("fromInstant.in=" + DEFAULT_FROM_INSTANT + "," + UPDATED_FROM_INSTANT);

        // Get all the spotExchangeList where fromInstant equals to UPDATED_FROM_INSTANT
        defaultSpotExchangeShouldNotBeFound("fromInstant.in=" + UPDATED_FROM_INSTANT);
    }

    @Test
    @Transactional
    public void getAllSpotExchangesByFromInstantIsNullOrNotNull() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);

        // Get all the spotExchangeList where fromInstant is not null
        defaultSpotExchangeShouldBeFound("fromInstant.specified=true");

        // Get all the spotExchangeList where fromInstant is null
        defaultSpotExchangeShouldNotBeFound("fromInstant.specified=false");
    }

    @Test
    @Transactional
    public void getAllSpotExchangesByOperationIsEqualToSomething() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);

        // Get all the spotExchangeList where operation equals to DEFAULT_OPERATION
        defaultSpotExchangeShouldBeFound("operation.equals=" + DEFAULT_OPERATION);

        // Get all the spotExchangeList where operation equals to UPDATED_OPERATION
        defaultSpotExchangeShouldNotBeFound("operation.equals=" + UPDATED_OPERATION);
    }

    @Test
    @Transactional
    public void getAllSpotExchangesByOperationIsInShouldWork() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);

        // Get all the spotExchangeList where operation in DEFAULT_OPERATION or UPDATED_OPERATION
        defaultSpotExchangeShouldBeFound("operation.in=" + DEFAULT_OPERATION + "," + UPDATED_OPERATION);

        // Get all the spotExchangeList where operation equals to UPDATED_OPERATION
        defaultSpotExchangeShouldNotBeFound("operation.in=" + UPDATED_OPERATION);
    }

    @Test
    @Transactional
    public void getAllSpotExchangesByOperationIsNullOrNotNull() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);

        // Get all the spotExchangeList where operation is not null
        defaultSpotExchangeShouldBeFound("operation.specified=true");

        // Get all the spotExchangeList where operation is null
        defaultSpotExchangeShouldNotBeFound("operation.specified=false");
    }

    @Test
    @Transactional
    public void getAllSpotExchangesBySourceCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        Currency sourceCurrency = CurrencyResourceIntTest.createEntity(em);
        em.persist(sourceCurrency);
        em.flush();
        spotExchange.setSourceCurrency(sourceCurrency);
        spotExchangeRepository.saveAndFlush(spotExchange);
        Long sourceCurrencyId = sourceCurrency.getId();

        // Get all the spotExchangeList where sourceCurrency equals to sourceCurrencyId
        defaultSpotExchangeShouldBeFound("sourceCurrencyId.equals=" + sourceCurrencyId);

        // Get all the spotExchangeList where sourceCurrency equals to sourceCurrencyId + 1
        defaultSpotExchangeShouldNotBeFound("sourceCurrencyId.equals=" + (sourceCurrencyId + 1));
    }


    @Test
    @Transactional
    public void getAllSpotExchangesByTargetCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        Currency targetCurrency = CurrencyResourceIntTest.createEntity(em);
        em.persist(targetCurrency);
        em.flush();
        spotExchange.setTargetCurrency(targetCurrency);
        spotExchangeRepository.saveAndFlush(spotExchange);
        Long targetCurrencyId = targetCurrency.getId();

        // Get all the spotExchangeList where targetCurrency equals to targetCurrencyId
        defaultSpotExchangeShouldBeFound("targetCurrencyId.equals=" + targetCurrencyId);

        // Get all the spotExchangeList where targetCurrency equals to targetCurrencyId + 1
        defaultSpotExchangeShouldNotBeFound("targetCurrencyId.equals=" + (targetCurrencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSpotExchangeShouldBeFound(String filter) throws Exception {
        restSpotExchangeMockMvc.perform(get("/api/spot-exchanges?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spotExchange.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromInstant").value(hasItem(DEFAULT_FROM_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSpotExchangeShouldNotBeFound(String filter) throws Exception {
        restSpotExchangeMockMvc.perform(get("/api/spot-exchanges?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSpotExchange() throws Exception {
        // Get the spotExchange
        restSpotExchangeMockMvc.perform(get("/api/spot-exchanges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpotExchange() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);
        spotExchangeSearchRepository.save(spotExchange);
        int databaseSizeBeforeUpdate = spotExchangeRepository.findAll().size();

        // Update the spotExchange
        SpotExchange updatedSpotExchange = spotExchangeRepository.findOne(spotExchange.getId());
        // Disconnect from session so that the updates on updatedSpotExchange are not directly saved in db
        em.detach(updatedSpotExchange);
        updatedSpotExchange
            .fromInstant(UPDATED_FROM_INSTANT)
            .operation(UPDATED_OPERATION);
        SpotExchangeDTO spotExchangeDTO = spotExchangeMapper.toDto(updatedSpotExchange);

        restSpotExchangeMockMvc.perform(put("/api/spot-exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spotExchangeDTO)))
            .andExpect(status().isOk());

        // Validate the SpotExchange in the database
        List<SpotExchange> spotExchangeList = spotExchangeRepository.findAll();
        assertThat(spotExchangeList).hasSize(databaseSizeBeforeUpdate);
        SpotExchange testSpotExchange = spotExchangeList.get(spotExchangeList.size() - 1);
        assertThat(testSpotExchange.getFromInstant()).isEqualTo(UPDATED_FROM_INSTANT);
        assertThat(testSpotExchange.getOperation()).isEqualTo(UPDATED_OPERATION);

        // Validate the SpotExchange in Elasticsearch
        SpotExchange spotExchangeEs = spotExchangeSearchRepository.findOne(testSpotExchange.getId());
        assertThat(spotExchangeEs).isEqualToIgnoringGivenFields(testSpotExchange);
    }

    @Test
    @Transactional
    public void updateNonExistingSpotExchange() throws Exception {
        int databaseSizeBeforeUpdate = spotExchangeRepository.findAll().size();

        // Create the SpotExchange
        SpotExchangeDTO spotExchangeDTO = spotExchangeMapper.toDto(spotExchange);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSpotExchangeMockMvc.perform(put("/api/spot-exchanges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spotExchangeDTO)))
            .andExpect(status().isCreated());

        // Validate the SpotExchange in the database
        List<SpotExchange> spotExchangeList = spotExchangeRepository.findAll();
        assertThat(spotExchangeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSpotExchange() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);
        spotExchangeSearchRepository.save(spotExchange);
        int databaseSizeBeforeDelete = spotExchangeRepository.findAll().size();

        // Get the spotExchange
        restSpotExchangeMockMvc.perform(delete("/api/spot-exchanges/{id}", spotExchange.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean spotExchangeExistsInEs = spotExchangeSearchRepository.exists(spotExchange.getId());
        assertThat(spotExchangeExistsInEs).isFalse();

        // Validate the database is empty
        List<SpotExchange> spotExchangeList = spotExchangeRepository.findAll();
        assertThat(spotExchangeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSpotExchange() throws Exception {
        // Initialize the database
        spotExchangeRepository.saveAndFlush(spotExchange);
        spotExchangeSearchRepository.save(spotExchange);

        // Search the spotExchange
        restSpotExchangeMockMvc.perform(get("/api/_search/spot-exchanges?query=id:" + spotExchange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spotExchange.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromInstant").value(hasItem(DEFAULT_FROM_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpotExchange.class);
        SpotExchange spotExchange1 = new SpotExchange();
        spotExchange1.setId(1L);
        SpotExchange spotExchange2 = new SpotExchange();
        spotExchange2.setId(spotExchange1.getId());
        assertThat(spotExchange1).isEqualTo(spotExchange2);
        spotExchange2.setId(2L);
        assertThat(spotExchange1).isNotEqualTo(spotExchange2);
        spotExchange1.setId(null);
        assertThat(spotExchange1).isNotEqualTo(spotExchange2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpotExchangeDTO.class);
        SpotExchangeDTO spotExchangeDTO1 = new SpotExchangeDTO();
        spotExchangeDTO1.setId(1L);
        SpotExchangeDTO spotExchangeDTO2 = new SpotExchangeDTO();
        assertThat(spotExchangeDTO1).isNotEqualTo(spotExchangeDTO2);
        spotExchangeDTO2.setId(spotExchangeDTO1.getId());
        assertThat(spotExchangeDTO1).isEqualTo(spotExchangeDTO2);
        spotExchangeDTO2.setId(2L);
        assertThat(spotExchangeDTO1).isNotEqualTo(spotExchangeDTO2);
        spotExchangeDTO1.setId(null);
        assertThat(spotExchangeDTO1).isNotEqualTo(spotExchangeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(spotExchangeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(spotExchangeMapper.fromId(null)).isNull();
    }
}
