package com.fangzhou.manatee.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fangzhou.manatee.domain.ReferralSource;
import com.fangzhou.manatee.repository.ReferralSourceRepository;
import com.fangzhou.manatee.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ReferralSource.
 */
@RestController
@RequestMapping("/api")
public class ReferralSourceResource {

    private final Logger log = LoggerFactory.getLogger(ReferralSourceResource.class);
        
    @Inject
    private ReferralSourceRepository referralSourceRepository;
    
    /**
     * POST  /referral-sources : Create a new referralSource.
     *
     * @param referralSource the referralSource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new referralSource, or with status 400 (Bad Request) if the referralSource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/referral-sources",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReferralSource> createReferralSource(@RequestBody ReferralSource referralSource) throws URISyntaxException {
        log.debug("REST request to save ReferralSource : {}", referralSource);
        if (referralSource.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("referralSource", "idexists", "A new referralSource cannot already have an ID")).body(null);
        }
        ReferralSource result = referralSourceRepository.save(referralSource);
        return ResponseEntity.created(new URI("/api/referral-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("referralSource", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /referral-sources : Updates an existing referralSource.
     *
     * @param referralSource the referralSource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated referralSource,
     * or with status 400 (Bad Request) if the referralSource is not valid,
     * or with status 500 (Internal Server Error) if the referralSource couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/referral-sources",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReferralSource> updateReferralSource(@RequestBody ReferralSource referralSource) throws URISyntaxException {
        log.debug("REST request to update ReferralSource : {}", referralSource);
        if (referralSource.getId() == null) {
            return createReferralSource(referralSource);
        }
        ReferralSource result = referralSourceRepository.save(referralSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("referralSource", referralSource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /referral-sources : get all the referralSources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of referralSources in body
     */
    @RequestMapping(value = "/referral-sources",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ReferralSource> getAllReferralSources() {
        log.debug("REST request to get all ReferralSources");
        List<ReferralSource> referralSources = referralSourceRepository.findAll();
        return referralSources;
    }

    /**
     * GET  /referral-sources/:id : get the "id" referralSource.
     *
     * @param id the id of the referralSource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the referralSource, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/referral-sources/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReferralSource> getReferralSource(@PathVariable Long id) {
        log.debug("REST request to get ReferralSource : {}", id);
        ReferralSource referralSource = referralSourceRepository.findOne(id);
        return Optional.ofNullable(referralSource)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /referral-sources/:id : delete the "id" referralSource.
     *
     * @param id the id of the referralSource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/referral-sources/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReferralSource(@PathVariable Long id) {
        log.debug("REST request to delete ReferralSource : {}", id);
        referralSourceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("referralSource", id.toString())).build();
    }

}
