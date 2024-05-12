package org.sid.salle.web;

import jakarta.servlet.http.HttpSession;
import org.sid.salle.dao.ReservationRepository;
import org.sid.salle.model.Categorie;
import org.sid.salle.model.Reservation;
import org.sid.salle.model.Salle;
import org.sid.salle.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DateFormat;

@RestController
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;
    @GetMapping("/reservations")
    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }
    @PostMapping("/submitReservation")
    public Map<String, Object>  submitReservation(@RequestBody Map<String, Object> requestData, HttpSession session) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date_fin;
        Date date_debut;
        Long categorieId;
        try {
            date_fin = dateFormat.parse((String) requestData.get("date_fin"));
            date_debut = dateFormat.parse((String) requestData.get("date_debut"));
            categorieId = Long.parseLong(requestData.get("categorieId").toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format or category ID");
            return errorResponse;
        }

        List<Salle> salles = reservationService.getAvailableSalle(categorieId, date_fin, date_debut);
        //System.out.println(chambres.size());
        if(salles.isEmpty()){
            return null;
        }else{
        Long salleId = salles.get(0).getId();
        //String sallename = salles.get(0).getName();
        Map<String, Object> reservationDetails = new HashMap<>();
        reservationDetails.put("date_fin", date_fin);
        reservationDetails.put("date_debut", date_debut);
        reservationDetails.put("categorieId", categorieId);
        reservationDetails.put("salleId", salleId);
        reservationDetails.put("salles", salles.get(0));
        session.setAttribute("reservationDetails", reservationDetails);
        System.out.println(session.toString());
        return reservationDetails;
    }}
}
