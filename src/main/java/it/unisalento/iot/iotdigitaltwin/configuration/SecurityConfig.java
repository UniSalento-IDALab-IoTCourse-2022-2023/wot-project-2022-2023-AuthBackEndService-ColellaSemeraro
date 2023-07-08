package it.unisalento.iot.iotdigitaltwin.configuration;

import it.unisalento.iot.iotdigitaltwin.security.JwtAuthenticationFilter;
import it.unisalento.iot.iotdigitaltwin.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .authorizeRequests().requestMatchers("/api/users/authenticate", "/api/users/amministratore/").permitAll().
                anyRequest().authenticated().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();


            /*return http.csrf().disable()
                    .authorizeHttpRequests()
                    .requestMatchers("/api/users/**").authenticated()
                    .and()
                    .authorizeHttpRequests()
                    .requestMatchers("/api/bins/**").permitAll()
                    .and()
                    .httpBasic(Customizer.withDefaults())
                    .build();*/
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }


        /*@Bean
        public UserDetailsService userDetailsService() {

            UserDetails roberto = User.builder()
                    .username("roberto")
                    .password(passwordEncoder().encode("12345"))
                    .roles("ADMIN")
                    .build();

            UserDetails paolo = User.builder()
                    .username("paolo")
                    .password(passwordEncoder().encode("12345"))
                    .roles("USER")
                    .build();

            return new InMemoryUserDetailsManager(roberto, paolo);

        }*/
}