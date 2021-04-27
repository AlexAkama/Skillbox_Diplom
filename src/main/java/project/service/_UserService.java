package project.service;

import project.dto.auth.user.AuthUserDto;
import project.exception.UnauthorizedException;
import project.exception.UserNotFoundException;
import project.model.User;

public interface _UserService {

    User createUser(String name, String email, String password);

    User findByEmail (String email) throws UserNotFoundException;

    boolean existByEmail(String email);

    User save(User user);

    User createAndSaveUser(String name, String email, String password);

    AuthUserDto createAuthUserDto(User user);

    AuthUserDto createAuthUserDtoByEmail(String email) throws UserNotFoundException;

    User checkUser() throws UnauthorizedException, UserNotFoundException;

}
