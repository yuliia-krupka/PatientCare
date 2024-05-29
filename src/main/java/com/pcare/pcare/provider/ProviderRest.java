package com.pcare.pcare.provider;

import com.pcare.pcare.utils.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/provider")
public class ProviderRest {

    private final ProviderService service;

    public ProviderRest(ProviderService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<Provider> findAll() {
        return this.service.findAll();
    }

    @PostMapping
    public Provider save(@RequestBody Provider Provider) {
        return this.service.save(Provider);
    }

    @PutMapping("/{id}")
    public Provider update(@PathVariable Integer id, @RequestBody Provider provider) {
        this.service.findById(id).orElseThrow(() -> new NotFoundException("Provider with id %d not found".formatted(id)));
        return this.service.update(id, provider);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        this.service.findById(id).orElseThrow(() -> new NotFoundException("Provider with id %d not found".formatted(id)));
        this.service.delete(id);
    }

    @GetMapping("/{id}")
    public Optional<Provider> find(@PathVariable int id) {
        return this.service.findById(id);
    }


}
