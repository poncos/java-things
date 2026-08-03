package com.ecollado.samples.oauth_rest_sample.security;


import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${jwt.signature-verification.enabled:true}")
    private boolean signatureVerificationEnabled;

    @Bean
    public JwtDecoder jwtDecoder() {
        if (signatureVerificationEnabled) {
            // Production: validate signature, timestamp, and issuer
            NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
            decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                    new JwtTimestampValidator(),
                    new JwtIssuerValidator(issuerUri)
            ));
            return decoder;
        } else {
            // Development: skip ALL validation including signature
            return token -> {
                try {
                    JWT jwt = JWTParser.parse(token);
                    Map<String, Object> claims = new HashMap<>(jwt.getJWTClaimsSet().getClaims());
                    
                    // Convert java.util.Date timestamps to java.time.Instant
                    if (claims.containsKey("exp") && claims.get("exp") instanceof Date) {
                        claims.put("exp", ((Date) claims.get("exp")).toInstant());
                    }
                    if (claims.containsKey("iat") && claims.get("iat") instanceof Date) {
                        claims.put("iat", ((Date) claims.get("iat")).toInstant());
                    }
                    if (claims.containsKey("nbf") && claims.get("nbf") instanceof Date) {
                        claims.put("nbf", ((Date) claims.get("nbf")).toInstant());
                    }
                    
                    // Log token scopes and other claims
                    log.debug("JWT Claims: {}", claims);
                    Object scopes = claims.get("scope");
                    if (scopes != null) {
                        log.info(">>>> Token Scopes: {}", scopes);
                    } else {
                        log.info(">>>> No Scopes encoded within the Token.");
                    }
                    
                    return Jwt.withTokenValue(token)
                            .headers(h -> h.putAll(jwt.getHeader().toJSONObject()))
                            .claims(c -> c.putAll(claims))
                            .build();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse JWT", e);
                }
            };
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.disable())
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**","/swagger-ui/**","/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                ).oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(Customizer.withDefaults())
                ).sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return httpSecurity.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}