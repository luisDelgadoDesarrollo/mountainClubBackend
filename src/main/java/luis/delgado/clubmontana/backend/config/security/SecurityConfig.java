package luis.delgado.clubmontana.backend.config.security;

import luis.delgado.clubmontana.backend.config.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

    return http.cors(cors -> {})
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        HttpMethod.GET,
                        "/clubs/{club}/activities/**",
                        "/clubs/{club}/articles/**",
                        "/clubs/{club}/doc/**",
                        "/images/**",
                        "/clubs/{club}/publications/**",
                        "/clubs/{club}/rules/**",
                        "/test/**",
                        "/clubs/{club}/us/**",
                        "/clubs/{club}/contactInfo",
                        "/clubs/{club}/users/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/**", "/clubs/{club}/contact/**")
                    .permitAll()
                    .requestMatchers("/users/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .build();
  }
}
