package com.pedro.arauz.service.impl;

import com.pedro.arauz.enums.Status;
import com.pedro.arauz.entity.Employee;
import com.pedro.arauz.entity.EmployeeVaccine;
import com.pedro.arauz.presentation.presenter.EmployeePresenter;
import com.pedro.arauz.presentation.presenter.EmployeeVaccinePresenter;
import com.pedro.arauz.presentation.presenter.Paginator;
import com.pedro.arauz.repository.EmployeeRepository;
import com.pedro.arauz.service.EmployeeService;
import com.pedro.arauz.service.EmployeeVaccineService;
import com.pedro.arauz.service.UserService;
import com.pedro.arauz.service.VaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        employee.setActive(employeePresenter.getActive());
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
        employeeRepository.findByActiveTrue().forEach(employee -> {
            try {
                employeePresenters.add(employeeToEmployeePresenter(employee.get()));
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
                .active(employee.getActive())
                .employeeVaccinePresenters(employeeVaccinePresenter)
                .build();
    }

    @Override
    public String delete(UUID id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employee.get().setActive(false);
            employeeRepository.save(employee.get());
            return "deleted correctly";
        }
        return "employee not found";
    }

    @Override
    public Paginator getPaginatedEmployees(String searchValue, Date initDate, Date endDate, String status, Integer page, Integer size) {

        Pageable pageable = null;
        Page<Employee> employeePages = null;
        List<EmployeePresenter> employeePresenters = new ArrayList<>();
        Status[] vaccineStatus = status.isEmpty() ? new Status[]{Status.VACUNADO, Status.NO_VACUNADO} : status.equals("VACUNADO") ? new Status[]{Status.VACUNADO} : new Status[]{Status.NO_VACUNADO};
        pageable = PageRequest.of(page, size);
        employeePages = employeeRepository.findByFilters(searchValue, initDate, endDate, vaccineStatus, pageable);
        employeePages.getContent().forEach(employee -> {
            employeePresenters.add(EmployeePresenter.builder()
                            .id(employee.getId())
                            .active(employee.getActive())
                            .address(employee.getAddress())
                            .dni(employee.getDni())
                            .dateOfBirth(employee.getDateOfBirth().toString())
                            .firstName(employee.getFirstName())
                            .lastName(employee.getLastName())
                            .phone(employee.getPhone())
                            .status(employee.getStatus().toString())
                    .build());
        });

        Set<EmployeePresenter> treeSet = new TreeSet<>(new Comparator<EmployeePresenter>() {
            @Override
            public int compare(EmployeePresenter employeePresenter, EmployeePresenter t1) {
                return employeePresenter.getActive().compareTo(t1.getActive()) * -1;
            }
        });
        treeSet.addAll(employeePresenters);
        Paginator paginatorEmployeePresenter = new Paginator(employeePages.getTotalPages(), employeePages.getTotalElements(), treeSet);

        return paginatorEmployeePresenter;
    }

    @Override
    public List<EmployeePresenter> getEmployeeByStatus(String status) {
        List<EmployeePresenter> employeesPresenter = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        Status[] vaccineStatus = status.isEmpty() ? new Status[]{Status.VACUNADO, Status.NO_VACUNADO} : status.equals("VACUNADO") ? new Status[]{Status.VACUNADO} : new Status[]{Status.NO_VACUNADO};
        employees = employeeRepository.findByStatus(vaccineStatus);
        employees.stream().forEach(employee -> {
            EmployeePresenter employeePresenter = new EmployeePresenter();
            try {
                employeePresenter = employeeToEmployeePresenter(employee);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            employeesPresenter.add(employeePresenter);
        });
        return employeesPresenter;
    }
}
