package com.pedro.arauz.presentation.controller;

import com.pedro.arauz.presentation.presenter.EmployeePresenter;
import com.pedro.arauz.service.EmployeeService;
import com.sun.istack.NotNull;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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

}
