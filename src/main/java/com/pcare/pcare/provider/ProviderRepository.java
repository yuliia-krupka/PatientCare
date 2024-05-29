package com.pcare.pcare.provider;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;
import java.time.LocalDate;


@Repository
public class ProviderRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProviderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Provider> findById(Integer id) {
        if (id != null) {
            try {
                Provider provider = this.jdbcTemplate.queryForObject("SELECT * FROM provider WHERE id = ?", new Object[]{id}, (rs, rowNum) -> {
                    Date date = rs.getDate(4);
                    return new Provider(rs.getInt(1), rs.getString(2), rs.getString(3), toLocalDate(date), rs.getString(5), rs.getString(6), rs.getString(7));
                });
                return Optional.of(provider);
            } catch (EmptyResultDataAccessException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public Provider save(Provider provider) {
        this.jdbcTemplate.update("INSERT INTO provider(FirstName, LastName, DoB, Email, Phone, speciality) VALUES (?, ?, ?, ?, ?, ?)", provider.firstName(), provider.lastName(), provider.dob(), provider.email(), provider.phone(), provider.speciality());

        return this.jdbcTemplate.query("select * from provider ORDER BY ID DESC LIMIT 1", rs -> {
            rs.next();
            final var date = rs.getDate(4);
            return new Provider(rs.getInt(1), rs.getString(2), rs.getString(3), toLocalDate(date), rs.getString(5), rs.getString(6), rs.getString(7));
        });
    }

    public Collection<Provider> findAll() {
        return this.jdbcTemplate.query("SELECT * FROM provider", (rs, rowNum) -> new Provider(rs.getInt(1), rs.getString(2), rs.getString(3), toLocalDate(rs.getDate(4)), rs.getString(5), rs.getString(6), rs.getString(7)));
    }

    public Provider update(int id, Provider provider) {
        this.jdbcTemplate.update(
                "UPDATE provider SET FirstName = ?, LastName = ?, DoB = ?, Speciality = ? WHERE Id = ?",
                provider.firstName(), provider.lastName(), provider.dob(), provider.speciality(), id
        );
        return new Provider(provider.id(), provider.firstName(), provider.lastName(), provider.dob(), provider.email(), provider.phone(),provider.speciality());
    }


    public void delete(int id) {
        this.jdbcTemplate.update("DELETE FROM provider WHERE Id = ?", id);
    }

    void deleteAll() {
        this.jdbcTemplate.update("DELETE FROM provider");
    }

    private static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

