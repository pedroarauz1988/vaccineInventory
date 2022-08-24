package com.pedro.arauz.presentation.controller;

import com.pedro.arauz.presentation.presenter.EmployeeVaccinePresenter;
import com.pedro.arauz.service.EmployeeVaccineService;
import com.sun.istack.NotNull;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Generated
@RestController
public class EmployeeVaccineController {

    @Autowired
    private EmployeeVaccineService employeeVaccineService;

    @PostMapping("/saveVaccine")
    public EmployeeVaccinePresenter saveVaccine(@RequestBody @NotNull EmployeeVaccinePresenter employeeVaccinePresenter) {
        return employeeVaccineService.employeeVaccineToEmployeeVaccinePresenter(employeeVaccineService.saveEmployeeVaccine(employeeVaccineService.employeeVaccinePresenterToEmployeeVaccine(employeeVaccinePresenter)));
    }

    @PostMapping("/deleteEmployeeVaccine")
    public String saveEmployee(@RequestBody @NotNull String id) {
        return employeeVaccineService.delete(UUID.fromString(id));
    }

}
