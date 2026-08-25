package com.projecto.gestiondeadmisiones.infrastructure.helpers.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey clave;
    private final long minutosExpiracion;

    public JwtProvider(@Value("${app.jwt.secret}") String secreto,
                        @Value("${app.jwt.expiracion-minutos}") long minutosExpiracion) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes());
        this.minutosExpiracion = minutosExpiracion;
    }

    public String generarToken(Long aspiranteId, String correoElectronico) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(aspiranteId.toString())
                .claim("correo", correoElectronico)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(minutosExpiracion, ChronoUnit.MINUTES)))
                .signWith(clave)
                .compact();
    }
}
