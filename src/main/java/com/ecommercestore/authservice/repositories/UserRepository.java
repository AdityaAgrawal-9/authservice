package com.ecommercestore.authservice.repositories;

import com.ecommercestore.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    User save(User user);

    @Override
    Optional<User> findById(Long Id);

    @Override
    boolean existsById(Long Id);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdOrEmail(Long id, String email);
}
