package com.bunch.of.ideas.doctorapi.repository;


import com.bunch.of.ideas.doctorapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findUserByEmail(String email);
}
