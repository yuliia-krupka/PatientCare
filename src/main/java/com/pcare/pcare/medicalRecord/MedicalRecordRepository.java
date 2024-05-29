package com.pcare.pcare.medicalRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public class MedicalRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MedicalRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MedicalRecord save( MedicalRecord medicalRecord) {
        this.jdbcTemplate.update("INSERT INTO MedicalRecord(patientId, creationDate, medicalCode, providerId, notes) VALUES (?, ?, ?, ?, ?)", medicalRecord.patientId(), LocalDateTime.now(), medicalRecord.medicalCode(), medicalRecord.providerId(), medicalRecord.notes());

        return this.jdbcTemplate.query("select * from MedicalRecord ORDER BY Id DESC LIMIT 1", rs -> {
            rs.next();
            final var date = rs.getDate(3);
            return new MedicalRecord(rs.getInt(1), rs.getInt(2), toLocalDate(date), rs.getString(4), rs.getInt(5), rs.getString(6));
        });
    }

    public Optional<MedicalRecord> findById(Integer id) {
        if (id != null) {
            try {
                MedicalRecord medicalRecord = this.jdbcTemplate.queryForObject("SELECT * FROM MedicalRecord WHERE id = ?", new Object[]{id}, (rs, rowNum) -> {
                    Date date = rs.getDate(3);
                    return new MedicalRecord(rs.getInt(1), rs.getInt(2), toLocalDate(date), rs.getString(4), rs.getInt(5), rs.getString(6));
                });
                return Optional.of(medicalRecord);
            } catch (EmptyResultDataAccessException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
    public Collection<MedicalRecord> findByPatientId(Integer id) {
        if (id == null) {
            return new ArrayList<>();
        }
        try {
            List<MedicalRecord> medicalRecords = this.jdbcTemplate.query(
                    "SELECT * FROM MedicalRecord WHERE patientId = ?",
                    new Object[]{id},
                    (rs, rowNum) -> {
                        Date date = rs.getDate(3);
                        return new MedicalRecord(
                                rs.getInt(1),
                                rs.getInt(2),
                                toLocalDate(date),
                                rs.getString(4),
                                rs.getInt(5),
                                rs.getString(6)
                        );
                    }
            );
            return medicalRecords;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public MedicalRecord update(int id, MedicalRecord medicalRecord) {
        this.jdbcTemplate.update(
                "UPDATE MedicalRecord SET patientId = ?, creationDate = ?, medicalCode = ?, providerId = ?, notes = ? WHERE id = ?",
                medicalRecord.patientId(), Date.valueOf(medicalRecord.creationDate()), medicalRecord.medicalCode(), medicalRecord.providerId(), medicalRecord.notes(), id);
        return new MedicalRecord(medicalRecord.id(), medicalRecord.patientId(), medicalRecord.creationDate(), medicalRecord.medicalCode(), medicalRecord.providerId(), medicalRecord.notes());
    }

    public void deleteByPatientId(int id){
        this.jdbcTemplate.update("DELETE FROM MedicalRecord WHERE patientId = ?", id);
    }

    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void deleteAll() {
        this.jdbcTemplate.update("DELETE FROM MedicalRecord");
    }

    public Collection<MedicalRecord> findAll() {
        return this.jdbcTemplate.query("SELECT * FROM MedicalRecord", (rs, rowNum) -> new MedicalRecord(rs.getInt(1), rs.getInt(2), toLocalDate(rs.getDate(3)), rs.getString(4), rs.getInt(5), rs.getString(6)));
    }
}

