package com.pcare.pcare.provider;

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
class ProviderRepositoryTest {

    @Autowired
    private ProviderRepository providerRepository;

    @AfterEach
    void tearDown() {
        this.providerRepository.deleteAll();
    }

    @Test
    public void testCreateNewProvider() {
        LocalDate dob = LocalDate.of(2001, 12, 26);
        Provider newProvider = new Provider(null, "John", "Smith", dob, "yulia@gmail.com", "+380966756456", "surgeon");
        Provider savedProvider = this.providerRepository.save(newProvider);
        Assertions.assertNotNull(savedProvider, "Provider can not be NULL");
        Assertions.assertNotNull(savedProvider.id(), "Provider id is required");
        Assertions.assertEquals(dob, savedProvider.dob());
    }

    @Test
    void testFindAll() {
        Collection<Provider> all = this.providerRepository.findAll();
        Assertions.assertEquals(0, all.size());

        IntStream.range(1, 10).forEach(i -> {
            LocalDate dob = LocalDate.of(2004, 12, i);
            Provider newProvider = new Provider(null, "Sam_" + i, "Williams", dob, "yulia@gmail.com", "+380966756456", "surgeon_" + i);
            this.providerRepository.save(newProvider);
        });

        Collection<Provider> allSecond = this.providerRepository.findAll();
        Assertions.assertEquals(9, allSecond.size());
    }


    @Test
    void testFindById() {
        LocalDate date = LocalDate.of(2004, 12, 26);
        Provider provider = new Provider(null, "Will", "Smith", date, "yulia@gmail.com", "+380966756456", "dentist");
        Provider saved = this.providerRepository.save(provider);
        Optional<Provider> foundById = this.providerRepository.findById(saved.id());
        Assertions.assertNotNull(saved, "Provider can not be NULL");
        Assertions.assertNotNull(saved.id(), "Provider id is required");
        Assertions.assertEquals(saved.id(), foundById.get().id());

    }

    @Test
    void testDeleteAll() {
        Collection<Provider> all = this.providerRepository.findAll();
        Assertions.assertEquals(0, all.size());

        IntStream.range(1, 10).forEach(i -> {
            LocalDate dob = LocalDate.of(2004, 12, i);
            Provider newProvider = new Provider(null, "Sam_" + i, "Williams", dob, "yulia@gmail.com", "+380966756456", "surgeon_" + i);
            this.providerRepository.save(newProvider);
        });

        Collection<Provider> allSecond = this.providerRepository.findAll();
        Assertions.assertEquals(9, allSecond.size());

        this.providerRepository.deleteAll();

        Collection<Provider> allAfterDelete = this.providerRepository.findAll();
        Assertions.assertEquals(0, allAfterDelete.size(), "Size after deleteAll should be zero");
    }


    @Test
    void testUpdate() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        Provider newProvider = new Provider(null, "Vika", "Kr", dob, "yulia@gmail.com", "+380966756456", "surgeon");
        Provider savedProvider = this.providerRepository.save(newProvider);

        Provider updatedProvider = new Provider(savedProvider.id(), "Yulia", savedProvider.lastName(), savedProvider.dob(), savedProvider.email(), savedProvider.phone(), savedProvider.speciality());

        this.providerRepository.update(savedProvider.id(), updatedProvider);

        Optional<Provider> retrievedProvider = this.providerRepository.findById(savedProvider.id());

        Assertions.assertNotNull(retrievedProvider);
        Assertions.assertEquals(savedProvider.id(), retrievedProvider.get().id());
        Assertions.assertEquals(updatedProvider.firstName(), retrievedProvider.get().firstName());
        Assertions.assertFalse(savedProvider.lastName() == retrievedProvider.get().lastName());
        Assertions.assertEquals(savedProvider.dob(), retrievedProvider.get().dob());
    }

    @Test
    void testDelete() {
        LocalDate dob = LocalDate.of(2004, 12, 26);
        Provider newProvider = new Provider(null, "Vika", "Kr", dob, "yulia@gmail.com", "+380966756456", "surgeon");
        Provider savedProvider = this.providerRepository.save(newProvider);
        int savedProviderId = savedProvider.id();
        this.providerRepository.delete(savedProviderId);
        Optional<Provider> deletedProvider = this.providerRepository.findById(savedProviderId);
        Assertions.assertFalse(deletedProvider.isPresent());
    }

}