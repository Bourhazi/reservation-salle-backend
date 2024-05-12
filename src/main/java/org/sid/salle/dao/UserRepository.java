package org.sid.salle.dao;

import org.sid.salle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.Optional;

@RepositoryRestController
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
