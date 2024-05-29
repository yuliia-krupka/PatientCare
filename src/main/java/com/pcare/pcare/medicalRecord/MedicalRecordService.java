package com.pcare.pcare.medicalRecord;

import com.pcare.pcare.patient.PatientRepository;
import com.pcare.pcare.utils.NotFoundException;
import com.pcare.pcare.utils.CrudService;
import com.pcare.pcare.utils.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@Service
public class MedicalRecordService implements CrudService<Integer, MedicalRecord> {

    private final MedicalRecordRepository repository;
    private final PatientRepository patientRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository repository, PatientRepository patientRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
    }


    @Override
    public Optional<MedicalRecord> findById(Integer integer) {
        return Optional.empty();
    }
    public Collection<MedicalRecord> findByPatientId(Integer id) {
        checkIfExists(id);
        return this.repository.findByPatientId(id);
    }


    @Override
    public Collection<MedicalRecord> findAll() {
        return this.repository.findAll();
    }


    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        validate(medicalRecord);
        return this.repository.save(medicalRecord);
    }

    @Override
    public MedicalRecord update(Integer id, MedicalRecord medicalRecord) {
        checkIfExists(medicalRecord.id());
        checkIfExists(id);
        validate(medicalRecord);
        return this.repository.update(id, medicalRecord);
    }


    @Override
    public void delete(Integer id) {
        checkIfExists(id);
        this.repository.deleteByPatientId(id);
    }

    @Override
    public void validate(MedicalRecord medicalRecord) {
        checkIfPatientExists(medicalRecord.patientId());
        boolean mdEmpty = medicalRecord.medicalCode() == null || medicalRecord.medicalCode().isEmpty();
        boolean patientIdEmpty = medicalRecord.patientId() == null;
        boolean providerIdEmpty = medicalRecord.providerId() == null;
        boolean dobEmpty = medicalRecord.creationDate() == null;
        if (mdEmpty || patientIdEmpty || providerIdEmpty || dobEmpty) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "First name, Last name and Dob cannot be empty");
        }
    }


    private void checkIfExists(Integer id) {
        if (this.repository.findByPatientId(id).isEmpty()) {
            throw new NotFoundException("Medical record with id %d not found".formatted(id));
        }
    }

    private void checkIfPatientExists(Integer id) {
        if (this.patientRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Medical record with id %d not found".formatted(id));
        }
    }
}
