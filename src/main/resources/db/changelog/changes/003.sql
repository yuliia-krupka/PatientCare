CREATE TABLE MedicalRecord
(
    id           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    patientId    INT          NOT NULL,
    creationDate DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    medicalCode  VARCHAR(255) NOT NULL,
    providerId   INT          NOT NULL,
    notes        VARCHAR(500)
);
