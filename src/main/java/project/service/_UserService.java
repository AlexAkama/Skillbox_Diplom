package project.service;

import project.dto.auth.user.AuthUserDto;
import project.model.User;

public interface _UserService {

    User createUser(String name, String email, String password);

    User findByEmail (String email);

    boolean existByEmail(String email);

    User save(User user);

    User createAndSaveUser(String name, String email, String password);

    AuthUserDto createAuthUserDto(User user);

    AuthUserDto createAuthUserDtoByEmail(String email);

}
