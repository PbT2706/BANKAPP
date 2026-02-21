package com.pratham.banking.repository;

import com.pratham.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Transaction entity.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
