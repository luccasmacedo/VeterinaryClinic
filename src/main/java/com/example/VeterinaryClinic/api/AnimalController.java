package com.example.VeterinaryClinic.api;

import java.net.URI;
import java.util.Optional;

import com.example.VeterinaryClinic.model.Animal;
import com.example.VeterinaryClinic.model.AnimalRepository;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Controller
@RequestMapping("/api/v1/animals")
public class AnimalController {

  @Autowired
  private AmqpTemplate amqpTemplate;

  public static final String exchange = "spring-boot-exchange";
  static final String routingkey = "foo.bar.#";

  private AnimalRepository animalRepository;

  @Autowired
  public AnimalController(AnimalRepository animalRepository) {
    this.animalRepository = animalRepository;
  }

  @GetMapping
  public ResponseEntity<Page<Animal>> getAllAnimals(Pageable pageable) {
    return ResponseEntity.ok(animalRepository.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Animal> getById(@PathVariable Integer id) {
    Optional<Animal> optionalAnimal = animalRepository.findById(id);
    if (!optionalAnimal.isPresent()) {
      return ResponseEntity.unprocessableEntity().build();
    }
    return ResponseEntity.ok(optionalAnimal.get());
  }

  @PostMapping
  public ResponseEntity<Animal> create(@Validated @RequestBody Animal animal) {
    Animal savedAnimal = animalRepository.save(animal);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedAnimal.getId())
        .toUri();
    produce(animal);
    return ResponseEntity.created(location).body(savedAnimal);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Animal> update(@PathVariable Integer id, @RequestBody Animal animal) {
    Optional<Animal> optionalAnimal = animalRepository.findById(id);
    if (!optionalAnimal.isPresent()) {
      return ResponseEntity.unprocessableEntity().build();
    }

    animal.setId(optionalAnimal.get().getId());
    animalRepository.save(animal);
    produce(animal);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Animal> delete(@PathVariable Integer id) {
    Optional<Animal> optionalAnimal = animalRepository.findById(id);
    if (!optionalAnimal.isPresent()) {
      return ResponseEntity.unprocessableEntity().build();
    }

    animalRepository.delete(optionalAnimal.get());

    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
    return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public void produce(Animal animal){
		amqpTemplate.convertAndSend(exchange, routingkey, animal);
		System.out.println("Send msg = " + animal);
	}
}