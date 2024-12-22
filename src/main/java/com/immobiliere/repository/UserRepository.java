package com.immobiliere.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import com.immobiliere.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
    
    User findByEmail(String email);
    
    
}

