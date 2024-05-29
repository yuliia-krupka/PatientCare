package com.pcare.pcare.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PatientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Patient> findById(Integer id) {
        if (id != null) {
            try {
                Patient patient = this.jdbcTemplate.queryForObject("SELECT * FROM patient WHERE Id = ?", new Object[]{id}, (rs, rowNum) -> {
                    Date date = rs.getDate(4);
                    return new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), toLocalDate(date), rs.getString(5), rs.getString(6), rs.getObject(7, Integer.class));
                });
                return Optional.of(patient);
            } catch (EmptyResultDataAccessException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public Patient update(int id, Patient patient) {
        this.jdbcTemplate.update(
                "UPDATE patient SET FirstName = ?, LastName = ?, DoB = ?, Email = ?, Phone = ?, ProviderId = ? WHERE Id = ?",
                patient.firstName(), patient.lastName(), patient.dob(), patient.email(), patient.phone(), patient.providerId(), id
        );
        return new Patient(patient.id(), patient.firstName(), patient.lastName(), patient.dob(), patient.email(), patient.phone(), patient.providerId());
    }


    public void delete(int id) {
        this.jdbcTemplate.update("DELETE FROM patient WHERE Id = ?", id);
    }


    public Collection<Patient> findAll() {
        return this.jdbcTemplate.query("SELECT * FROM patient", getPatientRowMapper());
    }

    private static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Patient save(Patient patient) {
        this.jdbcTemplate.update("INSERT INTO patient(FirstName, LastName, DoB, Email, Phone, ProviderId) VALUES (?, ?, ?, ?, ?, ?)", patient.firstName(), patient.lastName(), patient.dob(), patient.email(), patient.phone(), patient.providerId());

        return this.jdbcTemplate.query("select * from patient ORDER BY Id DESC LIMIT 1", rs -> {
            rs.next();
            final var date = rs.getDate(4);
            return new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), toLocalDate(date), rs.getString(5), rs.getString(6), rs.getObject(7, Integer.class));
        });
    }

    public void deleteAll() {
        this.jdbcTemplate.update("DELETE FROM patient");
    }

    Collection<Patient> findByFirstOrLastName(String firstName, String lastName) {
        return this.jdbcTemplate.query(
                "SELECT * FROM patient WHERE firstName LIKE ? AND lastName LIKE ?",
                getPatientRowMapper(),
                "%" + firstName + "%", "%" + lastName + "%"
        );
    }

    public List<Patient> findPatientsByProvider(int providerId) {
        return this.jdbcTemplate.query(
                "SELECT * FROM patient WHERE ProviderId = ?",
                getPatientRowMapper(),
                providerId
        );
    }


    private static RowMapper<Patient> getPatientRowMapper() {
        return (rs, rowNum) -> new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), toLocalDate(rs.getDate(4)), rs.getString(5), rs.getString(6), rs.getObject(7, Integer.class));
    }

}
