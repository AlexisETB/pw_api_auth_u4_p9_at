package uce.edu.web.api.auth.interfaces;

import java.time.Instant;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

public class AuthResource {
    @GET
    @Path("/token")
    public TokenResponse token(
            @QueryParam("user") String user,
            @QueryParam("password") String password) {
        // Validar usuario y password (simulado)

        boolean ok = true;
        String role = "user";
        if (ok) {
            String issuer = "matricula-auth";
            long ttl = 3600;

            Instant now = Instant.now();
            Instant exp = now.plusSeconds(ttl);

            String jwt = Jwt.issuer(issuer)
                    .subject(user)
                    .groups(Set.of(role)) // roles: user / admin
                    .issuedAt(now)
                    .expiresAt(exp)
                    .sign();

            return new TokenResponse(jwt, exp.getEpochSecond(), role);
        } else {
            // Manejar error de autenticación
            return null;
        }

    }

    public static class TokenResponse {
        public String accessToken;
        public long expiresAt;
        public String role;

        public TokenResponse() {
        }

        public TokenResponse(String accessToken, long expiresAt, String role) {
            this.accessToken = accessToken;
            this.expiresAt = expiresAt;
            this.role = role;
        }
    }
}
