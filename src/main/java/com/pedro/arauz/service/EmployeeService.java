package com.pedro.arauz.service;

import com.pedro.arauz.entity.Employee;
import com.pedro.arauz.presentation.presenter.EmployeePresenter;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeePresenter saveEmployee(EmployeePresenter employeePresenter) throws ParseException;

    List<EmployeePresenter> findAll();

    EmployeePresenter getEmployeeById(UUID employeeId) throws ParseException;

    EmployeePresenter employeeToEmployeePresenter(Employee workOrder) throws ParseException;


}
