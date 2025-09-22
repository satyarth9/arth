package com.satyarth.arth.seeder;

import com.satyarth.arth.models.OperationType;
import com.satyarth.arth.repo.OperationTypeRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationTypeSeeder {

    private final OperationTypeRepo operationTypeRepo;

    public OperationTypeSeeder(OperationTypeRepo operationTypeRepo) {
        this.operationTypeRepo = operationTypeRepo;
    }

    @PostConstruct
    public void seedOperationTypes(){
        if (operationTypeRepo.count() == 0){
            operationTypeRepo.saveAll(List.of(
                    new OperationType(1L, "CASH PURCHASE", Boolean.FALSE),
                    new OperationType(2L, "INSTALLMENT PURCHASE", Boolean.FALSE),
                    new OperationType(3L, "WITHDRAWAL", Boolean.FALSE),
                    new OperationType(4L, "PAYMENT", Boolean.TRUE)
            ));
        }
    }
}
