package com.fangzhou.manatee.web.rest;

import com.fangzhou.manatee.ManateeApp;
import com.fangzhou.manatee.domain.ReferralSource;
import com.fangzhou.manatee.repository.ReferralSourceRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ReferralSourceResource REST controller.
 *
 * @see ReferralSourceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ManateeApp.class)
@WebAppConfiguration
@IntegrationTest
public class ReferralSourceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CONTACT = "AAAAA";
    private static final String UPDATED_CONTACT = "BBBBB";

    @Inject
    private ReferralSourceRepository referralSourceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReferralSourceMockMvc;

    private ReferralSource referralSource;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReferralSourceResource referralSourceResource = new ReferralSourceResource();
        ReflectionTestUtils.setField(referralSourceResource, "referralSourceRepository", referralSourceRepository);
        this.restReferralSourceMockMvc = MockMvcBuilders.standaloneSetup(referralSourceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        referralSource = new ReferralSource();
        referralSource.setName(DEFAULT_NAME);
        referralSource.setContact(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    public void createReferralSource() throws Exception {
        int databaseSizeBeforeCreate = referralSourceRepository.findAll().size();

        // Create the ReferralSource

        restReferralSourceMockMvc.perform(post("/api/referral-sources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(referralSource)))
                .andExpect(status().isCreated());

        // Validate the ReferralSource in the database
        List<ReferralSource> referralSources = referralSourceRepository.findAll();
        assertThat(referralSources).hasSize(databaseSizeBeforeCreate + 1);
        ReferralSource testReferralSource = referralSources.get(referralSources.size() - 1);
        assertThat(testReferralSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReferralSource.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    public void getAllReferralSources() throws Exception {
        // Initialize the database
        referralSourceRepository.saveAndFlush(referralSource);

        // Get all the referralSources
        restReferralSourceMockMvc.perform(get("/api/referral-sources?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(referralSource.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())));
    }

    @Test
    @Transactional
    public void getReferralSource() throws Exception {
        // Initialize the database
        referralSourceRepository.saveAndFlush(referralSource);

        // Get the referralSource
        restReferralSourceMockMvc.perform(get("/api/referral-sources/{id}", referralSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(referralSource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReferralSource() throws Exception {
        // Get the referralSource
        restReferralSourceMockMvc.perform(get("/api/referral-sources/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReferralSource() throws Exception {
        // Initialize the database
        referralSourceRepository.saveAndFlush(referralSource);
        int databaseSizeBeforeUpdate = referralSourceRepository.findAll().size();

        // Update the referralSource
        ReferralSource updatedReferralSource = new ReferralSource();
        updatedReferralSource.setId(referralSource.getId());
        updatedReferralSource.setName(UPDATED_NAME);
        updatedReferralSource.setContact(UPDATED_CONTACT);

        restReferralSourceMockMvc.perform(put("/api/referral-sources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReferralSource)))
                .andExpect(status().isOk());

        // Validate the ReferralSource in the database
        List<ReferralSource> referralSources = referralSourceRepository.findAll();
        assertThat(referralSources).hasSize(databaseSizeBeforeUpdate);
        ReferralSource testReferralSource = referralSources.get(referralSources.size() - 1);
        assertThat(testReferralSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReferralSource.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void deleteReferralSource() throws Exception {
        // Initialize the database
        referralSourceRepository.saveAndFlush(referralSource);
        int databaseSizeBeforeDelete = referralSourceRepository.findAll().size();

        // Get the referralSource
        restReferralSourceMockMvc.perform(delete("/api/referral-sources/{id}", referralSource.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ReferralSource> referralSources = referralSourceRepository.findAll();
        assertThat(referralSources).hasSize(databaseSizeBeforeDelete - 1);
    }
}
