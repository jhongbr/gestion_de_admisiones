package com.projecto.gestiondeadmisiones.infrastructure.helpers.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    public JwtReactiveAuthenticationManager(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        return Mono.fromCallable(() -> jwtProvider.parseClaims(token))
                .map(claims -> (Authentication) new UsernamePasswordAuthenticationToken(
                        obtenerId(claims), token, obtenerAutoridades(claims)))
                .onErrorResume(e -> Mono.empty());
    }

    private Long obtenerId(Claims claims) {
        return Long.parseLong(claims.getSubject());
    }

    private List<SimpleGrantedAuthority> obtenerAutoridades(Claims claims) {
        String tipo = claims.get("tipo", String.class);
        if ("PERSONAL".equals(tipo)) {
            String rol = claims.get("rol", String.class);
            return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_ASPIRANTE"));
    }
}
