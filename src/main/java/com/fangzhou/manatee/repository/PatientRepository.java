package com.fangzhou.manatee.repository;

import com.fangzhou.manatee.domain.Patient;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Patient entity.
 */
@SuppressWarnings("unused")
public interface PatientRepository extends JpaRepository<Patient,Long> {

}
