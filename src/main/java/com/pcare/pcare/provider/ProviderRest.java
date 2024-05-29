package com.pcare.pcare.provider;

import com.pcare.pcare.patient.Patient;
import com.pcare.pcare.utils.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/provider")
public class ProviderRest {

    private final ProviderRepository repository;


    public ProviderRest(ProviderRepository repository) {
        this.repository = repository;
    }
    @GetMapping
    public Collection<Provider> findAll() {
        return this.repository.findAll();
    }

    @PostMapping
    public Provider save(@RequestBody Provider Provider) {
        return this.repository.save(Provider);
    }

    @PutMapping("/{id}")
    public Provider update(@PathVariable Integer id, @RequestBody Provider provider) {
        this.repository.findById(id).orElseThrow(() -> new NotFoundException("Provider with id %d not found".formatted(id)));
        return this.repository.update(id, provider);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        this.repository.findById(id).orElseThrow(() -> new NotFoundException("Provider with id %d not found".formatted(id)));
        this.repository.delete(id);
    }

    @GetMapping("/{id}")
    public Optional<Provider> find(@PathVariable int id) {
        return this.repository.findById(id);
    }


}
