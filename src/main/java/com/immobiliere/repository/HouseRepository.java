package com.immobiliere.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.immobiliere.model.House;
import com.immobiliere.model.User;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    // Méthodes de recherche basées sur un seul critère
    List<House> findByIsAvailableTrue();
    List<House> findAllByIsAvailable(boolean isAvailable);
    List<House> findAllByOwner(User owner);
    List<House> findByPriceLessThanEqual(BigDecimal price);
    List<House> findBySurfaceGreaterThanEqual(Double surface);
    List<House> findByCityContaining(String city);
    List<House> findByBedrooms(Integer bedrooms);
    
    List<House> findAllByOwner_Id(Long ownerId);
    
    // Méthodes de recherche basées sur plusieurs critères
    List<House> findByPriceLessThanEqualAndSurfaceGreaterThanEqualAndCityContaining(BigDecimal price, Double surface, String city);
    List<House> findByPriceLessThanEqualAndSurfaceGreaterThanEqual(BigDecimal price, Double surface);
    List<House> findByPriceLessThanEqualAndCityContaining(BigDecimal price, String city);
    List<House> findBySurfaceGreaterThanEqualAndCityContaining(Double surface, String city);
}
