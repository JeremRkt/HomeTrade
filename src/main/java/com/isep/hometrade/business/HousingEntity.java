package com.isep.hometrade.business;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "housing")
public class HousingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_housing")
    private Long idHousing;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "creation")
    private Date creation;

    @Column(name = "modification")
    private Date modification;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_address", nullable = false)
    private AddressEntity addressEntity;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "housingEntity", cascade = CascadeType.ALL)
    private Set<PhotoEntity> photoEntities;

}
