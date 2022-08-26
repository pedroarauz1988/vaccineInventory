package com.pedro.arauz.service;

import com.pedro.arauz.entity.Employee;
import com.pedro.arauz.presentation.presenter.EmployeePresenter;
import com.pedro.arauz.presentation.presenter.Paginator;
import com.sun.istack.NotNull;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeePresenter saveEmployee(EmployeePresenter employeePresenter) throws ParseException;

    List<EmployeePresenter> findAll();

    EmployeePresenter getEmployeeById(UUID employeeId) throws ParseException;

    EmployeePresenter employeeToEmployeePresenter(Employee workOrder) throws ParseException;

    String delete(UUID id);

    Paginator getPaginatedEmployees(String searchValue, Date initDate, Date endDate, String status, Integer page, Integer size);

    List<EmployeePresenter> getEmployeeByStatus(String status);


}
