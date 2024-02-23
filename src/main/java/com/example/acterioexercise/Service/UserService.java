    package com.example.acterioexercise.Service;

    import com.example.acterioexercise.Domain.RequestUpdateEmail;
    import com.example.acterioexercise.Domain.User;
    import com.example.acterioexercise.Repository.UserRepository;
    import com.example.acterioexercise.SecurityConfig;
    import jakarta.persistence.EntityNotFoundException;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class UserService   {

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private SecurityConfig securityConfig;

        public User saveUser(User user){
            return userRepository.save(user);
        }

        public User createUser(User user) throws UserAlreadyExistsException {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("User with this email already exists");
            }

            String encodedPassword = securityConfig.getPasswordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);

            return userRepository.save(user);
        }

        public void deleteUserById(Long id){
             userRepository.deleteById(id);
        }

        public Optional<User> getUserById(Long id){return userRepository.getUserById(id);}


        public Optional<String> findPasswordByEmail(String email){
            return userRepository.findPasswordByEmail(email);
        }

        public Optional<List<String>> findAllDomains(){
            Optional<List<String>> domains;
            domains= userRepository.findEachDomain();

            domains.get().forEach(System.out::println);
            return domains;
        }

        public void updateEmail(long id, RequestUpdateEmail requestUpdateEmail){
            Optional<User> userOptional = userRepository.getUserById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setEmail(requestUpdateEmail.getEmail());
                userRepository.save(user); // Save the updated user to the database
            } else {
                throw new EntityNotFoundException("User not found with ID: " + id);
            }
        }


    }
