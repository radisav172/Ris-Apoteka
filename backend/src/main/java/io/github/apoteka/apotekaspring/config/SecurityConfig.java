package io.github.apoteka.apotekaspring.config;

import io.github.apoteka.apotekaspring.filter.JwtAuthFilter;
import io.github.apoteka.apotekaspring.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil) {
        return new JwtAuthFilter(jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers("/api/korisnici/**").hasRole("admin")
                .requestMatchers(HttpMethod.POST,   "/api/lekovi/**").hasRole("admin")
                .requestMatchers(HttpMethod.PUT,    "/api/lekovi/**").hasRole("admin")
                .requestMatchers(HttpMethod.DELETE, "/api/lekovi/**").hasRole("admin")
                .requestMatchers(HttpMethod.POST,   "/api/kategorije/**").hasRole("admin")
                .requestMatchers(HttpMethod.PUT,    "/api/kategorije/**").hasRole("admin")
                .requestMatchers(HttpMethod.DELETE, "/api/kategorije/**").hasRole("admin")
                .requestMatchers(HttpMethod.POST,   "/api/dobavljaci/**").hasRole("admin")
                .requestMatchers(HttpMethod.PUT,    "/api/dobavljaci/**").hasRole("admin")
                .requestMatchers(HttpMethod.DELETE, "/api/dobavljaci/**").hasRole("admin")
                .requestMatchers(HttpMethod.DELETE, "/api/prodaje/**").hasRole("admin")
                .requestMatchers(HttpMethod.DELETE, "/api/nabavke/**").hasRole("admin")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
