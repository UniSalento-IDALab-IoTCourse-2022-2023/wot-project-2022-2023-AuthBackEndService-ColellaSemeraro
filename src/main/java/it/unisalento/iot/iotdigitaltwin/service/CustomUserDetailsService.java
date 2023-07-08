package it.unisalento.iot.iotdigitaltwin.service;

import it.unisalento.iot.iotdigitaltwin.domain.Amministratore;
import it.unisalento.iot.iotdigitaltwin.domain.Atleta;
import it.unisalento.iot.iotdigitaltwin.domain.Coach;
import it.unisalento.iot.iotdigitaltwin.domain.User;
import it.unisalento.iot.iotdigitaltwin.repositories.AmministratoreRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.AtletaRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.CoachRepository;
import it.unisalento.iot.iotdigitaltwin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AtletaRepository atletaRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final User user = userRepository.findByUsername(username);

        if(user != null) {
            return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles("USER").build();
        }

        final Atleta atleta = atletaRepository.findByUsername(username);
        if (atleta != null) {
            return org.springframework.security.core.userdetails.User.withUsername(atleta.getUsername())
                    .password(atleta.getPassword())
                    .roles("ATLETA")
                    .build();
        }

        final Coach coach = coachRepository.findByUsername(username);
        if (coach != null) {
            return org.springframework.security.core.userdetails.User.withUsername(coach.getUsername())
                    .password(coach.getPassword())
                    .roles("COACH")
                    .build();
        }

        final Amministratore amministratore = amministratoreRepository.findByUsername(username);
        if(amministratore != null) {
            return org.springframework.security.core.userdetails.User.withUsername(amministratore.getUsername())
                    .password(amministratore.getPassword())
                    .roles("AMMINISTRATORE")
                    .build();
        }

        throw new UsernameNotFoundException(username);

    }
}
