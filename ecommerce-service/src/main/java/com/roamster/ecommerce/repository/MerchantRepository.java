package com.roamster.ecommerce.repository;

import com.roamster.ecommerce.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    List<Merchant> findByTypeAndIsActive(String type, Boolean isActive);
    List<Merchant> findByCityAndIsActive(String city, Boolean isActive);
}
