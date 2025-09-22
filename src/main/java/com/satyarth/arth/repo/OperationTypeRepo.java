package com.satyarth.arth.repo;

import com.satyarth.arth.models.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTypeRepo extends JpaRepository<OperationType, Long> {
}
