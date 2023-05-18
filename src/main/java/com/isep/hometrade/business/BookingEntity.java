package com.isep.hometrade.business;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_booking")
    private Long idBooking;

    @Column(name = "start", nullable = false)
    private Date start;

    @Column(name = "end", nullable = false)
    private Date end;

    @Column(name = "adults")
    private int adults;

    @Column(name = "children")
    private int children;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "id_housing", nullable = false)
    private HousingEntity housingEntity;

}
