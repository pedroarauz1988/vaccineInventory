package com.pedro.arauz.service;

import com.pedro.arauz.entity.EmployeeVaccine;
import com.pedro.arauz.presentation.presenter.EmployeeVaccinePresenter;

import java.util.UUID;

public interface EmployeeVaccineService {
    EmployeeVaccine saveEmployeeVaccine(EmployeeVaccine employeeVaccine);

    String delete(UUID id);

    public EmployeeVaccine employeeVaccinePresenterToEmployeeVaccine(EmployeeVaccinePresenter employeeVaccinePresenter);

    EmployeeVaccinePresenter employeeVaccineToEmployeeVaccinePresenter(EmployeeVaccine employeeVaccine);
}
