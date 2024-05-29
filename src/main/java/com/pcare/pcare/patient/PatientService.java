package com.pcare.pcare.patient;

import com.pcare.pcare.utils.NotFoundException;
import com.pcare.pcare.utils.CrudService;
import com.pcare.pcare.utils.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService implements CrudService<Integer, Patient> {

    private final PatientRepository repository;

    @Autowired
    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Patient> findById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public Collection<Patient> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Patient save(Patient patient) {
        validate(patient);
        return this.repository.save(patient);
    }

    @Override
    public Patient update(Integer id, Patient object) {
        checkIfExists(id);
        validate(object);
        return this.repository.update(id, object);
    }

    @Override
    public void delete(Integer id) {
        checkIfExists(id);
        this.repository.delete(id);
    }

    @Override
    public void validate(Patient patient) {
        boolean fnEmpty = patient.firstName() == null || patient.firstName().isEmpty();
        boolean lnEmpty = patient.lastName() == null || patient.lastName().isEmpty();
        boolean dobEmpty = patient.dob() == null;
        if (fnEmpty || lnEmpty || dobEmpty) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "First name, Last name and Dob cannot be empty");
        }
    }

    public Collection<Patient> findByFirstOrLastName(String firstName, String lastName) {
        return this.repository.findByFirstOrLastName(firstName, lastName);
    }

    public List<Patient> findPatientsByProvider(int providerId) {
        return this.repository.findPatientsByProvider(providerId);
    }

    private void checkIfExists(Integer id) {
        if (this.repository.findById(id).isEmpty()) {
            throw new NotFoundException("Patient with id %d not found".formatted(id));
        }
    }
}
