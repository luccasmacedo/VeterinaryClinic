package com.example.VeterinaryClinic.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Animal implements Serializable{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String species;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    public Animal() {
    }

    public Animal(String name, String species) {
        this.name = name;
        this.species = species;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;

        for (Appointment b : appointments) {
            b.setAnimal(this);
        }
    }

}
