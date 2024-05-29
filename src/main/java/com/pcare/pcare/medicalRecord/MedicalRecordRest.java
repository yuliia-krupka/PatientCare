package com.pcare.pcare.medicalRecord;

import com.pcare.pcare.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordRest {

    private final MedicalRecordService service;

    @Autowired
    public MedicalRecordRest(MedicalRecordService service) {
        this.service = service;
    }

    @PostMapping
    public MedicalRecord save(@RequestBody MedicalRecord medicalRecord) {
        return this.service.save(medicalRecord);
    }

    @PutMapping("/{id}")
    public MedicalRecord update(@PathVariable int id, @RequestBody MedicalRecord medicalRecord) {
        Collection<MedicalRecord> existingRecords = this.service.findByPatientId(id);
        if (existingRecords.isEmpty()) {
            throw new NotFoundException("Medical records not found");
        }
        return this.service.update(id, medicalRecord);
    }

    @DeleteMapping("/patient/{patientId}")
    public void deleteByPatientId(@PathVariable Integer patientId) {
        Collection<MedicalRecord> existingRecords = this.service.findByPatientId(patientId);
        if (existingRecords.isEmpty()) {
            throw new NotFoundException("Medical records not found");
        }
        this.service.deleteByPatient(patientId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        this.service.delete(id);
    }

    @GetMapping("/patient/{id}")
    public Collection<MedicalRecord> findByPatientId(@PathVariable int id) {
        return this.service.findByPatientId(id);
    }

    @GetMapping("/{id}")
    public Optional<MedicalRecord> find(@PathVariable int id) {
        return this.service.findById(id);
    }

}
