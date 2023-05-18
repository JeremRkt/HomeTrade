package com.isep.hometrade.business;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address", unique = true, nullable = false)
    private Long idAddress;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "code", nullable = false)
    private int code;

    @Column(name = "country", nullable = false)
    private String country;

}
