package it.unisalento.iot.iotdigitaltwin.restcontrollers;

import it.unisalento.iot.iotdigitaltwin.domain.Atleta;
import it.unisalento.iot.iotdigitaltwin.domain.Coach;
import it.unisalento.iot.iotdigitaltwin.dto.AtletaDTO;
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
@RequestMapping("/api/users/atleta")
public class AtletaRestController {

    @Autowired
    AtletaRepository atletaRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;


    @PreAuthorize("hasAnyRole('AMMINISTRATORE')")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AtletaDTO creaAtleta(@RequestBody AtletaDTO atletaDTO) {

        if ((coachRepository.findByUsername(atletaDTO.getUsername())!= null) || (atletaRepository.findByUsername(atletaDTO.getUsername())!=null) || (amministratoreRepository.findByUsername(atletaDTO.getUsername())!=null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }


        if ((coachRepository.existsByEmail(atletaDTO.getEmail())) || (atletaRepository.existsByEmail(atletaDTO.getEmail()) || (amministratoreRepository.existsByEmail(atletaDTO.getEmail())))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        Atleta atleta = new Atleta();

        atleta.setUsername(atletaDTO.getUsername());
        atleta.setNome(atletaDTO.getNome());
        atleta.setCognome(atletaDTO.getCognome());
        atleta.setEmail(atletaDTO.getEmail());
        atleta.setDataNascita(atletaDTO.getDataNascita());
        atleta.setPassword(passwordEncoder().encode(atletaDTO.getPassword()));
        atleta.setRole("ATLETA");
        atleta.setSquadra(atletaDTO.getSquadra());
        atleta.setPosizioneCampo(atletaDTO.getPosizioneCampo());
        atleta.setAltezza(atletaDTO.getAltezza());
        atleta.setPeso(atletaDTO.getPeso());
        atleta.setIdCoach(atletaDTO.getIdCoach());

        atletaRepository.save(atleta);

        atletaDTO.setPassword(atleta.getPassword());
        atletaDTO.setId(atleta.getId());
        atletaDTO.setRole(atleta.getRole());

        return atletaDTO;
    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE')")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<AtletaDTO> trovaTutti() {

        List<AtletaDTO> atleti = new ArrayList<>();

        for (Atleta atleta : atletaRepository.findAll()){
            AtletaDTO atletaDTO = new AtletaDTO();

            atletaDTO.setId(atleta.getId());
            atletaDTO.setUsername(atleta.getUsername());
            atletaDTO.setNome(atleta.getNome());
            atletaDTO.setCognome(atleta.getCognome());
            atletaDTO.setEmail(atleta.getEmail());
            atletaDTO.setDataNascita(atleta.getDataNascita());
            atletaDTO.setPassword(passwordEncoder().encode(atleta.getPassword()));
            atletaDTO.setRole("ATLETA");
            atletaDTO.setSquadra(atleta.getSquadra());
            atletaDTO.setPosizioneCampo(atleta.getPosizioneCampo());
            atletaDTO.setAltezza(atleta.getAltezza());
            atletaDTO.setPeso(atleta.getPeso());
            atletaDTO.setIdCoach(atleta.getIdCoach());

            atleti.add(atletaDTO);
        }

        return atleti;
    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'ATLETA', 'COACH')")
    @RequestMapping(value = "/findById/{atletaId}", method = RequestMethod.GET)
    public AtletaDTO trovaPerId(@PathVariable String atletaId) {

        AtletaDTO atletaDTO = new AtletaDTO();

        Optional<Atleta> optionalAtleta = atletaRepository.findById(atletaId);
        if (optionalAtleta.isPresent()){
            Atleta atleta = optionalAtleta.get();

            atletaDTO.setId(atleta.getId());
            atletaDTO.setUsername(atleta.getUsername());
            atletaDTO.setNome(atleta.getNome());
            atletaDTO.setCognome(atleta.getCognome());
            atletaDTO.setEmail(atleta.getEmail());
            atletaDTO.setDataNascita(atleta.getDataNascita());
            atletaDTO.setPassword(passwordEncoder().encode(atleta.getPassword()));
            atletaDTO.setRole("ATLETA");
            atletaDTO.setSquadra(atleta.getSquadra());
            atletaDTO.setPosizioneCampo(atleta.getPosizioneCampo());
            atletaDTO.setAltezza(atleta.getAltezza());
            atletaDTO.setPeso(atleta.getPeso());
            atletaDTO.setIdCoach(atleta.getIdCoach());
        }

        return atletaDTO;
    }


    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'ATLETA', 'COACH')")
    @RequestMapping(value = "/find/username/{username}", method = RequestMethod.GET)
    public AtletaDTO getByUsername(@PathVariable String username) {

        AtletaDTO atletaDTO = new AtletaDTO();

        Atleta atleta = atletaRepository.findByUsername(username);

        atletaDTO.setId(atleta.getId());
        atletaDTO.setUsername(atleta.getUsername());
        atletaDTO.setNome(atleta.getNome());
        atletaDTO.setCognome(atleta.getCognome());
        atletaDTO.setEmail(atleta.getEmail());
        atletaDTO.setDataNascita(atleta.getDataNascita());
        atletaDTO.setPassword(passwordEncoder().encode(atleta.getPassword()));
        atletaDTO.setRole("ATLETA");
        atletaDTO.setSquadra(atleta.getSquadra());
        atletaDTO.setPosizioneCampo(atleta.getPosizioneCampo());
        atletaDTO.setAltezza(atleta.getAltezza());
        atletaDTO.setPeso(atleta.getPeso());
        atletaDTO.setIdCoach(atleta.getIdCoach());

        return atletaDTO;

    }



    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'COACH')")
    @RequestMapping(value = "/findByCoachId/{idCoach}", method = RequestMethod.GET)
    public List<AtletaDTO> trovaPerIdCoach(@PathVariable String idCoach) {

        List<AtletaDTO> atleti = new ArrayList<>();

        for (Atleta atleta : atletaRepository.findAllByIdCoach(idCoach)) {
            AtletaDTO atletaDTO = new AtletaDTO();

            atletaDTO.setId(atleta.getId());
            atletaDTO.setUsername(atleta.getUsername());
            atletaDTO.setNome(atleta.getNome());
            atletaDTO.setCognome(atleta.getCognome());
            atletaDTO.setEmail(atleta.getEmail());
            atletaDTO.setDataNascita(atleta.getDataNascita());
            atletaDTO.setPassword(passwordEncoder().encode(atleta.getPassword()));
            atletaDTO.setRole("ATLETA");
            atletaDTO.setSquadra(atleta.getSquadra());
            atletaDTO.setPosizioneCampo(atleta.getPosizioneCampo());
            atletaDTO.setAltezza(atleta.getAltezza());
            atletaDTO.setPeso(atleta.getPeso());
            atletaDTO.setIdCoach(atleta.getIdCoach());

            atleti.add(atletaDTO);
        }

        return atleti;
    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'COACH')")
    @RequestMapping(value = "/existsByCoachId/{idCoach}", method = RequestMethod.GET)
    public boolean checkIfAtletaAllenatoByIdCoach(@PathVariable String idCoach) {
        return atletaRepository.existsByIdCoach(idCoach);
    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE')")
    @PatchMapping(value = "/updateRuoloSquadraCoach/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateRuoloSquadraCoach(@PathVariable String username, @RequestBody AtletaDTO atletaDTO) throws UserNotFoundException {

        Atleta atleta = atletaRepository.findByUsername(username);

        if(atleta != null) {

            if(atletaDTO.getSquadra() != null) {
                String squadra = atletaDTO.getSquadra();
                atleta.setSquadra(squadra);
            }

            if(atletaDTO.getIdCoach() != null) {
                String idCoach = atletaDTO.getIdCoach();
                atleta.setIdCoach(idCoach);
            }

            atletaRepository.save(atleta);
            return ResponseEntity.ok("Coach aggiornato con successo");


        } throw new UserNotFoundException();

    }

    @PreAuthorize("hasAnyRole('COACH')")
    @PatchMapping(value = "/updateAltezzaEPeso/{idCoach}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAltezzaEPesoAtleta(@PathVariable String idCoach, @RequestBody AtletaDTO atletaDTO) throws UserNotFoundException {

        Atleta atleta = atletaRepository.findByUsername(atletaDTO.getUsername());

        if(atleta != null) {

            if (idCoach.equals(atletaDTO.getIdCoach())){
                if(atletaDTO.getAltezza() != 0) {
                    int altezza = atletaDTO.getAltezza();
                    atleta.setAltezza(altezza);
                }

                if(atletaDTO.getPeso() != 0) {
                    double peso = atletaDTO.getPeso();
                    atleta.setPeso(peso);
                }

                atletaRepository.save(atleta);

                return ResponseEntity.ok("Peso e/o altezza aggiornati con successo");
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } throw new UserNotFoundException();
    }

    @PreAuthorize("hasAnyRole('AMMINISTRATORE', 'COACH', 'ATLETA')")
    @RequestMapping(value = "/findIdCoachByIdAtleta/{idAtleta}", method = RequestMethod.GET)
    public String trovaIdCoach(@PathVariable String idAtleta){

        Optional<Atleta> optAtleta = atletaRepository.findById(idAtleta);

        Atleta atleta = new Atleta();

        if (optAtleta.isPresent()) {
            atleta = optAtleta.get();
        }

        return atleta.getIdCoach();
    }
}
