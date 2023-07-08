package it.unisalento.iot.iotdigitaltwin.repositories;


import it.unisalento.iot.iotdigitaltwin.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    public List<User> findByCognome(String Cognome);
    public List<User> findByNome(String Nome);
    public List<User> findByEmail(String Email);
    public User findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);


}
