package com.fangzhou.manatee.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fangzhou.manatee.domain.Staff;
import com.fangzhou.manatee.repository.StaffRepository;
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
 * REST controller for managing Staff.
 */
@RestController
@RequestMapping("/api")
public class StaffResource {

    private final Logger log = LoggerFactory.getLogger(StaffResource.class);
        
    @Inject
    private StaffRepository staffRepository;
    
    /**
     * POST  /staff : Create a new staff.
     *
     * @param staff the staff to create
     * @return the ResponseEntity with status 201 (Created) and with body the new staff, or with status 400 (Bad Request) if the staff has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/staff",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) throws URISyntaxException {
        log.debug("REST request to save Staff : {}", staff);
        if (staff.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("staff", "idexists", "A new staff cannot already have an ID")).body(null);
        }
        Staff result = staffRepository.save(staff);
        return ResponseEntity.created(new URI("/api/staff/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("staff", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /staff : Updates an existing staff.
     *
     * @param staff the staff to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated staff,
     * or with status 400 (Bad Request) if the staff is not valid,
     * or with status 500 (Internal Server Error) if the staff couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/staff",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Staff> updateStaff(@RequestBody Staff staff) throws URISyntaxException {
        log.debug("REST request to update Staff : {}", staff);
        if (staff.getId() == null) {
            return createStaff(staff);
        }
        Staff result = staffRepository.save(staff);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("staff", staff.getId().toString()))
            .body(result);
    }

    /**
     * GET  /staff : get all the staff.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of staff in body
     */
    @RequestMapping(value = "/staff",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Staff> getAllStaff() {
        log.debug("REST request to get all Staff");
        List<Staff> staff = staffRepository.findAll();
        return staff;
    }

    /**
     * GET  /staff/:id : get the "id" staff.
     *
     * @param id the id of the staff to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staff, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/staff/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Staff> getStaff(@PathVariable Long id) {
        log.debug("REST request to get Staff : {}", id);
        Staff staff = staffRepository.findOne(id);
        return Optional.ofNullable(staff)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /staff/:id : delete the "id" staff.
     *
     * @param id the id of the staff to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/staff/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        log.debug("REST request to delete Staff : {}", id);
        staffRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("staff", id.toString())).build();
    }

}
