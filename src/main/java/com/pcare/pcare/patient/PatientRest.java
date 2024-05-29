package com.pcare.pcare.patient;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
public class PatientRest {

    private final PatientService service;

    public PatientRest(PatientService service) {
        this.service = service;
    }

    @PostMapping
    public Patient save(@RequestBody Patient patient) {
        return this.service.save(patient);
    }

    @PutMapping("/{id}")
    public Patient update(@PathVariable Integer id, @RequestBody Patient patient) {
        return this.service.update(id, patient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        this.service.delete(id);
    }

    @GetMapping
    public Collection<Patient> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/search")
    public Collection<Patient> search(@RequestParam String firstName, @RequestParam String lastName) {
        return this.service.findByFirstOrLastName(firstName, lastName);
    }

    @GetMapping("/findByProvider")
    public Collection<Patient> findByProvider(@RequestParam int providerId) {
        return this.service.findPatientsByProvider(providerId);
    }

    @GetMapping("/{id}")
    public Optional<Patient> find(@PathVariable int id) {
        return this.service.findById(id);
    }

}
