package org.sid.salle.services;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.sid.salle.dao.ReservationRepository;
import org.sid.salle.model.Client;
import org.sid.salle.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ReservationRepository reservationRepository;
    @Override
    public void sendVerificationEmail(Client client, String lienReservation, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String subject="Please verify you inscription";
        String senderName="Salle Manager";
        String mailContent = "<p>Dear "+client.getNom()+" "+client.getPrenom()+", </p>";
        String verifyURL = siteUrl + "/verify?code="+lienReservation;
        mailContent+="<h3><a href=\""+verifyURL+"\"> VERIFY </a></h3>";
        mailContent+="<p>Please Click the link below to verify to your inscription :</p>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("h3456168@gmail.com", senderName);
        helper.setTo(client.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent,true);
        javaMailSender.send(message);
        System.out.println("email envoyé avec succée");
    }

    @Override
    public Reservation verify(String verifycode){
        Reservation reservation = reservationRepository.findReservationBylienReservation(verifycode);
        if (reservation == null || reservation.isStatus()){
            return  null;
        }else{
            reservationRepository.Upstatus(reservation.getId());
            return  reservation;
        }
    }

    public static void convertHtmlToPdf(String html, OutputStream outputStream) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(reader);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateSuccessHtml(Date dateArrive, String prenom, String nom, String categoryName, Date dateDepart, String adresse, String email, String idres, float prix ,String NomSal) {

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>");
        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<title>Reservation Confirmation</title>");
        htmlBuilder.append("</head>");
        htmlBuilder.append("<br>");
        htmlBuilder.append("<br>");
        htmlBuilder.append("<br>");
        htmlBuilder.append("<body>");
        htmlBuilder.append("<h1>Verification successful!</h1>");
        htmlBuilder.append(String.format("<p>Hello, %s %s! Your reservation is confirmed.</p>",nom, prenom));
        htmlBuilder.append(String.format("<p>Reservation details: Arriving on %tF, Departing on %tF.</p>", dateArrive, dateDepart));
        htmlBuilder.append(String.format("<p>Category: %s</p>", categoryName));
        htmlBuilder.append(String.format("<p>Salle: %s</p>", NomSal));
        htmlBuilder.append(String.format("<p>Address: %s</p>", adresse));
        htmlBuilder.append(String.format("<p>Email: %s</p>", email));
        htmlBuilder.append(String.format("<p>Reservation ID: %s</p>", idres));
        htmlBuilder.append(String.format("<p>Total price: $%.2f</p>", prix));
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");
        return htmlBuilder.toString();
    }

    @Override
    public void sendVerificationEmailWithAttachment(String code, byte[] pdfAttachment) throws MessagingException, UnsupportedEncodingException {
        Reservation reservation = reservationRepository.findReservationBylienReservation(code);
        if (reservation != null) {
            String subject = "Verification Successful";
            String senderName = "Hotel Manager";
            String mailContent = "Dear " + reservation.getClient().getNom() + " " + reservation.getClient().getPrenom() + ",\n";
            mailContent += "Your verification was successful! Please find the attached PDF for confirmation.";
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("h3456168@gmail.com", senderName);
            helper.setTo(reservation.getClient().getEmail());
            helper.setSubject(subject);
            helper.setText(mailContent);
            helper.addAttachment("verification_success.pdf", new ByteArrayResource(pdfAttachment));
            javaMailSender.send(message);
        }
    }

    @Scheduled(fixedDelay = 900000)
    public void deleteExpiredReservations() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservations(new Date());
        for (Reservation reservation : expiredReservations) {
            reservationRepository.deleteById(reservation.getId());
        }
    }
}
