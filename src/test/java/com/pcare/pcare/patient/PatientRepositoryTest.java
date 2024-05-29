package com.pcare.pcare.patient;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @AfterEach
    void tearDown() {
        this.patientRepository.deleteAll();
    }

    @Test
    void testCreateNewPatient() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        Patient newPatient = new Patient(null, "Yulia", "Kr", dob, "yulia@gmail.com", "+380966756456", null);
        Patient savedPatient = this.patientRepository.save(newPatient);
        assertNotNull(savedPatient, "YULIA Patient can not be NULL");
        assertNotNull(savedPatient.id(), "Patient id is required");
        assertEquals(dob, savedPatient.dob());
    }

    @Test
    void testFindAll() {
        Collection<Patient> all = this.patientRepository.findAll();
        assertEquals(0, all.size());

        IntStream.range(1, 10).forEach(i -> {
            LocalDate dob = LocalDate.of(2004, 12, i);
            Patient newPatient = new Patient(null, "Yulia_" + i, "Kr", dob,  "yulia@gmail.com", "+380966756456", null);
            this.patientRepository.save(newPatient);
        });

        Collection<Patient> allSecond = this.patientRepository.findAll();
        assertEquals(9, allSecond.size());
    }

    @Test
    void testFindById() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        Patient newPatient = new Patient(null, "Vika", "Kr", dob,  "yulia@gmail.com", "+380966756456", null);
        Patient savedPatient = this.patientRepository.save(newPatient);
        Optional<Patient> foundByIdPatient = this.patientRepository.findById(savedPatient.id());
        assertEquals(savedPatient, foundByIdPatient.get());
    }


    @Test
    void testDelete() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        Patient newPatient = new Patient(null, "Vika", "Kr", dob, "yulia@gmail.com", "+380966756456", null);
        Patient savedPatient = this.patientRepository.save(newPatient);
        this.patientRepository.delete(savedPatient.id());
        Optional<Patient> deletedPatient = this.patientRepository.findById(savedPatient.id());
        Assertions.assertFalse(deletedPatient.isPresent());
    }


    @Test
    void testUpdate() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        Patient newPatient = new Patient(null, "Yulia", "Kr", dob, "yulia@gmail.com", "+380966756456", null);
        Patient savedPatient = this.patientRepository.save(newPatient);

        Patient secondPatient = new Patient(savedPatient.id(), savedPatient.firstName(), "Smith", savedPatient.dob(), savedPatient.email(), savedPatient.phone(), savedPatient.providerId());

        Patient updatedPatient = this.patientRepository.update(savedPatient.id(), secondPatient);

        assertNotNull(updatedPatient);
        assertEquals(savedPatient.id(), updatedPatient.id());
        assertEquals(savedPatient.firstName(), updatedPatient.firstName());
        assertNotEquals(savedPatient.lastName(), updatedPatient.lastName());
        assertEquals(savedPatient.dob(), updatedPatient.dob());
    }

    @Test
    void testDeleteAll() {
        Collection<Patient> all = this.patientRepository.findAll();
        assertEquals(0, all.size());

        IntStream.range(1, 10).forEach(i -> {
            LocalDate dob = LocalDate.of(2004, 12, i);
            Patient newPatient = new Patient(null, "Sam_" + i, "Williams", dob, "sam@gmail.com", "+380966778756", null);
            this.patientRepository.save(newPatient);
        });

        Collection<Patient> allSecond = this.patientRepository.findAll();
        assertEquals(9, allSecond.size());

        this.patientRepository.deleteAll();

        Collection<Patient> allAfterDelete = this.patientRepository.findAll();
        assertEquals(0, allAfterDelete.size(), "Size after deleteAll should be zero");
    }

}
