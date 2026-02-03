package uce.edu.web.api.auth.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import uce.edu.web.api.auth.application.representation.UserRepresentation;
import uce.edu.web.api.auth.domain.User;
import uce.edu.web.api.auth.infrastructure.UserRepository;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public UserRepresentation validarCredencial(String username, String password) {
        User user = userRepository.find("username = ?1 and password = ?2", username, password).firstResult();
        if (user != null) {
            return toRepresentation(user);
        }
        return null;
    }

    private UserRepresentation toRepresentation(User user) {
        UserRepresentation representation = new UserRepresentation();
        representation.setUsername(user.getUsername());
        representation.setRole(user.getRole());
        return representation;
    }

}
