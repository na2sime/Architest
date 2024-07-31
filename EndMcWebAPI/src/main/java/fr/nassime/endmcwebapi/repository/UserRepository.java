package fr.nassime.endmcwebapi.repository;

import fr.nassime.endmcwebapi.api.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, UUID> {
    User findByName(String name);
    User findByUuid(UUID uuid);
}
