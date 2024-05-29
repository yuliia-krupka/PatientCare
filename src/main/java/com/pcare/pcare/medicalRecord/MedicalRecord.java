package com.pcare.pcare.medicalRecord;
import java.time.LocalDate;

public record MedicalRecord(
        Integer id,
        Integer patientId,
        LocalDate creationDate,
        String medicalCode,
        Integer providerId,
        String notes

) {
}
