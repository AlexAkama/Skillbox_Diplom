package project.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import project.model.User;
import project.repository._UserRepository;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final _UserRepository userRepository;

    public UserDetailsServiceImpl(_UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
        return SecurityUser.fromUser(user);
    }

}
