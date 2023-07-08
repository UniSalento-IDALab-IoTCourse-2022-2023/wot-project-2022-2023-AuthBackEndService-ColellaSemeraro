package it.unisalento.iot.iotdigitaltwin.restcontrollers;

import it.unisalento.iot.iotdigitaltwin.domain.Amministratore;
import it.unisalento.iot.iotdigitaltwin.domain.Coach;
import it.unisalento.iot.iotdigitaltwin.dto.AmministratoreDTO;
import it.unisalento.iot.iotdigitaltwin.dto.CoachDTO;
import it.unisalento.iot.iotdigitaltwin.exceptions.UserNotFoundException;
import it.unisalento.iot.iotdigitaltwin.repositories.AmministratoreRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.AtletaRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.unisalento.iot.iotdigitaltwin.configuration.SecurityConfig.passwordEncoder;

@CrossOrigin
@RestController
@RequestMapping("/api/users/coach")
public class CoachRestController {

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    AtletaRepository atletaRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;

    @PreAuthorize("hasAnyRole('AMMINISTRATORE')")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CoachDTO creaCoach(@RequestBody CoachDTO coachDTO) {

        if ((coachRepository.findByUsername(coachDTO.getUsername())!= null) || (atletaRepository.findByUsername(coachDTO.getUsername())!=null) || (amministratoreRepository.findByUsername(coachDTO.getUsername())!=null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }


        if ((coachRepository.existsByEmail(coachDTO.getEmail())) || (atletaRepository.existsByEmail(coachDTO.getEmail()) || (amministratoreRepository.existsByEmail(coachDTO.getEmail())))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        Coach coach = new Coach();

        coach.setUsername(coachDTO.getUsername());
        coach.setNome(coachDTO.getNome());
        coach.setCognome(coachDTO.getCognome());
        coach.setEmail(coachDTO.getEmail());
        coach.setDataNascita(coachDTO.getDataNascita());
        coach.setPassword(passwordEncoder().encode(coachDTO.getPassword()));
        coach.setRole("COACH");
        coach.setSquadra(coachDTO.getSquadra());
        coach.setRuoloAllenato(coachDTO.getRuoloAllenato());

        coachRepository.save(coach);

        coachDTO.setPassword(coach.getPassword());
        coachDTO.setId(coach.getId());
        coachDTO.setRole(coach.getRole());

        return coachDTO;
    }


    @PreAuthorize("hasAnyRole('AMMINISTRATORE')")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<CoachDTO> trovaTutti() {

        List<CoachDTO> coaches = new ArrayList<>();

        for (Coach coach : coachRepository.findAll()){
            CoachDTO newCoach = new CoachDTO();

            newCoach.setId(coach.getId());
            newCoach.setUsername(coach.getUsername());
            newCoach.setNome(coach.getNome());
            newCoach.setCognome(coach.getCognome());
            newCoach.setEmail(coach.getEmail());
            newCoach.setDataNascita(coach.getDataNascita());
            newCoach.setPassword(passwordEncoder().encode(coach.getPassword()));
            newCoach.setRole("COACH");
            newCoach.setSquadra(coach.getSquadra());
            newCoach.setRuoloAllenato(coach.getRuoloAllenato());

            coaches.add(newCoach);
        }

        return coaches;
    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'COACH')")
    @RequestMapping(value = "/findById/{coachId}", method = RequestMethod.GET)
    public CoachDTO trovaPerId(@PathVariable String coachId) {

        CoachDTO coachDTO = new CoachDTO();

        Optional<Coach> optionalCoach = coachRepository.findById(coachId);
        if (optionalCoach.isPresent()){
            Coach coach = optionalCoach.get();

            coachDTO.setId(coach.getId());
            coachDTO.setNome(coach.getNome());
            coachDTO.setCognome(coach.getCognome());
            coachDTO.setUsername(coach.getUsername());
            coachDTO.setPassword(passwordEncoder().encode(coach.getPassword()));
            coachDTO.setEmail(coach.getEmail());
            coachDTO.setDataNascita(coach.getDataNascita());
            coachDTO.setRole(coach.getRole());
            coachDTO.setSquadra(coach.getSquadra());
            coachDTO.setRuoloAllenato(coach.getRuoloAllenato());
        }

        return coachDTO;
    }


    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'ATLETA', 'COACH')")
    @RequestMapping(value = "/find/username/{username}", method = RequestMethod.GET)
    public CoachDTO findByUsername(@PathVariable String username) {

        CoachDTO coachDTO = new CoachDTO();

        Coach coach = coachRepository.findByUsername(username);

        coachDTO.setId(coach.getId());
        coachDTO.setNome(coach.getNome());
        coachDTO.setCognome(coach.getCognome());
        coachDTO.setUsername(coach.getUsername());
        coachDTO.setPassword(passwordEncoder().encode(coach.getPassword()));
        coachDTO.setEmail(coach.getEmail());
        coachDTO.setDataNascita(coach.getDataNascita());
        coachDTO.setRole(coach.getRole());
        coachDTO.setSquadra(coach.getSquadra());
        coachDTO.setRuoloAllenato(coach.getRuoloAllenato());

        return coachDTO;

    }


    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'ATLETA', 'COACH')")
    @RequestMapping(value = "/findByTeamAndRole", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CoachDTO trovaPerSquadraERuoloAllenato(@RequestBody CoachDTO richiesta) {

        CoachDTO coachDTO = new CoachDTO();

        Optional<Coach> optionalCoach = coachRepository.findBySquadraAndRuoloAllenato(richiesta.getSquadra(), richiesta.getRuoloAllenato());
        if (optionalCoach.isPresent()){
            Coach coach = optionalCoach.get();

            coachDTO.setId(coach.getId());
            coachDTO.setNome(coach.getNome());
            coachDTO.setCognome(coach.getCognome());
            coachDTO.setUsername(coach.getUsername());
            coachDTO.setPassword(passwordEncoder().encode(coach.getPassword()));
            coachDTO.setEmail(coach.getEmail());
            coachDTO.setDataNascita(coach.getDataNascita());
            coachDTO.setRole(coach.getRole());
            coachDTO.setSquadra(coach.getSquadra());
            coachDTO.setRuoloAllenato(coach.getRuoloAllenato());
        }

        return coachDTO;
    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'COACH')")
    @PostMapping(value = "/checkifExistsBySquadraAndRuolo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean checkIfExistsBySquadraAndRuolo(@RequestBody CoachDTO richiesta) {

        return coachRepository.existsBySquadraAndRuoloAllenato(richiesta.getSquadra(), richiesta.getRuoloAllenato());

    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE')")
    @PatchMapping(value = "/updateRuoloSquadraCoach/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateRuoloSquadraCoach(@PathVariable String username, @RequestBody CoachDTO coachDTO) throws UserNotFoundException {

        Coach coach = coachRepository.findByUsername(username);

        if(coach != null) {

            if(coachDTO.getSquadra() != null) {
                String squadra = coachDTO.getSquadra();
                coach.setSquadra(squadra);
            }

            if(coachDTO.getRuoloAllenato() != null) {
                String ruoloAllenato = coachDTO.getRuoloAllenato();
                coach.setRuoloAllenato(ruoloAllenato);
            }

            if(!checkIfExistsBySquadraAndRuolo(coachDTO)) {
                coachRepository.save(coach);
                return ResponseEntity.ok("Coach aggiornato con successo");
            }
            else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esiste già un coach avente lo stesso ruolo nella squadra selezionata");

        } throw new UserNotFoundException();

    }


}
