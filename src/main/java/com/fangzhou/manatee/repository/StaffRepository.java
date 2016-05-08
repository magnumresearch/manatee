package com.fangzhou.manatee.repository;

import com.fangzhou.manatee.domain.Staff;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Staff entity.
 */
@SuppressWarnings("unused")
public interface StaffRepository extends JpaRepository<Staff,Long> {

}
