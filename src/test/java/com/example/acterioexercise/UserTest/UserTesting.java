package com.example.acterioexercise.UserTest;

import com.example.acterioexercise.Domain.User;
import com.example.acterioexercise.Repository.UserRepository;
import com.example.acterioexercise.Service.UserAlreadyExistsException;
import com.example.acterioexercise.Service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
@Transactional
public class UserTesting {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;


    @Test
    public void userCreated() throws UserAlreadyExistsException {
        User user = new User("Dave", "Ho","dave@gmail.com","1234");

        userService.createUser(user);

        Assert.isTrue(userRepository.findByEmail("dave@gmail.com").isPresent(),"Is true");
    }


}
