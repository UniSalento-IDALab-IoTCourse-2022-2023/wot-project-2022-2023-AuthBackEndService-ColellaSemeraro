package it.unisalento.iot.iotdigitaltwin.restcontrollers;

import it.unisalento.iot.iotdigitaltwin.domain.Amministratore;
import it.unisalento.iot.iotdigitaltwin.dto.AmministratoreDTO;
import it.unisalento.iot.iotdigitaltwin.repositories.AmministratoreRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.AtletaRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static it.unisalento.iot.iotdigitaltwin.configuration.SecurityConfig.passwordEncoder;

@CrossOrigin
@RestController
@RequestMapping("/api/users/amministratore")
public class AmministratoreRestController {

    @Autowired
    AtletaRepository atletaRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;


    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AmministratoreDTO creaAmministratore(@RequestBody AmministratoreDTO amministratoreDTO) {

        if ((coachRepository.findByUsername(amministratoreDTO.getUsername())!= null) || (atletaRepository.findByUsername(amministratoreDTO.getUsername())!=null) || (amministratoreRepository.findByUsername(amministratoreDTO.getUsername())!=null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }


        if ((coachRepository.existsByEmail(amministratoreDTO.getEmail())) || (atletaRepository.existsByEmail(amministratoreDTO.getEmail()) || (amministratoreRepository.existsByEmail(amministratoreDTO.getEmail())))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        Amministratore amministratore = new Amministratore();

        amministratore.setUsername(amministratoreDTO.getUsername());
        amministratore.setNome(amministratoreDTO.getNome());
        amministratore.setCognome(amministratoreDTO.getCognome());
        amministratore.setEmail(amministratoreDTO.getEmail());
        amministratore.setDataNascita(amministratoreDTO.getDataNascita());
        amministratore.setPassword(passwordEncoder().encode(amministratoreDTO.getPassword()));
        amministratore.setRole("AMMINISTRATORE");

        amministratoreRepository.save(amministratore);

        amministratoreDTO.setPassword(amministratore.getPassword());
        amministratoreDTO.setId(amministratore.getId());
        amministratoreDTO.setRole(amministratore.getRole());

        return amministratoreDTO;
    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE')")
    @RequestMapping(value = "/find/username/{username}", method = RequestMethod.GET)
    public AmministratoreDTO getByUsername(@PathVariable String username) {

        System.out.println("ARRIVATO L'USERNAME: " + username);

        Amministratore amministratore = amministratoreRepository.findByUsername(username);

        AmministratoreDTO amministratoreDTO = new AmministratoreDTO();

        amministratoreDTO.setId(amministratore.getId());
        amministratoreDTO.setUsername(amministratore.getUsername());
        amministratoreDTO.setNome(amministratore.getNome());
        amministratoreDTO.setCognome(amministratore.getCognome());
        amministratoreDTO.setEmail(amministratore.getEmail());
        amministratoreDTO.setDataNascita(amministratore.getDataNascita());
        amministratoreDTO.setRole(amministratore.getRole());

        return amministratoreDTO;

    }

}
