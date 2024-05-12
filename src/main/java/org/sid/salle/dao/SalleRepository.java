package org.sid.salle.dao;
import org.sid.salle.model.Categorie;
import org.sid.salle.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import java.util.Date;
import java.util.List;

@RepositoryRestController
public interface SalleRepository extends JpaRepository<Salle,Long> {
    @Query("SELECT s FROM Salle s " +
            "WHERE s.categorie = :categorie " +
            "AND NOT EXISTS (" +
            "   SELECT r FROM Reservation r " +
            "   WHERE r.salle = s " +
            "   AND ((r.date_fin >= :date_debut AND r.date_debut <= :date_debut) " +
            "        OR (r.date_fin >= :date_fin AND r.date_debut <= :date_fin) " +
            "        OR (r.date_fin <= :date_fin AND r.date_debut >= :date_debut)))")
    List<Salle> findAvailableSallesByCategoryAndDates(
            @Param("categorie") Categorie categorie,
            @Param("date_fin") Date date_fin,
            @Param("date_debut") Date date_debut
    );
}

