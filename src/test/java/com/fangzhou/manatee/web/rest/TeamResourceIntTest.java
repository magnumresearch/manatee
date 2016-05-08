package com.fangzhou.manatee.web.rest;

import com.fangzhou.manatee.ManateeApp;
import com.fangzhou.manatee.domain.Team;
import com.fangzhou.manatee.repository.TeamRepository;

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
 * Test class for the TeamResource REST controller.
 *
 * @see TeamResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ManateeApp.class)
@WebAppConfiguration
@IntegrationTest
public class TeamResourceIntTest {

    private static final String DEFAULT_ORGANIZATION = "AAAAA";
    private static final String UPDATED_ORGANIZATION = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SPECIALTY = "AAAAA";
    private static final String UPDATED_SPECIALTY = "BBBBB";

    private static final Long DEFAULT_MAX_PATIENTS = 1L;
    private static final Long UPDATED_MAX_PATIENTS = 2L;

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTeamMockMvc;

    private Team team;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeamResource teamResource = new TeamResource();
        ReflectionTestUtils.setField(teamResource, "teamRepository", teamRepository);
        this.restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        team = new Team();
        team.setOrganization(DEFAULT_ORGANIZATION);
        team.setName(DEFAULT_NAME);
        team.setSpecialty(DEFAULT_SPECIALTY);
        team.setMaxPatients(DEFAULT_MAX_PATIENTS);
    }

    @Test
    @Transactional
    public void createTeam() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team

        restTeamMockMvc.perform(post("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(team)))
                .andExpect(status().isCreated());

        // Validate the Team in the database
        List<Team> teams = teamRepository.findAll();
        assertThat(teams).hasSize(databaseSizeBeforeCreate + 1);
        Team testTeam = teams.get(teams.size() - 1);
        assertThat(testTeam.getOrganization()).isEqualTo(DEFAULT_ORGANIZATION);
        assertThat(testTeam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeam.getSpecialty()).isEqualTo(DEFAULT_SPECIALTY);
        assertThat(testTeam.getMaxPatients()).isEqualTo(DEFAULT_MAX_PATIENTS);
    }

    @Test
    @Transactional
    public void getAllTeams() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teams
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
                .andExpect(jsonPath("$.[*].organization").value(hasItem(DEFAULT_ORGANIZATION.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].specialty").value(hasItem(DEFAULT_SPECIALTY.toString())))
                .andExpect(jsonPath("$.[*].maxPatients").value(hasItem(DEFAULT_MAX_PATIENTS.intValue())));
    }

    @Test
    @Transactional
    public void getTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", team.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(team.getId().intValue()))
            .andExpect(jsonPath("$.organization").value(DEFAULT_ORGANIZATION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.specialty").value(DEFAULT_SPECIALTY.toString()))
            .andExpect(jsonPath("$.maxPatients").value(DEFAULT_MAX_PATIENTS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTeam() throws Exception {
        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Update the team
        Team updatedTeam = new Team();
        updatedTeam.setId(team.getId());
        updatedTeam.setOrganization(UPDATED_ORGANIZATION);
        updatedTeam.setName(UPDATED_NAME);
        updatedTeam.setSpecialty(UPDATED_SPECIALTY);
        updatedTeam.setMaxPatients(UPDATED_MAX_PATIENTS);

        restTeamMockMvc.perform(put("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTeam)))
                .andExpect(status().isOk());

        // Validate the Team in the database
        List<Team> teams = teamRepository.findAll();
        assertThat(teams).hasSize(databaseSizeBeforeUpdate);
        Team testTeam = teams.get(teams.size() - 1);
        assertThat(testTeam.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testTeam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeam.getSpecialty()).isEqualTo(UPDATED_SPECIALTY);
        assertThat(testTeam.getMaxPatients()).isEqualTo(UPDATED_MAX_PATIENTS);
    }

    @Test
    @Transactional
    public void deleteTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        int databaseSizeBeforeDelete = teamRepository.findAll().size();

        // Get the team
        restTeamMockMvc.perform(delete("/api/teams/{id}", team.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Team> teams = teamRepository.findAll();
        assertThat(teams).hasSize(databaseSizeBeforeDelete - 1);
    }
}
