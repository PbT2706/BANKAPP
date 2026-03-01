package com.pratham.banking.repository;

import com.pratham.banking.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for IdempotencyRecord entity.
 */
public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord, String> {
}
