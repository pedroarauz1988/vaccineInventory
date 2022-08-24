package com.pedro.arauz.presentation.presenter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPresenter implements Comparable<UserPresenter> {
    private UUID id;
    private String userName;
    private String password;
    private String fullName;
    private String dni;
    @Builder.Default
    private List<com.pedro.arauz.presentation.presenter.RolePresenter> rolePresenters = new ArrayList<>();

    public int compareTo(UserPresenter userPresenter) {
        return this.userName.compareTo(userPresenter.userName);
    }
}
