package com.isep.hometrade.business;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "house")
public class HouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_house")
    private Integer idHouse;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_address", nullable = false)
    private AddressEntity addressEntity;

    @OneToMany(mappedBy = "houseEntity", cascade = CascadeType.ALL)
    private Set<PhotoEntity> photoEntities;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;

}
