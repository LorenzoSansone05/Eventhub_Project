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
import org.springframework.web.cors.CorsConfiguration;


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
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(java.util.List.of("http://127.0.0.1:5500", "http://localhost:5500"));
                    config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(java.util.List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers("/api/profiles/me/**").authenticated()
                        .requestMatchers("/api/users/me/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/events").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events/*/rating").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/events/{id}/participants").hasRole("ORGANIZER")
                        .requestMatchers(HttpMethod.POST, "/api/events").hasRole("ORGANIZER")
                        .requestMatchers(HttpMethod.PUT, "/api/events/*").hasRole("ORGANIZER")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/*").hasRole("ORGANIZER")

                        .requestMatchers(HttpMethod.GET, "/api/speakers").hasAnyRole("ORGANIZER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/speakers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/speakers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/speakers/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/venues/**").hasAnyRole("ORGANIZER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/venues/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/venues/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/venues/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/tags").hasAnyRole("ORGANIZER", "ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/tags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/tags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tags/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/feedbacks").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/feedbacks/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/feedbacks/*").hasRole("ADMIN")
                        .requestMatchers("/api/tickets/**").hasRole("USER")

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
                CASE 
                    WHEN r."name" LIKE 'ROLE_%' THEN r."name"
                    ELSE CONCAT('ROLE_', r."name")
                END AS authority
            FROM "User" u
            JOIN "Role" r ON u."role_id" = r."id"
            WHERE u."email" = ?
        """);

        return manager;
    }

}
