package com.poligran.tsp.sistema_encuestas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.poligran.tsp.sistema_encuestas.services.CustomUserDetailsService;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(Customizer.withDefaults())
                .httpBasic(h -> h.disable())
                .formLogin(login -> 
                        login.defaultSuccessUrl("/customer"))
                .authorizeHttpRequests(httz -> httz
                        .requestMatchers("/", "/r/*").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/business").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .build();

    }


    @Bean
    AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }   

    @Bean
    AuthenticationManager authenticationManager() {
        ProviderManager authenticationManager = new ProviderManager(daoAuthenticationProvider());
        return authenticationManager;
    }
}
