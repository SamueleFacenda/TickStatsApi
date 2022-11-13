package com.tickstats.tickstatsapi.repositories;

import com.tickstats.tickstatsapi.repositories.entities.TickData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TickDataRepository extends CrudRepository<TickData, Long> {
}