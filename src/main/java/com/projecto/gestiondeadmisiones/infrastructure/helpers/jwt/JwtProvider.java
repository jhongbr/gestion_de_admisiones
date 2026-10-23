package com.projecto.gestiondeadmisiones.infrastructure.helpers.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    private final SecretKey clave;
    private final long minutosExpiracion;

    public JwtProvider(@Value("${app.jwt.secret}") String secreto,
                        @Value("${app.jwt.expiracion-minutos}") long minutosExpiracion) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes());
        this.minutosExpiracion = minutosExpiracion;
    }

    public String generarTokenAspirante(Long aspiranteId, String correoElectronico) {
        return construirToken(aspiranteId.toString(), Map.of(
                "correo", correoElectronico,
                "tipo", "ASPIRANTE"));
    }

    public String generarTokenPersonal(Long personalId, String correoElectronico, String rol) {
        return construirToken(personalId.toString(), Map.of(
                "correo", correoElectronico,
                "tipo", "PERSONAL",
                "rol", rol));
    }

    private String construirToken(String subject, Map<String, Object> claims) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(minutosExpiracion, ChronoUnit.MINUTES)))
                .signWith(clave)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
