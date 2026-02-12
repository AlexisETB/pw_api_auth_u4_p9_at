package uce.edu.web.api.auth.interfaces;

import java.time.Instant;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uce.edu.web.api.auth.application.UserService;
import uce.edu.web.api.auth.application.representation.UserRepresentation;

@ApplicationScoped
@Path("/auth")
public class AuthResource {

    @Inject
    UserService userService;

    //http://localhost:8082/auth/token?user=admn1&password=1234a
    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(
            @QueryParam("user") String user,
            @QueryParam("password") String password) {
        // Validar usuario y password usando UserService
        UserRepresentation userRepresentation = userService.validarCredencial(user, password);

        if (userRepresentation != null) {
            String role = userRepresentation.getRole();
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

            return Response.ok(new TokenResponse(jwt, exp.getEpochSecond(), role)).build();
        } else {
            // Manejar error de autenticación - credenciales inválidas
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Credenciales inválidas",
                            "Las credenciales proporcionadas no son válidas"))
                    .build();
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

    public static class ErrorResponse {
        public String error;
        public String message;

        public ErrorResponse() {
        }

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
    }
}
