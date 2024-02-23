package com.example.acterioexercise;

import com.example.acterioexercise.Domain.User;
import com.example.acterioexercise.Repository.UserRepository;
import com.example.acterioexercise.Service.UserAlreadyExistsException;
import com.example.acterioexercise.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class databaseInitialisationTest implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;


    @Override
    public void run(String... args) throws Exception {
        User a = new User("john","doe","a@gmail.com","1234");
        User b = new User("carl","doe","b@outlook.com","1234");
        User c = new User("bill","Yeates","c@cheese.com","1555");
        try {
            userService.createUser(a);
            userService.createUser(b);
            userService.createUser(c);

        } catch (UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }
}
