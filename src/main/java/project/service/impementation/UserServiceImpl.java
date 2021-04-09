package project.service.impementation;

import org.springframework.stereotype.Service;
import project.exception.UserNotFoundException;
import project.model.User;
import project.repository.UserRepository;
import project.service.UserService;

@Service
public class UserServiceImpl implements UserService {

 private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, String email, String password) {
        User user = new User(name, email, password);
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", email)));
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User createAndSaveUser(String name, String email, String password) {
        return save(createUser(name, email, password));
    }

}
