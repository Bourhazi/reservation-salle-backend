package org.sid.salle.dao;

import org.sid.salle.model.Categorie;
import org.sid.salle.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RepositoryRestController
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Transactional
    @Query("UPDATE Reservation r SET r.status=true WHERE r.id = ?1")
    @Modifying
    public void Upstatus(long id);

    @Query("Select r FROM Reservation r WHERE r.lienReservation = ?1")
    public Reservation findReservationBylienReservation(String code);

    @Query("SELECT r FROM Reservation r WHERE r.status = false AND r.dateCreated < :expirationDate")
    List<Reservation> findExpiredReservations(@Param("expirationDate") Date expirationDate);
}
