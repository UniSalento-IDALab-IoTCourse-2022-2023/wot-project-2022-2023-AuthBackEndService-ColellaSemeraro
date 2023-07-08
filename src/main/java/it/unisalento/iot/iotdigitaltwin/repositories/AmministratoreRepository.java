package it.unisalento.iot.iotdigitaltwin.repositories;

import it.unisalento.iot.iotdigitaltwin.domain.Amministratore;
import it.unisalento.iot.iotdigitaltwin.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AmministratoreRepository extends MongoRepository<Amministratore, String> {

    Amministratore findByUsername(String username);

    boolean existsByEmail(String email);
}
