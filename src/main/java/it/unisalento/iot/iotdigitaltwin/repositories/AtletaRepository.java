package it.unisalento.iot.iotdigitaltwin.repositories;

import it.unisalento.iot.iotdigitaltwin.domain.Atleta;
import it.unisalento.iot.iotdigitaltwin.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AtletaRepository extends MongoRepository<Atleta, String> {

    Atleta findByUsername(String username);

    List<Atleta> findAllByIdCoach(String idCoach);

    boolean existsByEmail(String email);

    boolean existsByIdCoach(String idCoach);
}
