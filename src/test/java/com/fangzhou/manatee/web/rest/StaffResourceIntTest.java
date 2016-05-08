package com.fangzhou.manatee.web.rest;

import com.fangzhou.manatee.ManateeApp;
import com.fangzhou.manatee.domain.Staff;
import com.fangzhou.manatee.repository.StaffRepository;

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
 * Test class for the StaffResource REST controller.
 *
 * @see StaffResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ManateeApp.class)
@WebAppConfiguration
@IntegrationTest
public class StaffResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ROLE = "AAAAA";
    private static final String UPDATED_ROLE = "BBBBB";

    @Inject
    private StaffRepository staffRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStaffMockMvc;

    private Staff staff;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StaffResource staffResource = new StaffResource();
        ReflectionTestUtils.setField(staffResource, "staffRepository", staffRepository);
        this.restStaffMockMvc = MockMvcBuilders.standaloneSetup(staffResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        staff = new Staff();
        staff.setName(DEFAULT_NAME);
        staff.setRole(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createStaff() throws Exception {
        int databaseSizeBeforeCreate = staffRepository.findAll().size();

        // Create the Staff

        restStaffMockMvc.perform(post("/api/staff")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staff)))
                .andExpect(status().isCreated());

        // Validate the Staff in the database
        List<Staff> staff = staffRepository.findAll();
        assertThat(staff).hasSize(databaseSizeBeforeCreate + 1);
        Staff testStaff = staff.get(staff.size() - 1);
        assertThat(testStaff.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStaff.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void getAllStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staff
        restStaffMockMvc.perform(get("/api/staff?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(staff.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    public void getStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get the staff
        restStaffMockMvc.perform(get("/api/staff/{id}", staff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(staff.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStaff() throws Exception {
        // Get the staff
        restStaffMockMvc.perform(get("/api/staff/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff
        Staff updatedStaff = new Staff();
        updatedStaff.setId(staff.getId());
        updatedStaff.setName(UPDATED_NAME);
        updatedStaff.setRole(UPDATED_ROLE);

        restStaffMockMvc.perform(put("/api/staff")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStaff)))
                .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staff = staffRepository.findAll();
        assertThat(staff).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staff.get(staff.size() - 1);
        assertThat(testStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaff.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void deleteStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);
        int databaseSizeBeforeDelete = staffRepository.findAll().size();

        // Get the staff
        restStaffMockMvc.perform(delete("/api/staff/{id}", staff.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Staff> staff = staffRepository.findAll();
        assertThat(staff).hasSize(databaseSizeBeforeDelete - 1);
    }
}
