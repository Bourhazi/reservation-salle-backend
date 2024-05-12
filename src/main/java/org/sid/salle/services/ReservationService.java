package org.sid.salle.services;

import org.sid.salle.model.Reservation;
import org.sid.salle.model.Salle;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


public interface ReservationService {
    public List<Salle> getAvailableSalle(Long categorieId, Date date_fin, Date date_debut);
    public Reservation save(Reservation reservation);
}
