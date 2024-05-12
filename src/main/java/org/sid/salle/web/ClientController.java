package org.sid.salle.web;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.sid.salle.dao.ClientRepository;
import org.sid.salle.dao.ReservationRepository;
import org.sid.salle.model.*;
import org.sid.salle.services.ClientService;
import org.sid.salle.services.ClientServiceImpl;
import org.sid.salle.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@RestController
public class ClientController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ReservationService reservationService;
    @Autowired
    ClientService clientService;


    @GetMapping("/clients")
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }
    @PostMapping("/submitClient")
    public void submitClient(@RequestBody ClientReservationWrapper clientReservationWrapper , HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String siteUrl = ClientService.getSiteURL(request);
        Client client = clientReservationWrapper.getClient();
        Reservation reservation = clientReservationWrapper.getReservation();

        Salle salle = reservation.getSalle();
        if (salle == null || salle.getId() == 0) {
            throw new IllegalArgumentException("Salle must not be null");
        }
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation must not be null");
        }
        Client client1 = clientRepository.save(client);
        reservation.setClient(client1);
        reservationService.save(reservation);
        //clientService.deleteExpiredReservations();
        clientService.sendVerificationEmail(client, reservation.getLienReservation(), siteUrl);
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, HttpServletResponse response) throws IOException {
        boolean redirectSuccess = false;
        try {
            Reservation reservation = clientService.verify(code);
            if (reservation != null) {
                Date dateArrive = reservation.getDate_debut();
                String prenom = reservation.getClient().getPrenom();
                String nom = reservation.getClient().getNom();
                String CategoryName = reservation.getSalle().getCategorie().getName();
                Date dateDepart = reservation.getDate_fin();
                String adresse = reservation.getClient().getAdresse();
                String email = reservation.getClient().getEmail();
                String NomSal= reservation.getSalle().getName();
                Long idrese= reservation.getId();
                float prix= reservation.getSalle().getPrix();

                String successHtml = clientService.generateSuccessHtml(dateArrive,prenom,nom,CategoryName,dateDepart,adresse,email, String.valueOf(idrese),prix ,NomSal);
                ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
                ClientServiceImpl.convertHtmlToPdf(successHtml, pdfOutputStream);
                byte[] pdfBytes = pdfOutputStream.toByteArray();
                clientService.sendVerificationEmailWithAttachment(code, pdfBytes);
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=verification_success.pdf");
                response.getOutputStream().write(pdfBytes);
                //clientService.deleteExpiredReservations();
                redirectSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (redirectSuccess) {
            return "redirect:/create-reservation";
        } else {
            return "redirect:/reservation?Reservation Perimé";
        }
    }
}
