package it.unisalento.iot.iotdigitaltwin.restcontrollers;

import it.unisalento.iot.iotdigitaltwin.domain.Amministratore;
import it.unisalento.iot.iotdigitaltwin.domain.Atleta;
import it.unisalento.iot.iotdigitaltwin.domain.Coach;
import it.unisalento.iot.iotdigitaltwin.domain.User;
import it.unisalento.iot.iotdigitaltwin.dto.AuthenticationResponseDTO;
import it.unisalento.iot.iotdigitaltwin.dto.UserDTO;
import it.unisalento.iot.iotdigitaltwin.exceptions.UserNotFoundException;
import it.unisalento.iot.iotdigitaltwin.repositories.AmministratoreRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.AtletaRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.CoachRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.UserRepository;
import it.unisalento.iot.iotdigitaltwin.security.JwtUtilities;
import it.unisalento.iot.iotdigitaltwin.service.CustomUserDetailsService;
import it.unisalento.iot.iotdigitaltwin.service.RequestPasswordChange;
import it.unisalento.iot.iotdigitaltwin.service.RoleResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static it.unisalento.iot.iotdigitaltwin.configuration.SecurityConfig.passwordEncoder;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AtletaRepository atletaRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @RequestMapping(value="/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody it.unisalento.pas.smartcitywastemanagement.dto.LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        User user = userRepository.findByUsername(authentication.getName());

        if (user != null) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final String jwt = jwtUtilities.generateToken(user.getUsername());

            return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
        } else {

            Atleta atleta = atletaRepository.findByUsername(authentication.getName());

            if (atleta != null) {

                SecurityContextHolder.getContext().setAuthentication(authentication);

                final String jwt = jwtUtilities.generateToken(atleta.getUsername());

                return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));

            } else {

                Coach coach = coachRepository.findByUsername(authentication.getName());

                if (coach != null) {

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    final String jwt = jwtUtilities.generateToken(coach.getUsername());

                    return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));

                }

            }

            Amministratore amministratore = amministratoreRepository.findByUsername(authentication.getName());

            if(amministratore != null) {

                SecurityContextHolder.getContext().setAuthentication(authentication);

                final String jwt = jwtUtilities.generateToken(amministratore.getUsername());

                return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));

            } else throw new UsernameNotFoundException(loginDTO.getUsername());

        }

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<RoleResponse> validateToken(HttpServletRequest request) {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            // Effettua la convalida del token utilizzando la logica esistente nel servizio degli utenti
            // Utilizza JwtUtilities per estrarre il nome utente dal token
            String username = jwtUtilities.extractUsername(jwt);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (userDetails != null) {
                // Il token è valido
                System.out.println("Token ricevuto");
                // Effettua la chiamata al servizio di autenticazione per ottenere il ruolo dell'utente
                RoleResponse roleResponse = this.checkRole(username);

                if (roleResponse != null) {
                    // Restituisci il ruolo dell'utente nella risposta
                    return ResponseEntity.ok(roleResponse);
                }
            }
        }

        // Il token non è valido o mancante
        System.out.println("Autenticazione fallita");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @RequestMapping(value = "/role/{username}", method = RequestMethod.GET)
    public RoleResponse checkRole(@PathVariable String username) {
        Atleta atleta = atletaRepository.findByUsername(username);
        if(atleta!=null) {
            RoleResponse response = new RoleResponse();
            response.setRole(atleta.getRole());
            return response;
        }
        else {
            Coach coach = coachRepository.findByUsername(username);
            if(coach!=null){
                RoleResponse response = new RoleResponse();
                response.setRole(coach.getRole());
                return response;
            }
            else{
                Amministratore amministratore = amministratoreRepository.findByUsername(username);
                RoleResponse response = new RoleResponse();
                response.setRole(amministratore.getRole());
                return response;
            }
        }

    }

    @PreAuthorize("hasAnyRole('COACH', 'ATLETA')")
    @PatchMapping(value = "/update/email")
    public ResponseEntity<String> updateEmail(@RequestBody UserDTO userDTO) throws UserNotFoundException {

        Atleta atleta = atletaRepository.findByUsername(userDTO.getUsername());
        Coach coach = coachRepository.findByUsername(userDTO.getUsername());

        if(atleta != null) {
            if(userDTO.getEmail() != null) {
                String email = userDTO.getEmail();
                if(!atletaRepository.existsByEmail(email))
                    atleta.setEmail(email);
                else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email già esistente");
            }

            atletaRepository.save(atleta);
            return ResponseEntity.ok("Atleta aggiornato con successo");

        } else if(coach != null) {
            if(userDTO.getEmail() != null) {
                String email = userDTO.getEmail();
                if(!coachRepository.existsByEmail(email))
                    coach.setEmail(email);
                else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email già esistente");
            }

            coachRepository.save(coach);
            return ResponseEntity.ok("Coach aggiornato con successo");

        }
        throw new UserNotFoundException();

    }


    @PreAuthorize("hasAnyRole('COACH', 'ATLETA')")
    @PatchMapping(value = "/update/password/{username}")
    public ResponseEntity<String> updatePassword(@PathVariable String username, @RequestBody RequestPasswordChange requestPasswordChange) throws UserNotFoundException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        requestPasswordChange.getOld_password()
                )
        );

        Atleta atleta = atletaRepository.findByUsername(username);
        Coach coach = coachRepository.findByUsername(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if(atleta!=null) {
            if(requestPasswordChange.getNew_password() != null) {
                String new_password = requestPasswordChange.getNew_password();;
                atleta.setPassword(passwordEncoder().encode(new_password));
                atletaRepository.save(atleta);

            }
            return ResponseEntity.ok("Password Atleta aggiornata con successo");
        }else {
            if(coach!=null) {
                if(requestPasswordChange.getNew_password() != null) {
                    String new_password = requestPasswordChange.getNew_password();
                    coach.setPassword(passwordEncoder().encode(new_password));
                    coachRepository.save(coach);

                }
                return ResponseEntity.ok("Password Coach aggiornata con successo");
            }
        } throw new UserNotFoundException();
    }
}
