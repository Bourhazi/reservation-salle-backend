package org.sid.salle.services;

import jakarta.servlet.http.HttpServletRequest;
import org.sid.salle.model.Client;
import jakarta.mail.MessagingException;
import org.sid.salle.model.Reservation;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;


public interface ClientService {

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public void sendVerificationEmail(Client client , String lienReservation, String siteUrl) throws MessagingException, UnsupportedEncodingException;
    public Reservation verify(String verifycode);
    public String generateSuccessHtml(Date dateArrive, String prenom, String nom, String categoryName, Date dateDepart, String adresse, String email, String id, float prix , String NomSal);
    public void sendVerificationEmailWithAttachment(String code, byte[] pdfAttachment) throws MessagingException, UnsupportedEncodingException;
    public void deleteExpiredReservations();
}
