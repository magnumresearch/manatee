package com.fangzhou.manatee.web.rest;

import com.fangzhou.manatee.ManateeApp;
import com.fangzhou.manatee.domain.Patient;
import com.fangzhou.manatee.repository.PatientRepository;

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
 * Test class for the PatientResource REST controller.
 *
 * @see PatientResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ManateeApp.class)
@WebAppConfiguration
@IntegrationTest
public class PatientResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_MEDICAL_REFERRAL_ID = "AAAAA";
    private static final String UPDATED_MEDICAL_REFERRAL_ID = "BBBBB";

    private static final Long DEFAULT_AGE = 1L;
    private static final Long UPDATED_AGE = 2L;
    private static final String DEFAULT_CONDITION_DESCIPTION = "AAAAA";
    private static final String UPDATED_CONDITION_DESCIPTION = "BBBBB";
    private static final String DEFAULT_PRIORITY = "AAAAA";
    private static final String UPDATED_PRIORITY = "BBBBB";

    private static final ZonedDateTime DEFAULT_DEADLINE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DEADLINE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DEADLINE_STR = dateTimeFormatter.format(DEFAULT_DEADLINE);

    @Inject
    private PatientRepository patientRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPatientMockMvc;

    private Patient patient;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PatientResource patientResource = new PatientResource();
        ReflectionTestUtils.setField(patientResource, "patientRepository", patientRepository);
        this.restPatientMockMvc = MockMvcBuilders.standaloneSetup(patientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        patient = new Patient();
        patient.setName(DEFAULT_NAME);
        patient.setMedicalReferralId(DEFAULT_MEDICAL_REFERRAL_ID);
        patient.setAge(DEFAULT_AGE);
        patient.setConditionDesciption(DEFAULT_CONDITION_DESCIPTION);
        patient.setPriority(DEFAULT_PRIORITY);
        patient.setDeadline(DEFAULT_DEADLINE);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient

        restPatientMockMvc.perform(post("/api/patients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(patient)))
                .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patients = patientRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patients.get(patients.size() - 1);
        assertThat(testPatient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatient.getMedicalReferralId()).isEqualTo(DEFAULT_MEDICAL_REFERRAL_ID);
        assertThat(testPatient.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPatient.getConditionDesciption()).isEqualTo(DEFAULT_CONDITION_DESCIPTION);
        assertThat(testPatient.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testPatient.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patients
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].medicalReferralId").value(hasItem(DEFAULT_MEDICAL_REFERRAL_ID.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.intValue())))
                .andExpect(jsonPath("$.[*].conditionDesciption").value(hasItem(DEFAULT_CONDITION_DESCIPTION.toString())))
                .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
                .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE_STR)));
    }

    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.medicalReferralId").value(DEFAULT_MEDICAL_REFERRAL_ID.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.intValue()))
            .andExpect(jsonPath("$.conditionDesciption").value(DEFAULT_CONDITION_DESCIPTION.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = new Patient();
        updatedPatient.setId(patient.getId());
        updatedPatient.setName(UPDATED_NAME);
        updatedPatient.setMedicalReferralId(UPDATED_MEDICAL_REFERRAL_ID);
        updatedPatient.setAge(UPDATED_AGE);
        updatedPatient.setConditionDesciption(UPDATED_CONDITION_DESCIPTION);
        updatedPatient.setPriority(UPDATED_PRIORITY);
        updatedPatient.setDeadline(UPDATED_DEADLINE);

        restPatientMockMvc.perform(put("/api/patients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
                .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patients = patientRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patients.get(patients.size() - 1);
        assertThat(testPatient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatient.getMedicalReferralId()).isEqualTo(UPDATED_MEDICAL_REFERRAL_ID);
        assertThat(testPatient.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatient.getConditionDesciption()).isEqualTo(UPDATED_CONDITION_DESCIPTION);
        assertThat(testPatient.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testPatient.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Get the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Patient> patients = patientRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeDelete - 1);
    }
}
