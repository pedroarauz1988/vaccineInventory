package com.pedro.arauz.repository;

import com.pedro.arauz.entity.Employee;
import com.pedro.arauz.entity.EmployeeVaccine;
import com.pedro.arauz.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, UUID> {
    @Query("SELECT e " +
            "FROM Employee e " +
            "left JOIN e.employeeVaccines ev " +
            "WHERE (LOWER(e.dni) like LOWER(CONCAT('%',:searchValue,'%')) " +
            "OR LOWER(e.firstName) like LOWER(CONCAT('%',:searchValue,'%')) " +
            "OR LOWER(e.lastName) like LOWER(CONCAT('%',:searchValue,'%'))) " +
            "AND (cast(ev.date as string) between coalesce(cast(:initDate as string), cast(ev.date as string)) " +
            "and coalesce(cast(:endDate as string), cast(ev.date as string))) " +
            "AND e.status in :status ")
    Page<Employee> findByFilters(@Param("searchValue") String searchValue,
                                 @Param("initDate") Date initDate,
                                 @Param("endDate") Date endDate,
                                 @Param("status") Status[] status,
                                 Pageable pageable);

    List<Optional<Employee>> findByActiveTrue();

    @Query("SELECT e " +
            " FROM Employee e " +
            " WHERE e.status in :status")
    List<Employee> findByStatus(Status[] status);

}
