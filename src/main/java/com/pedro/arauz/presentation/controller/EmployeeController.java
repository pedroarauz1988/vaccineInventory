package com.pedro.arauz.presentation.controller;

import com.pedro.arauz.entity.Employee;
import com.pedro.arauz.presentation.presenter.EmployeePresenter;
import com.pedro.arauz.presentation.presenter.Paginator;
import com.pedro.arauz.service.EmployeeService;
import com.sun.istack.NotNull;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Generated
@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/saveEmployee")
    public EmployeePresenter saveEmployee(@RequestBody @NotNull EmployeePresenter employeePresenter) throws ParseException {
        return employeeService.saveEmployee(employeePresenter);
    }

    @GetMapping("/findAll")
    public List<EmployeePresenter> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/getEmployeeById")
    public EmployeePresenter getEmployeeById(@RequestParam("employeeId") UUID employeeId) throws ParseException {
        return employeeService.getEmployeeById(employeeId);
    }

    @GetMapping("/deleteEmployee")
    public String saveEmployee(@RequestParam @NotNull String id) {
        return employeeService.delete(UUID.fromString(id));
    }

    @GetMapping("/employee/search")
    public Paginator searchOrders(@RequestParam(value = "searchValue", required = false) String searchValue,
                                  @RequestParam(value = "initDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date initDate,
                                  @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                  @RequestParam(value = "status", required = false) String status,
                                  @RequestParam("page") Integer page,
                                  @RequestParam("size") Integer size) {
        return employeeService.getPaginatedEmployees(searchValue, initDate, endDate, status, page, size);
    }

    @GetMapping("/findByStatus")
    public List<EmployeePresenter> getByStatus(@RequestParam(value = "status", required = false) String status) {
        return employeeService.getEmployeeByStatus(status);
    }

}
