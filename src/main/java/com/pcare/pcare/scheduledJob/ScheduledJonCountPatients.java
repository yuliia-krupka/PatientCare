package com.pcare.pcare.scheduledJob;

import com.pcare.pcare.patient.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJonCountPatients {

    private final PatientRepository patientRepository;

    @Autowired
    public ScheduledJonCountPatients(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Scheduled(initialDelay = 50000, fixedRate = 10000)
    public void countPatients() {
        long patientCount = this.patientRepository.findAll().size();
        System.out.println("TOTAL NUMBER OF PATIENTS: " + patientCount);
    }
}
