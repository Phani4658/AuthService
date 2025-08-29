package org.financetracker.repository;

import org.financetracker.entities.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users, Long> {
    public Users findByUsername(String username);
}
