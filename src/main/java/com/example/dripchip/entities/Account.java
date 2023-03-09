package com.example.dripchip.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@NoArgsConstructor
public class Account extends AbstractEntity<Integer> {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "email", nullable = false)
    private String password;
    @OneToMany(mappedBy = "chipper")
    private List<Animal> animals;

    public Account(Integer id) {
        super(id);
    }
}
