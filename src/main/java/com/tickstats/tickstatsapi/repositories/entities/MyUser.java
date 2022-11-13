package com.tickstats.tickstatsapi.repositories.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
@NoArgsConstructor
@Table(name="users")
public class MyUser {

    @Id
    @SequenceGenerator(name="users_id_seq", sequenceName="users_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="users_id_seq")
    @Getter
    private Long id;
    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String username;
    @Getter @Setter
    @Column(nullable = false)
    private String password;

    @Getter
    @OneToMany(mappedBy = "userid")
    @ToString.Exclude
    private List<TickData> userData;


    public MyUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
