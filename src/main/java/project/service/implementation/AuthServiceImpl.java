package project.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.dto.auth.login.LoginRequest;
import project.dto.auth.registration.*;
import project.dto.auth.user.AuthResponse;
import project.dto.auth.user.AuthUserDto;
import project.dto.main.AppResponse;
import project.exception.NotFoundException;
import project.service.*;

import javax.servlet.http.*;
import java.security.Principal;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final CaptchaService captchaService;
    private final AuthenticationManager authenticationManager;

    /**
     * Минимальная длинна пароля
     */
    @Value("${config.password.minlength}")
    private int passwordMinLength;

    public AuthServiceImpl(UserService userService,
                           CaptchaService captchaService,
                           AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.captchaService = captchaService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<AuthResponse> checkUserAuthorization(Principal principal) throws NotFoundException {
        AuthResponse response = new AuthResponse();
        if (principal != null) {
            response = new AuthResponse(userService.createAuthUserDtoByEmail(principal.getName()));
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        AuthResponse response = new AuthResponse();
        try {
            AuthUserDto user = userService.createAuthUserDtoByEmail(request.getEmail());
            response.setUser(user);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (NotFoundException ignore) {
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RegistrationResponse> registration(RegistrationRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();
        String code = request.getCode();
        String secret = request.getSecret();

        boolean emailCorrect = emailIsCorrect(email);
        boolean captchaCorrect = captchaService.codeIsCorrect(code, secret);
        boolean nameCorrect = nameIsCorrect(name);
        boolean passwordCorrect = passwordIsCorrect(password);

        boolean registration = emailCorrect && nameCorrect && passwordCorrect && captchaCorrect;
        RegistrationResponse response = new RegistrationResponse();
        if (registration) {
            userService.createAndSaveUser(name, email, password);
        } else {
            RegistrationErrorMap errors = new RegistrationErrorMap();
            if (!emailCorrect) errors.addEmailError();
            if (!nameCorrect) errors.addNameError();
            if (!passwordCorrect) errors.addPasswordError();
            if (!captchaCorrect) errors.addCaptchaError();
            response.setErrors(errors.getErrors());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AppResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        for (Cookie cookie : request.getCookies()) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(new AppResponse().ok());
    }

    @Override
    public boolean nameIsCorrect(String name) {
        return name.length() == name.replaceAll("[^A-Za-zА-Яа-яЁё\\s]+", "").length();
    }

    @Override
    public boolean emailIsCorrect(String email) {
        return !userService.existByEmail(email);
    }

    @Override
    public boolean passwordIsCorrect(String password) {
        return password.length() >= passwordMinLength;
    }

}
