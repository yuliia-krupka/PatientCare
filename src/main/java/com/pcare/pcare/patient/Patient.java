package com.pcare.pcare.patient;

import java.time.LocalDate;

public record Patient(
        Integer id,
        String firstName,
        String lastName,
        LocalDate dob,
        String email,
        String phone,
        Integer providerId

) {
}
