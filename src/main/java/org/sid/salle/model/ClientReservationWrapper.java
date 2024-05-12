package org.sid.salle.model;

public class ClientReservationWrapper {
    private Client client;
    private Reservation reservation;

    // Getters and setters
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
