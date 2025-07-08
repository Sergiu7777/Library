package org.sergheimorari.library.repository;

import org.sergheimorari.library.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {}
