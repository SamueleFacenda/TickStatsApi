package com.tickstats.tickstatsapi.repositories.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter @Setter
    @Column(columnDefinition="TEXT")
    private String comment;

    @Getter @Setter
    @OneToOne(mappedBy = "comment")
    private TickData tickData;

}