package org.ecommerce.project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "addresses")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {
//    public Address(String street, String building, String city, String state, String country, String zipcode) {
//        this.street = street;
//        this.building = building;
//        this.city = city;
//        this.state = state;
//        this.country = country;
//        this.zipcode = zipcode;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 5, message = "Street name must have at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 1, message = "Building name must have at least 1 characters")
    private String building;

    @NotBlank
    @Size(min = 3, message = "City name must have at least 3 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must have at least 2 characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must have at least 2 characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Zipcode name must have at least 6 characters")
    private String zipcode;

    @ManyToOne
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "address", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();
}
