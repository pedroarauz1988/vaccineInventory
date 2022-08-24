package com.pedro.arauz.service.impl;

import com.pedro.arauz.entity.Employee;
import com.pedro.arauz.entity.Permission;
import com.pedro.arauz.entity.Roles;
import com.pedro.arauz.entity.User;
import com.pedro.arauz.presentation.presenter.PermissionPresenter;
import com.pedro.arauz.presentation.presenter.RolePresenter;
import com.pedro.arauz.presentation.presenter.UserPresenter;
import com.pedro.arauz.repository.UserRepository;
import com.pedro.arauz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserPresenter loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            return userToUserPresenter(user.get());

        }
        return null;
    }

    @Override
    public UserPresenter saveUserByEmployee(Employee employee) {
        return userToUserPresenter(userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .employee(employee)
                .username(employee.getFirstName().replaceAll(" ", "").toLowerCase().trim() + "." + employee.getLastName().replaceAll(" ", "").toLowerCase().trim())
                .password(employee.getDni())
                .build()));
    }

    private UserPresenter userToUserPresenter(User user) {
        List<RolePresenter> rolePresenters = new ArrayList<>();
        user.getRoles().forEach(role -> {
            rolePresenters.add(roleToRolePresenter(role));
        });
        return UserPresenter.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName())
                .dni(user.getEmployee().getDni())
                .rolePresenters(rolePresenters)
                .build();

    }

    private RolePresenter roleToRolePresenter(Roles role) {
        List<PermissionPresenter> permissionPresenters = new ArrayList<>();
        role.getPermissions().forEach(permission -> {
            permissionPresenters.add(permissionToPermissionPresenter(permission));
        });
        return RolePresenter.builder()
                .id(role.getId())
                .name(role.getName())
                .permissionPresenters(permissionPresenters)
                .build();
    }

    private PermissionPresenter permissionToPermissionPresenter(Permission permission) {
        return PermissionPresenter.builder()
                .id(permission.getId())
                .domainAction(permission.getDomainAction())
                .name(permission.getName())
                .build();
    }


}