package com.sumerge.authenticationmicroservice.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//UserModel refers to the user class ,  Integer is the type of userId
public interface UserRepository extends JpaRepository<UserModel , Integer> {

    Optional<UserModel> findByEmail(String email);
}
