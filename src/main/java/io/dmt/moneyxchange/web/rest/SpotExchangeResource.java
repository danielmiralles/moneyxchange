package io.dmt.moneyxchange.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.dmt.moneyxchange.service.SpotExchangeService;
import io.dmt.moneyxchange.web.rest.errors.BadRequestAlertException;
import io.dmt.moneyxchange.web.rest.util.HeaderUtil;
import io.dmt.moneyxchange.web.rest.util.PaginationUtil;
import io.dmt.moneyxchange.service.dto.SpotExchangeDTO;
import io.dmt.moneyxchange.service.dto.SpotExchangeCriteria;
import io.dmt.moneyxchange.service.SpotExchangeQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SpotExchange.
 */
@RestController
@RequestMapping("/api")
public class SpotExchangeResource {

    private final Logger log = LoggerFactory.getLogger(SpotExchangeResource.class);

    private static final String ENTITY_NAME = "spotExchange";

    private final SpotExchangeService spotExchangeService;

    private final SpotExchangeQueryService spotExchangeQueryService;

    public SpotExchangeResource(SpotExchangeService spotExchangeService, SpotExchangeQueryService spotExchangeQueryService) {
        this.spotExchangeService = spotExchangeService;
        this.spotExchangeQueryService = spotExchangeQueryService;
    }

    /**
     * POST  /spot-exchanges : Create a new spotExchange.
     *
     * @param spotExchangeDTO the spotExchangeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spotExchangeDTO, or with status 400 (Bad Request) if the spotExchange has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/spot-exchanges")
    @Timed
    public ResponseEntity<SpotExchangeDTO> createSpotExchange(@Valid @RequestBody SpotExchangeDTO spotExchangeDTO) throws URISyntaxException {
        log.debug("REST request to save SpotExchange : {}", spotExchangeDTO);
        if (spotExchangeDTO.getId() != null) {
            throw new BadRequestAlertException("A new spotExchange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpotExchangeDTO result = spotExchangeService.save(spotExchangeDTO);
        return ResponseEntity.created(new URI("/api/spot-exchanges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spot-exchanges : Updates an existing spotExchange.
     *
     * @param spotExchangeDTO the spotExchangeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spotExchangeDTO,
     * or with status 400 (Bad Request) if the spotExchangeDTO is not valid,
     * or with status 500 (Internal Server Error) if the spotExchangeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/spot-exchanges")
    @Timed
    public ResponseEntity<SpotExchangeDTO> updateSpotExchange(@Valid @RequestBody SpotExchangeDTO spotExchangeDTO) throws URISyntaxException {
        log.debug("REST request to update SpotExchange : {}", spotExchangeDTO);
        if (spotExchangeDTO.getId() == null) {
            return createSpotExchange(spotExchangeDTO);
        }
        SpotExchangeDTO result = spotExchangeService.save(spotExchangeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, spotExchangeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spot-exchanges : get all the spotExchanges.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of spotExchanges in body
     */
    @GetMapping("/spot-exchanges")
    @Timed
    public ResponseEntity<List<SpotExchangeDTO>> getAllSpotExchanges(SpotExchangeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SpotExchanges by criteria: {}", criteria);
        Page<SpotExchangeDTO> page = spotExchangeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/spot-exchanges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /spot-exchanges/:id : get the "id" spotExchange.
     *
     * @param id the id of the spotExchangeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spotExchangeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/spot-exchanges/{id}")
    @Timed
    public ResponseEntity<SpotExchangeDTO> getSpotExchange(@PathVariable Long id) {
        log.debug("REST request to get SpotExchange : {}", id);
        SpotExchangeDTO spotExchangeDTO = spotExchangeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(spotExchangeDTO));
    }

    /**
     * DELETE  /spot-exchanges/:id : delete the "id" spotExchange.
     *
     * @param id the id of the spotExchangeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/spot-exchanges/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpotExchange(@PathVariable Long id) {
        log.debug("REST request to delete SpotExchange : {}", id);
        spotExchangeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/spot-exchanges?query=:query : search for the spotExchange corresponding
     * to the query.
     *
     * @param query the query of the spotExchange search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/spot-exchanges")
    @Timed
    public ResponseEntity<List<SpotExchangeDTO>> searchSpotExchanges(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SpotExchanges for query {}", query);
        Page<SpotExchangeDTO> page = spotExchangeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/spot-exchanges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
