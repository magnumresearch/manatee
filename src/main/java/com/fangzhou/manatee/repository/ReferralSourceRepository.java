package com.fangzhou.manatee.repository;

import com.fangzhou.manatee.domain.ReferralSource;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReferralSource entity.
 */
@SuppressWarnings("unused")
public interface ReferralSourceRepository extends JpaRepository<ReferralSource,Long> {

}
