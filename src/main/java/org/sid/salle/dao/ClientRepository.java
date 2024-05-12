package org.sid.salle.dao;

import org.sid.salle.model.Categorie;
import org.sid.salle.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface ClientRepository extends JpaRepository<Client,Long> {
}
