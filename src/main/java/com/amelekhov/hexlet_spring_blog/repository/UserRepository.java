package com.amelekhov.hexlet_spring_blog.repository;

import com.amelekhov.hexlet_spring_blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
