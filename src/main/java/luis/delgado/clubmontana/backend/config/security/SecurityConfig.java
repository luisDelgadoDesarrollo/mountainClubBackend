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

    return http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        HttpMethod.GET,
                        "/publications/{clubId}/**",
                        "/clubs/{clubId}/activities/**",
                        "/us/{clubId}",
                        "/doc/{clubId}",
                        "/rules/{clubId}",
                        "/images/**",
                        "/test/**",
                        "/clubs/{clubId}/articles/**")
                    .permitAll()
                    .requestMatchers("/auth/**", "/users/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .build();
  }
}
