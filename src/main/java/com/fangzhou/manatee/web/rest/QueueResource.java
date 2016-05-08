package com.fangzhou.manatee.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fangzhou.manatee.domain.Queue;
import com.fangzhou.manatee.repository.QueueRepository;
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
 * REST controller for managing Queue.
 */
@RestController
@RequestMapping("/api")
public class QueueResource {

    private final Logger log = LoggerFactory.getLogger(QueueResource.class);
        
    @Inject
    private QueueRepository queueRepository;
    
    /**
     * POST  /queues : Create a new queue.
     *
     * @param queue the queue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new queue, or with status 400 (Bad Request) if the queue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/queues",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Queue> createQueue(@RequestBody Queue queue) throws URISyntaxException {
        log.debug("REST request to save Queue : {}", queue);
        if (queue.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("queue", "idexists", "A new queue cannot already have an ID")).body(null);
        }
        Queue result = queueRepository.save(queue);
        return ResponseEntity.created(new URI("/api/queues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("queue", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /queues : Updates an existing queue.
     *
     * @param queue the queue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated queue,
     * or with status 400 (Bad Request) if the queue is not valid,
     * or with status 500 (Internal Server Error) if the queue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/queues",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Queue> updateQueue(@RequestBody Queue queue) throws URISyntaxException {
        log.debug("REST request to update Queue : {}", queue);
        if (queue.getId() == null) {
            return createQueue(queue);
        }
        Queue result = queueRepository.save(queue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("queue", queue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /queues : get all the queues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of queues in body
     */
    @RequestMapping(value = "/queues",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Queue> getAllQueues() {
        log.debug("REST request to get all Queues");
        List<Queue> queues = queueRepository.findAll();
        return queues;
    }

    /**
     * GET  /queues/:id : get the "id" queue.
     *
     * @param id the id of the queue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the queue, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/queues/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Queue> getQueue(@PathVariable Long id) {
        log.debug("REST request to get Queue : {}", id);
        Queue queue = queueRepository.findOne(id);
        return Optional.ofNullable(queue)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /queues/:id : delete the "id" queue.
     *
     * @param id the id of the queue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/queues/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteQueue(@PathVariable Long id) {
        log.debug("REST request to delete Queue : {}", id);
        queueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("queue", id.toString())).build();
    }

}
