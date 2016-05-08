package com.fangzhou.manatee.repository;

import com.fangzhou.manatee.domain.Queue;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Queue entity.
 */
@SuppressWarnings("unused")
public interface QueueRepository extends JpaRepository<Queue,Long> {

}
