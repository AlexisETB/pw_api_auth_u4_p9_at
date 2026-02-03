package uce.edu.web.api.auth.infrastructure;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import uce.edu.web.api.auth.domain.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
