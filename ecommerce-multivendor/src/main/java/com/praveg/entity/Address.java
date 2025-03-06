package com.praveg.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String locality;
    private String address;

    private String city;

    private String state;

    private String pinCode;

    private String mobile;

    @ManyToOne      //remove
    @JoinColumn(name = "user_id") // Creates a foreign key column in the Address table
    private User user;
}
