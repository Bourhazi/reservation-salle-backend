package org.sid.salle.services;

import org.sid.salle.dao.CategorieRepository;
import org.sid.salle.dao.ReservationRepository;
import org.sid.salle.dao.SalleRepository;
import org.sid.salle.model.Categorie;
import org.sid.salle.model.Reservation;
import org.sid.salle.model.Salle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService{
    @Autowired
    CategorieRepository categorieRepository;
    @Autowired
    SalleRepository salleRepository;

    @Autowired
    private ReservationRepository reservationRepository;
    @Override
    public List<Salle> getAvailableSalle(Long categorieId, Date date_fin, Date date_debut) {
        Optional<Categorie> categorie = categorieRepository.findById(categorieId);
        return salleRepository.findAvailableSallesByCategoryAndDates(categorie.get(),date_fin,date_debut);
    }

    @Override
    public Reservation save(Reservation reservation) {
        String randomString = RandomStringUtils.randomAlphanumeric(64);
        reservation.setLienReservation(randomString);
        return reservationRepository.save(reservation);
    }
}
