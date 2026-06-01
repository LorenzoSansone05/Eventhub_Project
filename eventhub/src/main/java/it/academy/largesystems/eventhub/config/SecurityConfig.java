package it.academy.largesystems.eventhub.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events/*/rating").permitAll()

                        .requestMatchers("/api/profiles/me").authenticated()
                        .requestMatchers("/api/users/me/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tags").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/feedbacks").hasRole("USER")
                        .requestMatchers("/api/tickets/**").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/api/events").hasRole("ORGANIZER")
                        .requestMatchers(HttpMethod.PUT, "/api/events/*").hasRole("ORGANIZER")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/*").hasRole("ORGANIZER")

                        .requestMatchers(HttpMethod.GET, "/api/speakers").hasAnyRole("ORGANIZER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/venues").hasAnyRole("ORGANIZER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/venues/*").hasAnyRole("ORGANIZER", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/feedbacks/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/feedbacks/*").hasRole("ADMIN")

                        .requestMatchers("/api/speakers/**").hasRole("ADMIN")
                        .requestMatchers("/api/tags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/venues").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/venues/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/venues/*").hasRole("ADMIN")

                        .requestMatchers("/api/users/by-email/*").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        // UTENTE
        manager.setUsersByUsernameQuery("""
                    SELECT 
                        "email" AS username,
                        "password",
                        NOT "isBanned" AS enabled
                    FROM "User"
                    WHERE "email" = ?
                """);
        // RUOLO
        manager.setAuthoritiesByUsernameQuery("""
                    SELECT 
                        u."email" AS username,
                        r."name" AS authority
                    FROM "User" u
                    JOIN "Role" r ON u."role_id" = r."id"
                    WHERE u."email" = ?
                """);

        return manager;
    }

}
