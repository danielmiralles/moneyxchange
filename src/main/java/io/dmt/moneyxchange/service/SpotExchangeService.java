package io.dmt.moneyxchange.service;

import io.dmt.moneyxchange.config.ApplicationProperties;
import io.dmt.moneyxchange.domain.Currency;
import io.dmt.moneyxchange.domain.SpotExchange;
import io.dmt.moneyxchange.repository.SpotExchangeRepository;
import io.dmt.moneyxchange.repository.search.SpotExchangeSearchRepository;
import io.dmt.moneyxchange.service.dto.ExchangeResponseDTO;
import io.dmt.moneyxchange.service.dto.SpotExchangeDTO;
import io.dmt.moneyxchange.service.mapper.SpotExchangeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SpotExchange.
 */
@Service
@Transactional
public class SpotExchangeService {

    private final ApplicationProperties applicationProperties;

    private final Logger log = LoggerFactory.getLogger(SpotExchangeService.class);

    private final SpotExchangeRepository spotExchangeRepository;

    private final SpotExchangeMapper spotExchangeMapper;

    private final SpotExchangeSearchRepository spotExchangeSearchRepository;

    public SpotExchangeService(SpotExchangeRepository spotExchangeRepository, SpotExchangeMapper spotExchangeMapper, SpotExchangeSearchRepository spotExchangeSearchRepository, ApplicationProperties applicationProperties) {
        this.spotExchangeRepository = spotExchangeRepository;
        this.spotExchangeMapper = spotExchangeMapper;
        this.spotExchangeSearchRepository = spotExchangeSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a spotExchange.
     *
     * @param spotExchangeDTO the entity to save
     * @return the persisted entity
     */
    public SpotExchangeDTO save(SpotExchangeDTO spotExchangeDTO) {
        log.debug("Request to save SpotExchange : {}", spotExchangeDTO);
        SpotExchange spotExchange = spotExchangeMapper.toEntity(spotExchangeDTO);
        spotExchange = spotExchangeRepository.save(spotExchange);
        SpotExchangeDTO result = spotExchangeMapper.toDto(spotExchange);
        spotExchangeSearchRepository.save(spotExchange);
        return result;
    }

    /**
     * Get all the spotExchanges.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SpotExchangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpotExchanges");
        return spotExchangeRepository.findAll(pageable)
            .map(spotExchangeMapper::toDto);
    }

    /**
     * Get one spotExchange by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SpotExchangeDTO findOne(Long id) {
        log.debug("Request to get SpotExchange : {}", id);
        SpotExchange spotExchange = spotExchangeRepository.findOne(id);
        return spotExchangeMapper.toDto(spotExchange);
    }

    /**
     * Delete the spotExchange by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SpotExchange : {}", id);
        spotExchangeRepository.delete(id);
        spotExchangeSearchRepository.delete(id);
    }

    /**
     * Search for the spotExchange corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SpotExchangeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SpotExchanges for query {}", query);
        Page<SpotExchange> result = spotExchangeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(spotExchangeMapper::toDto);
    }

    /**
     *  Search spot exchange for request.
     *
     *  @param instant the instant of time
     */
    public ExchangeResponseDTO getExchangeResponse(Instant instant, Currency source, Currency target){
        Optional<SpotExchange> actualSE = this.spotExchangeRepository.findFirstByFromInstantBeforeAndSourceCurrencyAndTargetCurrencyOrderByFromInstantDesc(instant, source, target);
        Optional<SpotExchange> futureSE = this.spotExchangeRepository.findFirstByFromInstantAfterAndSourceCurrencyAndTargetCurrencyOrderByFromInstantAsc(instant, source, target);

        ExchangeResponseDTO response = new ExchangeResponseDTO();

        if (actualSE.isPresent()){
            response.setExchangeRate(actualSE.get().getRate());
            response.setOperation(actualSE.get().getOperation().name());
            if (futureSE.isPresent()){
                long timeInSecDiff = futureSE.get().getFromInstant().getEpochSecond() - actualSE.get().getFromInstant().getEpochSecond();
                if (timeInSecDiff > applicationProperties.getExchange().getTimeout()){
                    response.setTimeout(applicationProperties.getExchange().getTimeout());
                } else {
                    response.setTimeout((int) timeInSecDiff);
                }
            } else {
                response.setTimeout(applicationProperties.getExchange().getTimeout());
            }
        }

        return response;
    }
}
