package com.tigrbank.repository;

import com.tigrbank.domain.Operation;
import java.time.LocalDate;
import java.util.List;

public interface OperationRepository extends Repository<Operation> {
    List<Operation> findByBankAccountId(Long accountId);
    List<Operation> findByCategoryId(Long categoryId);
    List<Operation> findByDateBetween(LocalDate start, LocalDate end);
}