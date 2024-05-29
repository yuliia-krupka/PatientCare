package com.pcare.pcare.provider;

import java.time.LocalDate;

public record Provider(
        Integer id,
        String firstName,
        String lastName,
        LocalDate dob,
        String email,
        String phone,
        String speciality
) {

}
