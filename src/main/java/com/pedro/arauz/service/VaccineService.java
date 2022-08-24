package com.pedro.arauz.service;

import com.pedro.arauz.entity.Vaccine;
import com.pedro.arauz.presentation.presenter.VaccinePresenter;

import java.util.List;
import java.util.UUID;

public interface VaccineService {

    VaccinePresenter vaccineToVaccinePresenter(Vaccine vaccine);

    Vaccine vaccinePresenterToVaccine(VaccinePresenter vaccinePresenter);

    List<VaccinePresenter> getVaccines();

    Vaccine findById(UUID id);
}
