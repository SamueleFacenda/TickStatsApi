package com.tickstats.tickstatsapi.repositories;

import com.tickstats.tickstatsapi.repositories.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<MyUser, Long> {
    MyUser findByUsername(String username);
    boolean existsByUsername(String username);
}