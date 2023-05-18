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
@Table(name = "message")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message", unique = true, nullable = false)
    private Long idMessage;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_housing")
    private HousingEntity housingEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_other_message")
    private MessageEntity messageEntity;

}