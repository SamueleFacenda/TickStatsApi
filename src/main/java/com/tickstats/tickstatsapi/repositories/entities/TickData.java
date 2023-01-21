package com.tickstats.tickstatsapi.repositories.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@ToString
@NoArgsConstructor
@Table(name="data")
public class TickData {

    @Id
    @SequenceGenerator(name="data_id_seq", sequenceName="data_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="data_id_seq")
    @Getter
    private Long id;
    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", referencedColumnName = "id")
    @ToString.Exclude
    private MyUser userid;

    @Getter @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment", referencedColumnName = "id")
    private Comment comment;
    @Getter @Setter
    @Column(nullable = false)
    private String label;
    @Getter @Setter
    @Column(nullable = false)
    private Timestamp createdat;


    public TickData(MyUser userid, String label, Timestamp createdat) {
        this.userid = userid;
        this.label = label;
        this.createdat = createdat;
    }
}
