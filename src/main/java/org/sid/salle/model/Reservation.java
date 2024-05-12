package org.sid.salle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date date_debut;
    private Date date_fin;
    @Column(name = "date_created", updatable = false)
    private Date dateCreated;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lien_Reservation")
    private String lienReservation;
    private boolean status=false;
    @ManyToOne
    private Salle salle;
    @ManyToOne
    private Client client;
    @PrePersist
    protected void onCreate() {
        dateCreated = new Date();
    }
}



