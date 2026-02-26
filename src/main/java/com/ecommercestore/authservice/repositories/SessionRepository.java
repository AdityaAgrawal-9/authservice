package com.ecommercestore.authservice.repositories;

import com.ecommercestore.authservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<List<Session>> findByUser_Id(Long userId);
}
