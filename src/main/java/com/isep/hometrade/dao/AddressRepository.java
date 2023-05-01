package com.isep.hometrade.dao;

import com.isep.hometrade.business.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
