package com.example.VeterinaryClinic.api;

import java.net.URI;
import java.util.Optional;

import com.example.VeterinaryClinic.model.Animal;
import com.example.VeterinaryClinic.model.AnimalRepository;
import com.example.VeterinaryClinic.model.Appointment;
import com.example.VeterinaryClinic.model.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private AnimalRepository animalRepository;
    private AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentController(AnimalRepository animalRepository, AppointmentRepository appointmentRepository) {
        this.animalRepository = animalRepository;

    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment) {
        Optional<Animal> optionalAnimal = animalRepository.findById(appointment.getAnimal().getId());
        if (!optionalAnimal.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        appointment.setAnimal(optionalAnimal.get());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(savedAppointment.getId()).toUri();

        return ResponseEntity.created(location).body(savedAppointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@RequestBody Appointment appointment, @PathVariable Integer id) {
        Optional<Animal> optionalAnimal = animalRepository.findById(appointment.getAnimal().getId());
        if (!optionalAnimal.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Appointment> optionalBook = appointmentRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        appointment.setAnimal(optionalAnimal.get());
        appointment.setId(optionalBook.get().getId());
        appointmentRepository.save(appointment);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Appointment> delete(@PathVariable Integer id) {
        Optional<Appointment> optionalBook = appointmentRepository.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        appointmentRepository.delete(optionalBook.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Appointment>> getAll(Pageable pageable) {
        return ResponseEntity.ok(appointmentRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable Integer id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalAppointment.get());
    }
}
