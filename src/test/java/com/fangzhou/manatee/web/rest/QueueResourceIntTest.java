package com.fangzhou.manatee.web.rest;

import com.fangzhou.manatee.ManateeApp;
import com.fangzhou.manatee.domain.Queue;
import com.fangzhou.manatee.repository.QueueRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the QueueResource REST controller.
 *
 * @see QueueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ManateeApp.class)
@WebAppConfiguration
@IntegrationTest
public class QueueResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    private static final ZonedDateTime DEFAULT_TIMESTAMP_INITIAL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIMESTAMP_INITIAL = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIMESTAMP_INITIAL_STR = dateTimeFormatter.format(DEFAULT_TIMESTAMP_INITIAL);

    private static final ZonedDateTime DEFAULT_TIMESTAMP_FINAL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIMESTAMP_FINAL = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIMESTAMP_FINAL_STR = dateTimeFormatter.format(DEFAULT_TIMESTAMP_FINAL);

    @Inject
    private QueueRepository queueRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restQueueMockMvc;

    private Queue queue;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QueueResource queueResource = new QueueResource();
        ReflectionTestUtils.setField(queueResource, "queueRepository", queueRepository);
        this.restQueueMockMvc = MockMvcBuilders.standaloneSetup(queueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        queue = new Queue();
        queue.setStatus(DEFAULT_STATUS);
        queue.setTimestampInitial(DEFAULT_TIMESTAMP_INITIAL);
        queue.setTimestampFinal(DEFAULT_TIMESTAMP_FINAL);
    }

    @Test
    @Transactional
    public void createQueue() throws Exception {
        int databaseSizeBeforeCreate = queueRepository.findAll().size();

        // Create the Queue

        restQueueMockMvc.perform(post("/api/queues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(queue)))
                .andExpect(status().isCreated());

        // Validate the Queue in the database
        List<Queue> queues = queueRepository.findAll();
        assertThat(queues).hasSize(databaseSizeBeforeCreate + 1);
        Queue testQueue = queues.get(queues.size() - 1);
        assertThat(testQueue.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQueue.getTimestampInitial()).isEqualTo(DEFAULT_TIMESTAMP_INITIAL);
        assertThat(testQueue.getTimestampFinal()).isEqualTo(DEFAULT_TIMESTAMP_FINAL);
    }

    @Test
    @Transactional
    public void getAllQueues() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);

        // Get all the queues
        restQueueMockMvc.perform(get("/api/queues?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(queue.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].timestampInitial").value(hasItem(DEFAULT_TIMESTAMP_INITIAL_STR)))
                .andExpect(jsonPath("$.[*].timestampFinal").value(hasItem(DEFAULT_TIMESTAMP_FINAL_STR)));
    }

    @Test
    @Transactional
    public void getQueue() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);

        // Get the queue
        restQueueMockMvc.perform(get("/api/queues/{id}", queue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(queue.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.timestampInitial").value(DEFAULT_TIMESTAMP_INITIAL_STR))
            .andExpect(jsonPath("$.timestampFinal").value(DEFAULT_TIMESTAMP_FINAL_STR));
    }

    @Test
    @Transactional
    public void getNonExistingQueue() throws Exception {
        // Get the queue
        restQueueMockMvc.perform(get("/api/queues/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQueue() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();

        // Update the queue
        Queue updatedQueue = new Queue();
        updatedQueue.setId(queue.getId());
        updatedQueue.setStatus(UPDATED_STATUS);
        updatedQueue.setTimestampInitial(UPDATED_TIMESTAMP_INITIAL);
        updatedQueue.setTimestampFinal(UPDATED_TIMESTAMP_FINAL);

        restQueueMockMvc.perform(put("/api/queues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedQueue)))
                .andExpect(status().isOk());

        // Validate the Queue in the database
        List<Queue> queues = queueRepository.findAll();
        assertThat(queues).hasSize(databaseSizeBeforeUpdate);
        Queue testQueue = queues.get(queues.size() - 1);
        assertThat(testQueue.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQueue.getTimestampInitial()).isEqualTo(UPDATED_TIMESTAMP_INITIAL);
        assertThat(testQueue.getTimestampFinal()).isEqualTo(UPDATED_TIMESTAMP_FINAL);
    }

    @Test
    @Transactional
    public void deleteQueue() throws Exception {
        // Initialize the database
        queueRepository.saveAndFlush(queue);
        int databaseSizeBeforeDelete = queueRepository.findAll().size();

        // Get the queue
        restQueueMockMvc.perform(delete("/api/queues/{id}", queue.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Queue> queues = queueRepository.findAll();
        assertThat(queues).hasSize(databaseSizeBeforeDelete - 1);
    }
}
