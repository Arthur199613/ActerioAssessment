package com.example.acterioexercise.Repository;

import com.example.acterioexercise.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<Object> findByEmail(String email);

    @Query("SELECT u.password FROM User u WHERE u.email = :email")
    Optional<String> findPasswordByEmail(String email);

    @Query("SELECT email FROM User")
    Optional<List<String>> findEachDomain();

    Optional<User> getUserById(long id);
    Optional<User> deleteUserByIdIs(long id);





}

