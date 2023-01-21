package com.tickstats.tickstatsapi.repositories;

import com.tickstats.tickstatsapi.repositories.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<MyUser, Long> {
    public interface ILabelCount {
        String getLabel();
        int getFrequency();
    }
    MyUser findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(
            value = """
                    SELECT label, count(*) as frequency
                    FROM data
                    JOIN users ON data.userid = users.id
                    WHERE users.username = :username
                    GROUP BY label;""",
            nativeQuery = true
    )
    List<ILabelCount> findCountLabelForUser(String username);
}