package com.pcare.pcare.medicalRecord;

import com.pcare.pcare.utils.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordRest {

    private final MedicalRecordRepository repository;

    public MedicalRecordRest(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public MedicalRecord save(@RequestBody MedicalRecord medicalRecord) {
        return this.repository.save(medicalRecord);
    }

    @PutMapping("/{id}")
    public MedicalRecord update(@PathVariable int id, @RequestBody MedicalRecord medicalRecord) {
        Collection<MedicalRecord> existingRecords = this.repository.findByPatientId(id);
        if (existingRecords.isEmpty()) {
            throw new NotFoundException("Medical records not found");
        }
        return this.repository.update(id, medicalRecord);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        Collection<MedicalRecord> existingRecords = this.repository.findByPatientId(id);
        if (existingRecords.isEmpty()) {
            throw new NotFoundException("Medical records not found");
        }
        this.repository.deleteByPatientId(id);
    }

    @GetMapping("/patient/{id}")
    public Collection<MedicalRecord> findByPatientId(@PathVariable int id) {
        return this.repository.findByPatientId(id);
    }

    @GetMapping("/{id}")
    public Optional<MedicalRecord> find(@PathVariable int id) {
        return this.repository.findById(id);
    }

}
