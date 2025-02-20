package io.github.nhatbangle.sdp.software.repository;

import io.github.nhatbangle.sdp.software.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}