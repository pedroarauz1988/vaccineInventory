package com.pedro.arauz.service.impl;

import com.pedro.arauz.enums.Status;
import com.pedro.arauz.entity.Employee;
import com.pedro.arauz.entity.EmployeeVaccine;
import com.pedro.arauz.presentation.presenter.EmployeePresenter;
import com.pedro.arauz.presentation.presenter.EmployeeVaccinePresenter;
import com.pedro.arauz.repository.EmployeeRepository;
import com.pedro.arauz.service.EmployeeService;
import com.pedro.arauz.service.EmployeeVaccineService;
import com.pedro.arauz.service.UserService;
import com.pedro.arauz.service.VaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private EmployeeVaccineService employeeVaccineService;

    @Autowired
    private UserService userService;

    @Override
    public EmployeePresenter saveEmployee(EmployeePresenter employeePresenter) throws ParseException {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = formatter1.parse(employeePresenter.getDateOfBirth());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean createUser = false;
        Employee employee = new Employee();
        if (employeePresenter.getId() != null) {
            employee = employeeRepository.findById(employeePresenter.getId()).get();
        } else {
            employee.setId(UUID.randomUUID());
            createUser = true;
        }
        employee.setDni(employeePresenter.getDni());
        employee.setFirstName(employeePresenter.getFirstName());
        employee.setLastName(employeePresenter.getLastName());
        employee.setMail(employeePresenter.getMail());
        employee.setDateOfBirth(date1);
        employee.setAddress(employeePresenter.getAddress());
        employee.setPhone(employeePresenter.getPhone());
        employee.setStatus(Status.valueOf(employeePresenter.getStatus()));
        employee = employeeRepository.save(employee);
        if (!employeePresenter.getEmployeeVaccinePresenters().isEmpty()) {
            Set<EmployeeVaccine> employeeVaccines = new HashSet<>();
            employeePresenter.getEmployeeVaccinePresenters().forEach(employeeVaccinePresenter -> {
                employeeVaccines.add(employeeVaccineService.employeeVaccinePresenterToEmployeeVaccine(employeeVaccinePresenter));
            });
            Employee finalEmployee = employee;
            employeeVaccines.forEach(employeeVaccine -> {
                employeeVaccine.setEmployee(finalEmployee);
                employeeVaccineService.saveEmployeeVaccine(employeeVaccine);
            });
            employee.setEmployeeVaccines(employeeVaccines);
        }
        if (createUser) {
            userService.saveUserByEmployee(employee);
        }
        return employeeToEmployeePresenter(employee);
    }

    @Override
    public List<EmployeePresenter> findAll() {
        List<EmployeePresenter> employeePresenters = new ArrayList<>();
        employeeRepository.findAll().forEach(employee -> {
            try {
                employeePresenters.add(employeeToEmployeePresenter(employee));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return employeePresenters;
    }

    @Override
    public EmployeePresenter getEmployeeById(UUID employeeId) throws ParseException {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            return employeeToEmployeePresenter(employee.get());
        }
        return null;
    }

    @Override
    public EmployeePresenter employeeToEmployeePresenter(Employee employee) throws ParseException {
        List<EmployeeVaccinePresenter> employeeVaccinePresenter = new ArrayList<>();
        employee.getEmployeeVaccines().forEach(vaccine -> {
            employeeVaccinePresenter.add(employeeVaccineService.employeeVaccineToEmployeeVaccinePresenter(vaccine));
        });
        return EmployeePresenter.builder()
                .id(employee.getId())
                .dni(employee.getDni())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .mail(employee.getMail())
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(employee.getDateOfBirth()))
                .address(employee.getAddress())
                .phone(employee.getPhone())
                .status(employee.getStatus().name())
                .employeeVaccinePresenters(employeeVaccinePresenter)
                .build();
    }
}
