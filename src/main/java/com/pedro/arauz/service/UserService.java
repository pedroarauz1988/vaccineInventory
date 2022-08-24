package com.pedro.arauz.service;


import com.pedro.arauz.entity.Employee;
import com.pedro.arauz.presentation.presenter.UserPresenter;

public interface UserService {

    UserPresenter loginUser(String userName, String password);

    UserPresenter saveUserByEmployee(Employee employee);

}
