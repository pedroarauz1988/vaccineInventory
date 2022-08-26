package com.pedro.arauz.repository;

import com.pedro.arauz.entity.EmployeeVaccine;
import com.pedro.arauz.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeVaccineRepository extends CrudRepository<EmployeeVaccine, UUID> {

    Optional<EmployeeVaccine> findByEmployeeId(UUID id);

}
