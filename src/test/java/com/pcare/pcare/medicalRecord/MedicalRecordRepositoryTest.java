package com.pcare.pcare.medicalRecord;

import com.pcare.pcare.patient.PatientRepository;
import com.pcare.pcare.patient.Patient;
import com.pcare.pcare.provider.Provider;
import com.pcare.pcare.provider.ProviderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
class MedicalRecordRepositoryTest {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ProviderRepository providerRepository;


    @AfterEach
    void tearDown() {
        this.medicalRecordRepository.deleteAll();
    }

    @Test
    void findByPatientIdTest() {
        Patient newPatient = new Patient(null, "YuliaAA", "Kr", LocalDate.of(2004, 12, 26), "yulia@gmail.com", "+380966756456", null);
        Patient savedPatient = this.patientRepository.save(newPatient);

        MedicalRecord medicalRecord = new MedicalRecord(null, savedPatient.id(), LocalDate.now(), "A15-A19", 100, null);
        this.medicalRecordRepository.save(medicalRecord);

        Collection<MedicalRecord> foundByIdMedRec = this.medicalRecordRepository.findByPatientId(savedPatient.id());

        Assertions.assertFalse(foundByIdMedRec.isEmpty(), "Medical record should be found");

        MedicalRecord retrievedMedicalRecord = foundByIdMedRec.stream()
                .filter(record -> record.patientId().equals(savedPatient.id()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(retrievedMedicalRecord, "Retrieved medical record should not be null");
        Assertions.assertEquals(savedPatient.id(), retrievedMedicalRecord.patientId(), "Patient ID should match");
        Assertions.assertEquals(LocalDate.now(), retrievedMedicalRecord.creationDate(), "Creation date should match");
        Assertions.assertEquals("A15-A19", retrievedMedicalRecord.medicalCode(), "Medical code should match");
    }

    @Test
    void testSave() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        LocalDate dobProvider = LocalDate.of(2004, 12, 26);
        Patient pat = this.patientRepository.save(new Patient(null, "Yulia", "Kr", dob, "yulia@gmail.com", "+380966756456", null));
        Provider newProvider = this.providerRepository.save(new Provider(null, "John", "Smith", dobProvider, "yulia@gmail.com", "+380966756456", "surgeon"));

        MedicalRecord medicalRecord = new MedicalRecord(null, pat.id(), LocalDate.now(), "A15-A19", newProvider.id(), null);
        MedicalRecord savedMedRec = this.medicalRecordRepository.save(medicalRecord);

        Collection<MedicalRecord> getMedRecord = this.medicalRecordRepository.findByPatientId(pat.id());
        Assertions.assertFalse(getMedRecord.isEmpty());

        MedicalRecord fetchedMedicalRecord = getMedRecord.stream()
                .filter(record -> record.id().equals(savedMedRec.id()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(fetchedMedicalRecord);
        Assertions.assertEquals(savedMedRec.id(), fetchedMedicalRecord.id());
        Assertions.assertEquals(savedMedRec.providerId(), fetchedMedicalRecord.providerId());
        Assertions.assertEquals(savedMedRec.patientId(), fetchedMedicalRecord.patientId());
        Assertions.assertEquals(savedMedRec.medicalCode(), fetchedMedicalRecord.medicalCode());
    }

    @Test
    void updateMedRecordTest() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        Patient newPatient = new Patient(null, "Yulia", "Kr", dob, "yulia@gmail.com", "+380966756456", null);
        Patient savedPatient = this.patientRepository.save(newPatient);

        LocalDate dobProvider = LocalDate.of(2004, 12, 26);
        Provider newProvider = new Provider(null, "John", "Smith", dobProvider, "john.smith@gmail.com", "+380966756457", "surgeon");
        Provider savedProvider = this.providerRepository.save(newProvider);

        MedicalRecord medicalRecord = new MedicalRecord(null, savedPatient.id(), LocalDate.now(), "A15-A19", savedProvider.id(), "Initial notes");
        MedicalRecord savedMedRec = this.medicalRecordRepository.save(medicalRecord);

        MedicalRecord updatedMedicalRecord = new MedicalRecord(savedMedRec.id(), savedMedRec.patientId(), savedMedRec.creationDate(), "A15-A20", savedMedRec.providerId(), "Updated notes");
        this.medicalRecordRepository.update(savedMedRec.id(), updatedMedicalRecord);

        Collection<MedicalRecord> updatedMedRecords = this.medicalRecordRepository.findByPatientId(savedPatient.id());
        Assertions.assertFalse(updatedMedRecords.isEmpty());

        MedicalRecord fetchedUpdatedMedicalRecord = updatedMedRecords.stream()
                .filter(record -> record.id().equals(savedMedRec.id()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(fetchedUpdatedMedicalRecord);
        Assertions.assertEquals(savedMedRec.id(), fetchedUpdatedMedicalRecord.id());
        Assertions.assertEquals(savedMedRec.patientId(), fetchedUpdatedMedicalRecord.patientId());
        Assertions.assertEquals(savedMedRec.providerId(), fetchedUpdatedMedicalRecord.providerId());
        Assertions.assertEquals("A15-A20", fetchedUpdatedMedicalRecord.medicalCode());
        Assertions.assertEquals("Updated notes", fetchedUpdatedMedicalRecord.notes());
    }


    @Test
    void testDelete() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        LocalDate dobProvider = LocalDate.of(2004, 12, 26);
        Patient newPatient = new Patient(null, "Yulia", "Kr", dob, "yulia@gmail.com", "+380966756456", null);
        Patient savedPatient = this.patientRepository.save(newPatient);
        Provider newProvider = this.providerRepository.save(new Provider(null, "John", "Smith", dobProvider, "yulia@gmail.com", "+380966756456", "surgeon"));

        MedicalRecord newMedicalRecord = new MedicalRecord(null, savedPatient.id(), LocalDate.now(), "A15-A19", newProvider.id(), null);
        this.medicalRecordRepository.save(newMedicalRecord);
        Collection<MedicalRecord> getMedicalRecord = this.medicalRecordRepository.findByPatientId(savedPatient.id());
        Assertions.assertFalse(getMedicalRecord.isEmpty());

        this.medicalRecordRepository.deleteByPatientId(savedPatient.id());
        Collection<MedicalRecord> getDeletedMedicalRecord = this.medicalRecordRepository.findByPatientId(savedPatient.id());
        Assertions.assertTrue(getDeletedMedicalRecord.isEmpty());

    }

    @Test
    void testFindAll() {
        Collection<MedicalRecord> all = this.medicalRecordRepository.findAll();
        Assertions.assertEquals(0, all.size());

        IntStream.range(1, 11).forEach(i -> {
            LocalDate dob = LocalDate.of(2004, 12, i);
            MedicalRecord medicalRecord = new MedicalRecord(null, i, dob, "AA104-AA111", i, null);
            this.medicalRecordRepository.save(medicalRecord);
        });

        Collection<MedicalRecord> allSecond = this.medicalRecordRepository.findAll();
        Assertions.assertEquals(10, allSecond.size());
    }

}
