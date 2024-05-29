package com.pcare.pcare.provider;

import com.pcare.pcare.patient.Patient;
import com.pcare.pcare.utils.NotFoundException;
import com.pcare.pcare.utils.CrudService;
import com.pcare.pcare.utils.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ProviderService implements CrudService<Integer, Provider> {

    private final ProviderRepository repository;

    @Autowired
    public ProviderService(ProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Provider> findAll() {
        return this.repository.findAll();
    }
    @Override
    public Optional<Provider> findById(Integer id) {
        return this.repository.findById(id);
    }


    @Override
    public Provider save(Provider provider) {
        validate(provider);
        return this.repository.save(provider);
    }

    @Override
    public Provider update(Integer id, Provider provider) {
        checkIfExists(id);
        validate(provider);
        return this.repository.update(id, provider);
    }

    @Override
    public void delete(Integer id) {
        checkIfExists(id);
        this.repository.delete(id);
    }

    @Override
    public void validate(Provider provider) {
        boolean fnEmpty = provider.firstName() == null || provider.firstName().isEmpty();
        boolean lnEmpty = provider.lastName() == null || provider.lastName().isEmpty();
        boolean dobEmpty = provider.dob() == null;
        boolean specialityEmpty = provider.speciality() == null;
        if (fnEmpty || lnEmpty || dobEmpty || specialityEmpty) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "First name, Last name, Dob and speciality cannot be empty");
        }
    }


    private void checkIfExists(Integer id) {
        if (this.repository.findById(id).isEmpty()) {
            throw new NotFoundException("Provider with id %d not found".formatted(id));
        }
    }
}
