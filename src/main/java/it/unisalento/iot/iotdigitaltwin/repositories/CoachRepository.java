package it.unisalento.iot.iotdigitaltwin.repositories;

import it.unisalento.iot.iotdigitaltwin.domain.Coach;
import it.unisalento.iot.iotdigitaltwin.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CoachRepository extends MongoRepository<Coach, String> {

    Coach findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Coach> findBySquadraAndRuoloAllenato(String squadra, String ruoloAllenato);

    boolean existsBySquadraAndRuoloAllenato(String squadra, String ruoloAllenato);
}
