package io.dmt.moneyxchange.service;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.dmt.moneyxchange.domain.SpotExchange;
import io.dmt.moneyxchange.domain.*; // for static metamodels
import io.dmt.moneyxchange.repository.SpotExchangeRepository;
import io.dmt.moneyxchange.repository.search.SpotExchangeSearchRepository;
import io.dmt.moneyxchange.service.dto.SpotExchangeCriteria;

import io.dmt.moneyxchange.service.dto.SpotExchangeDTO;
import io.dmt.moneyxchange.service.mapper.SpotExchangeMapper;
import io.dmt.moneyxchange.domain.enumeration.Operation;

/**
 * Service for executing complex queries for SpotExchange entities in the database.
 * The main input is a {@link SpotExchangeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SpotExchangeDTO} or a {@link Page} of {@link SpotExchangeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpotExchangeQueryService extends QueryService<SpotExchange> {

    private final Logger log = LoggerFactory.getLogger(SpotExchangeQueryService.class);


    private final SpotExchangeRepository spotExchangeRepository;

    private final SpotExchangeMapper spotExchangeMapper;

    private final SpotExchangeSearchRepository spotExchangeSearchRepository;

    public SpotExchangeQueryService(SpotExchangeRepository spotExchangeRepository, SpotExchangeMapper spotExchangeMapper, SpotExchangeSearchRepository spotExchangeSearchRepository) {
        this.spotExchangeRepository = spotExchangeRepository;
        this.spotExchangeMapper = spotExchangeMapper;
        this.spotExchangeSearchRepository = spotExchangeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SpotExchangeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SpotExchangeDTO> findByCriteria(SpotExchangeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SpotExchange> specification = createSpecification(criteria);
        return spotExchangeMapper.toDto(spotExchangeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SpotExchangeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpotExchangeDTO> findByCriteria(SpotExchangeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SpotExchange> specification = createSpecification(criteria);
        final Page<SpotExchange> result = spotExchangeRepository.findAll(specification, page);
        return result.map(spotExchangeMapper::toDto);
    }

    /**
     * Function to convert SpotExchangeCriteria to a {@link Specifications}
     */
    private Specifications<SpotExchange> createSpecification(SpotExchangeCriteria criteria) {
        Specifications<SpotExchange> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SpotExchange_.id));
            }
            if (criteria.getFromInstant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromInstant(), SpotExchange_.fromInstant));
            }
            if (criteria.getOperation() != null) {
                specification = specification.and(buildSpecification(criteria.getOperation(), SpotExchange_.operation));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), SpotExchange_.rate));
            }
            if (criteria.getSourceCurrencyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSourceCurrencyId(), SpotExchange_.sourceCurrency, Currency_.id));
            }
            if (criteria.getTargetCurrencyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTargetCurrencyId(), SpotExchange_.targetCurrency, Currency_.id));
            }
        }
        return specification;
    }

}
