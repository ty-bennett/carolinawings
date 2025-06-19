/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
//    List<User> findAll();
//    Optional<User> findById(UUID id);
//    Optional<User> findByEmail(String email);
//    Optional<User> findByName(String name);
//    void deleteById(UUID id);
//    Optional<User> createUser(User user);
//    Optional<User> updateUser(User user);

}
